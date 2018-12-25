package com.itstyle.service;

import com.google.gson.Gson;
import com.itstyle.common.PageResponse;
import com.itstyle.common.WebSocketData;
import com.itstyle.domain.account.Account;
import com.itstyle.domain.car.manager.CarNumQueryVo;
import com.itstyle.domain.car.manager.CarNumVo;
import com.itstyle.domain.car.manager.enums.*;
import com.itstyle.domain.report.ChargeRecord;
import com.itstyle.handler.MyTextWebSocketHandler;
import com.itstyle.mapper.CarNumExtMapper;
import com.itstyle.mapper.CarNumMapper;
import com.itstyle.utils.BeanUtilIgnore;
import com.itstyle.utils.enums.Status;
import com.itstyle.utils.hibernate.BaseDaoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.criteria.OrderImpl;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CarNumService extends BaseDaoService<CarNumVo, Long> {
    @Resource
    private Gson gson;
    @Resource
    private CarNumMapper carNumMapper;
    @Resource
    private CarNumExtMapper carNumExtMapper;
    @Resource
    private FileResourceService fileResourceService;
    @Resource
    private ChargeRecordService chargeRecordService;

    @PostConstruct
    private void init() {
        jpaRepository = carNumMapper;
    }

    public int upload(MultipartFile file, CarNumVo carNumVo, CarNumExtVo carNumExtVo) {
        int status = Status.NORMAL;
        String uuid = UUID.randomUUID().toString();
        carNumExtVo.setUuid(uuid);
        List<CarNumVo> find = carNumMapper.findAll(Example.of(carNumVo.buildQueryVo()));
        CarNumVo saveVo = new CarNumVo();
        if (find != null && !find.isEmpty()) {
//            Optional<CarNumExtVo> any = find.stream().flatMap(e -> e.getCarNumExtVos().stream())
//                    .filter(e -> e.getCarNumType() == carNumExtVo.getCarNumType()).findAny();
//            if (any.isPresent()) {
//                return Status.WARN_ALREAD_EXIST;
//            }
            if (find.size() == 1) {
                saveVo = find.get(0);
            }
        }
        carNumVo.setCarNumExtVos(null);//为下一个copy属性准备
        BeanUtilIgnore.copyPropertiesIgnoreNull(carNumVo, saveVo);
        //现在可以重复上传了，所以如果上传的type相同，则把之前的删除了。
        Optional<CarNumExtVo> any = saveVo.getCarNumExtVos().stream().filter(e -> e.getCarNumType() == carNumExtVo.getCarNumType()).findAny();
        if (any.isPresent()) {
            CarNumExtVo getVo = any.get();
            saveVo.getCarNumExtVos().remove(getVo);
            carNumExtMapper.delete(getVo.getId());
        }
        saveVo.getCarNumExtVos().add(carNumExtVo);
        try {
            carNumMapper.save(saveVo);
            fileResourceService.upload(file, uuid);
        } catch (Exception e) {
            log.error("upload error",e);
            status = Status.ERROR;
        }
        return status;
    }

    public ResponseEntity<byte[]> download(CarNumVo carNumVo, CarNumExtVo carNumExtVo) {
        List<CarNumVo> all = carNumMapper.findAll(Example.of(carNumVo));
        if (all != null && !all.isEmpty()) {
            CarNumVo vo = all.get(0);
            String uuid = vo.getUuid(carNumExtVo.getCarNumType());
            return fileResourceService.downloadByUuid(uuid);
        }
        return null;
    }

    public ResponseEntity<byte[]> download(String path) {
        CarNumExtVo carNumExtVo = carNumExtMapper.findByPath(path);
        return fileResourceService.downloadByUuid(carNumExtVo.getUuid());
    }

    public void delete(String path) {
        CarNumExtVo carNumExtVo = carNumExtMapper.findByPath(path);
        carNumExtMapper.delete(carNumExtVo.getId());
        fileResourceService.deleteByUuid(carNumExtVo.getUuid());
    }

    public List<CarNumVo> query(CarNumVo carNumVo) {
        return carNumMapper.findAll(Example.of(carNumVo));
    }

    public Page<CarNumVo> query(CarNumQueryVo queryVo, String isEnter) {
        PageRequest pageRequest = PageResponse.getPageRequest(queryVo.getPage(), queryVo.getLimit());
        Specification<CarNumVo> sp = (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();
            if (StringUtils.isNotEmpty(queryVo.getCarNum())) {
                predicate.add(cb.like(root.get("carNum").as(String.class), "%" + queryVo.getCarNum() + "%"));
            }
            if (queryVo.getCarType() != null) {
                predicate.add(cb.equal(root.get("carType").as(Integer.class), queryVo.getCarType().ordinal()));
            }
            if (queryVo.getEnterPass() != null) {
                predicate.add(cb.equal(root.get("enterPass").as(String.class), queryVo.getEnterPass()));
            }
            if (queryVo.getStartTime() != null) {
                predicate.add(cb.ge(root.get("time").as(Long.class), queryVo.getStartTime()));
            }
            if (queryVo.getEndTime() != null) {
                predicate.add(cb.le(root.get("time").as(Long.class), queryVo.getEndTime()));
            }
            if (StringUtils.isNotEmpty(isEnter)) {
                predicate.add(cb.isNull(root.get("lTime").as(Long.class)));
            }
            if (StringUtils.isNotEmpty(queryVo.getLeavePass())) {
                Predicate p1 = cb.equal(root.get("leavePass").as(String.class), queryVo.getLeavePass());
                Predicate p2 = cb.equal(root.get("enterPass").as(String.class), queryVo.getLeavePass());
                predicate.add(cb.or(p1,p2));
            }
            if (queryVo.getLeaveStartTime() != null) {
                predicate.add(cb.ge(root.get("lTime").as(Long.class), queryVo.getLeaveStartTime()));
            }
            if (queryVo.getLeaveEndTime() != null) {
                predicate.add(cb.le(root.get("lTime").as(Long.class), queryVo.getLeaveEndTime()));
            }
            query.orderBy(cb.desc(root.get("time")));
            query.where(predicate.toArray(new Predicate[0]));
            return query.getRestriction();
        };
        return carNumMapper.findAll(sp, pageRequest);
    }

    public List<Object> statisticsAccess(Integer count) {
        return carNumMapper.statisticsAccess(count);
    }

    /**
     * 上传收费记录
     * @param id 需要确认的id
     */
    public String tempcarinfoPaymentConfirm(Long id, Account account) {
        String info = "放行成功";
        CarNumVo carNumVo = carNumMapper.getOne(id);
        if (carNumVo.getRecord() != null && carNumVo.getRecord()) {
            info = "收费放行失败 重复放行";
        }else{
            if (carNumVo.getLTime() == null) {
                info = "收费放行失败 车辆还未离场";
            }else{
                chargeRecord(carNumVo, account);
                carNumVo.setRecord(true);
                carNumMapper.save(carNumVo);
            }
        }
        log.info("info = {} id = {}", info, id);
        return info;
    }

    private void chargeRecord(CarNumVo carNumVo, Account account) {
        String username = "";
        if (account != null) {
            username = account.getUsername();
        }
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setCarNum(carNumVo.getCarNum());
        chargeRecord.setCarType(CarType.TEMP_CAR_A);
        chargeRecord.setCarRealType(carNumVo.getCarType());
        chargeRecord.setChargeType(ChargeType.CASH_PAYMENT);
        chargeRecord.setEnterTime(carNumVo.getTime());
        chargeRecord.setLeaveTime(carNumVo.getLTime());
        chargeRecord.setFee(carNumVo.getFee());
        chargeRecord.setChargePersonnel(username);
        chargeRecordService.upload(chargeRecord);
    }

    /**
     * 刪除场内车辆
     */
    public void deleteInner(Long[] ids) {
        for (Long id : ids) {
            CarNumVo carNumVo = findById(id);
            delete(id);
            WebSocketData data = new WebSocketData();
            data.setAction(WebSocketAction.CLEAR_PARKING_LOT);
            MyTextWebSocketHandler.sendMessageToUser(carNumVo.getEnterPass(), gson.toJson(data));
        }
    }
}
