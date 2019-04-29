package com.itstyle.service;

import com.itstyle.common.PageResponse;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.car.manager.ChargeRecordExcelModel;
import com.itstyle.domain.car.manager.ChargeRecordExcelModel2;
import com.itstyle.domain.car.manager.FixedCarManager;
import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.car.manager.enums.ChargeType;
import com.itstyle.domain.report.ChargeRecord;
import com.itstyle.domain.report.ChargeRecordStatistics;
import com.itstyle.mapper.ChargeRecordMapper;
import com.itstyle.utils.FeeUtil;
import com.itstyle.utils.FileUtils;
import com.itstyle.utils.hibernate.BaseDaoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChargeRecordService extends BaseDaoService<ChargeRecord, Long> {
    @Resource
    private ChargeRecordMapper chargeRecordMapper;
    @Resource
    private GlobalSettingService globalSettingService;
    @Resource
    private MonthCarInfoService monthCarInfoService;

    @PostConstruct
    private void init() {
        this.jpaRepository = chargeRecordMapper;
    }

    public void upload(ChargeRecord chargeRecord) {
        chargeRecord.setTime(System.currentTimeMillis());
        add(chargeRecord);
    }

    public PageResponse<ChargeRecord> query(int page, int limit, ChargeType chargeType, CarType carType,CarType carRealType,
                                            String carNum, String chargePersonnel, Long startTime, Long endTime) {
        PageRequest pageRequest = PageResponse.getPageRequest(page, limit);
        Specification<ChargeRecord> sp = fillSpecification(chargeType, carType, carRealType, carNum, chargePersonnel, startTime, endTime);
        Page<ChargeRecord> all = chargeRecordMapper.findAll(sp, pageRequest);
        //月租车特殊处理,需要查询关联月租车
        if (carType == CarType.MONTH_CAR_A) {
            List<ChargeRecord> monthContent = all.getContent();
            monthCarAssociated(monthContent);
            return new PageResponse<>((long) monthContent.size(), monthContent);
        }
        return PageResponse.build(all);
    }

    private void monthCarAssociated(List<ChargeRecord> monthContent) {
        monthContent.forEach(e -> {
                    Long associateId = e.getAssociateId();
                    if (associateId != null) {
                        MonthCarInfo monthCarInfo = monthCarInfoService.findById(associateId);
                        if (monthCarInfo != null) {
                            if (StringUtils.isNotEmpty(monthCarInfo.getCarNum())) {
                                e.setCarNum(monthCarInfo.getCarNum());
                            }
                            if (monthCarInfo.getCarType() != null) {
                                e.setCarRealType(monthCarInfo.getCarType());
                            }
                        }
                    }
                });
    }

    public ChargeRecordStatistics statistics(ChargeType chargeType, CarType carType, CarType carRealType, String carNum, String chargePersonnel, Long startTime, Long endTime) {
        List<Object[]> statistics = chargeRecordMapper.statistics(
                chargeType == null ? null : chargeType.ordinal(),
                carType == null ? null : carType.ordinal(),
                carRealType == null ? null : carRealType.ordinal(),
                StringUtils.isEmpty(carNum) ? null : carNum,
                StringUtils.isEmpty(chargePersonnel) ? null : chargePersonnel,
                startTime, endTime);
        ChargeRecordStatistics crs = new ChargeRecordStatistics();
        if (statistics != null && statistics.size() > 0) {
            Object[] datas = statistics.get(0);
            crs.setTotleFee(datas[0] == null ? null : ((BigDecimal)datas[0]).intValue());
            crs.setTotleReceivableFee(datas[1] == null ? null : ((BigDecimal)datas[1]).intValue());
            crs.setTotleDiscountAmount(datas[2] == null ? null : ((BigDecimal)datas[2]).intValue());
        }
        return crs;
    }

    public List<Object> statisticsTemp(Integer carType, Integer count) {
        return chargeRecordMapper.statisticsTemp(carType, count);
    }

    public ResponseEntity<byte[]> exportExcel(CarType carType) {
        List<FixedCarManager> f = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
        List<ChargeRecord> chargeRecordList = chargeRecordMapper.findByCarType(carType);
        String fileName;
        if (carType == CarType.TEMP_CAR_A) {
            fileName = "临时车收费明细.xlsx";
            List<ChargeRecordExcelModel> data = chargeRecordList.stream().map(m -> ChargeRecordExcelModel.convert(m, f)).collect(Collectors.toList());
            return FileUtils.buildExcelResponseEntity(data, ChargeRecordExcelModel.class, fileName);
        }else{
            fileName = "月租车明细.xlsx";
            monthCarAssociated(chargeRecordList);
            List<ChargeRecordExcelModel2> data = chargeRecordList.stream().map(m -> ChargeRecordExcelModel2.convert(m, f)).collect(Collectors.toList());
            return FileUtils.buildExcelResponseEntity(data, ChargeRecordExcelModel2.class, fileName);
        }
    }

    private Specification<ChargeRecord> fillSpecification(ChargeType chargeType, CarType carType, CarType carRealType, String carNum, String chargePersonnel, Long startTime, Long endTime) {
        return (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();
            if (chargeType != null) {
                Predicate p1 = cb.equal(root.get("chargeType").as(Integer.class), chargeType.ordinal());
                predicate.add(p1);
            }
            if (carRealType != null) {
                Predicate p1 = cb.equal(root.get("carRealType").as(Integer.class), carRealType.ordinal());
                predicate.add(p1);
            }
            if (StringUtils.isNotEmpty(carNum)) {
                Predicate p1 = cb.like(root.get("carNum").as(String.class), "%"+carNum+"%");
                predicate.add(p1);
            }
            if (carType != null) {
                Predicate p1 = cb.equal(root.get("carType").as(Integer.class), carType.ordinal());
                predicate.add(p1);
            }
            if (StringUtils.isNotEmpty(chargePersonnel)) {
                Predicate p1 = cb.equal(root.get("chargePersonnel").as(String.class), chargePersonnel);
                predicate.add(p1);
            }
            if (startTime != null && endTime != null) {
                Predicate p1 = cb.between(root.get("time").as(Long.class), startTime, endTime);
                predicate.add(p1);
            }
            query.orderBy(cb.desc(root.get("time")));
            query.where(predicate.toArray(new Predicate[0]));
            return query.getRestriction();
        };
    }
}
