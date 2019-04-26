package com.itstyle.service;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.itstyle.common.PageResponse;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.account.Account;
import com.itstyle.domain.car.manager.CarInfoExcelModel;
import com.itstyle.domain.car.manager.FixedCarManager;
import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.car.manager.enums.CarType2;
import com.itstyle.domain.car.manager.enums.ChargeType;
import com.itstyle.domain.report.ChargeRecord;
import com.itstyle.mapper.MonthCarInfoMapper;
import com.itstyle.service.listener.MonthCarExcelListener;
import com.itstyle.utils.DateUtil;
import com.itstyle.utils.FileUtils;
import com.itstyle.utils.hibernate.BaseDaoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MonthCarInfoService extends BaseDaoService<MonthCarInfo, Long>{
    @Resource
    private MonthCarInfoMapper monthCarInfoMapper;
    @Resource
    private ChargeRecordService chargeRecordService;
    @Resource
    private CarInfoService carInfoService;
    @Resource
    private GlobalSettingService globalSettingService;
    @Resource
    private DeleteRecordService deleteRecordService;

    @PostConstruct
    private void init() {
        jpaRepository = monthCarInfoMapper;
    }

    public PageResponse<MonthCarInfo> queryLimit(int page, int limit, String queryStr) {
        PageRequest pageRequest = PageResponse.getPageRequest(page, limit);
        Specification<MonthCarInfo> sp = (root, query, cb) -> {
            if (StringUtils.isNotEmpty(queryStr)) {
                Predicate p1 = cb.like(root.get("carNum").as(String.class), "%" + queryStr + "%");
                Predicate p2 = cb.equal(root.get("phone").as(String.class), queryStr);
                query.where(cb.or(p1, p2));
            }
            return query.getRestriction();
        };
        Page<MonthCarInfo> all = monthCarInfoMapper.findAll(sp, pageRequest);
        return PageResponse.build(all);
    }

    public void payment(Integer month, Long id, Account account) {
        MonthCarInfo one = findById(id);
        Long endTime = one.getEndTime();
//        Long now = System.currentTimeMillis();
//        if (endTime < now) {
//            endTime = now;
//        }
        endTime = DateUtil.calcAfterMonthTime(endTime, month);
        one.setEndTime(endTime);
        one.setModifyTime(new Date());
        update(one.getId(), one);
        //上传月租车续费信息
        chargeRecord(one, month, account);
    }


    /**
     * 新增和更新车辆
     * @param monthCarInfo
     * @return
     */
    public String edit(MonthCarInfo monthCarInfo, Integer month, Account account) {
        String result = carInfoService.verification(monthCarInfo.getCarNum());
        long now = System.currentTimeMillis();
        if (monthCarInfo.getId() == null) {
            //add
            if (monthCarInfo.getStartTime() == null) {
                monthCarInfo.setStartTime(now);
            }
            if (month != null) {
                monthCarInfo.setEndTime(DateUtil.calcAfterMonthTime(now, month));
            }
            monthCarInfo.setCreateTime(new Date(now));
            if(!result.equals("")){
                return result;
            }
            add(monthCarInfo);
            //上传月租车续费信息
            chargeRecord(monthCarInfo, month, account);
            return "添加成功！";
        }else{
            //update 这个接口不得修改 startTime 和 endTime ，如果需要修改需要去续费接口
            monthCarInfo.setStartTime(null);
            monthCarInfo.setEndTime(null);
            monthCarInfo.setModifyTime(new Date());
            MonthCarInfo byId = findById(monthCarInfo.getId());
            if (!byId.getCarNum().equals(monthCarInfo.getCarNum())) {
                if(result!=""){
                    return result;
                }
            }
            update(monthCarInfo.getId(), monthCarInfo);
            return "修改成功！";
        }
    }

    @Override
    public void delete(Long id) {
        MonthCarInfo one = monthCarInfoMapper.getOne(id);

        monthCarInfoMapper.delete(id);

        deleteRecordService.upload(one.getCarNum(), CarType2.MONTH_CAR);
    }

    /**
     * 查找区域时间内新增的车
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    public List<MonthCarInfo> getCarAddInfo(Long startTime, Long endTime) {
        Specification<MonthCarInfo> sp = (root, query, cb) -> {
            if (startTime != null && endTime != null) {
                Predicate p1 = cb.between(root.get("createTime").as(Date.class), new Date(startTime), new Date(endTime));
                query.where(p1);
            }
            return query.getRestriction();
        };
        return monthCarInfoMapper.findAll(sp);
    }

    /**
     * 查找区域时间内续费的车
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    public List<MonthCarInfo> getCarRenewInfo(Long startTime, Long endTime) {
        Specification<MonthCarInfo> sp = (root, query, cb) -> {
            if (startTime != null && endTime != null) {
                Predicate p1 = cb.between(root.get("modifyTime").as(Date.class), new Date(startTime), new Date(endTime));
                query.where(p1);
            }
            return query.getRestriction();
        };
        return monthCarInfoMapper.findAll(sp);
    }

    public List<MonthCarInfo> list() {
        return monthCarInfoMapper.findAll();
    }

    private void chargeRecord(MonthCarInfo one, Integer month, Account account) {
        Integer fee = getMonthFee(one.getCarType()) * month;
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setCarNum(one.getCarNum());
        chargeRecord.setCarType(CarType.MONTH_CAR_A);
        chargeRecord.setCarRealType(one.getCarType());
        chargeRecord.setChargeType(ChargeType.CASH_PAYMENT);
        chargeRecord.setEnterTime(one.getStartTime());
        chargeRecord.setLeaveTime(one.getEndTime());
        chargeRecord.setFee(fee);
        chargeRecord.setChargePersonnel(account.getUsername());
        chargeRecordService.upload(chargeRecord);
    }

    public int getMonthFee(CarType carType) {
        List<FixedCarManager> f = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
        Optional<FixedCarManager> any = f.stream().filter(e -> e.getCarType() == carType).findAny();
        if (any.isPresent()) {
            FixedCarManager fixedCarManager = any.get();
            if (fixedCarManager.getMonthFee() == null) {
                return 0;
            }
            return fixedCarManager.getMonthFee();
        }
        return 0;
    }

	public MonthCarInfo getByCarNum(String carNum){return monthCarInfoMapper.findByCarNum(carNum);}

    /**
     * 导出所有数据到EXCEL
     */
    public ResponseEntity<byte[]> exportExcel() {
        List<MonthCarInfo> carInfos = monthCarInfoMapper.findAll();
        List<FixedCarManager> f = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
        List<CarInfoExcelModel> data = carInfos.stream().map(m -> CarInfoExcelModel.convert(m, f)).collect(Collectors.toList());
        return FileUtils.buildExcelResponseEntity(data, CarInfoExcelModel.class, "月租车数据.xlsx");
    }

    public void importExcel(MultipartFile excel, Account account) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = excel.getInputStream();
            // 解析每行结果在listener中处理
            MonthCarExcelListener listener = new MonthCarExcelListener(globalSettingService,this, account);

            ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLSX, null, listener);

            excelReader.read(new Sheet(1, 1, CarInfoExcelModel.class));
        } catch (Exception e) {
            log.error("importExcel error1",e);
            throw e;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("importExcel error2",e);
            }
        }

    }
}
