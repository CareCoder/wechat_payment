package com.itstyle.service;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.account.Account;
import com.itstyle.domain.car.manager.CarNumQueryVo;
import com.itstyle.domain.car.manager.CarNumVo;
import com.itstyle.domain.car.manager.enums.CarNumExtVo;
import com.itstyle.domain.car.manager.enums.CarNumType;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.car.manager.enums.ChargeType;
import com.itstyle.domain.report.ChargeRecord;
import com.itstyle.mapper.CarNumExtMapper;
import com.itstyle.mapper.CarNumMapper;
import com.itstyle.utils.BeanUtilIgnore;
import com.itstyle.utils.enums.Status;
import com.itstyle.utils.hibernate.BaseDaoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CarNumService extends BaseDaoService<CarNumVo, Long> {
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

    public int upload(MultipartFile file, CarNumVo carNumVo, CarNumExtVo carNumExtVo, Account account) {
        int status = Status.NORMAL;
        String uuid = UUID.randomUUID().toString();
        carNumExtVo.setUuid(uuid);
        List<CarNumVo> find = carNumMapper.findAll(Example.of(carNumVo.buildQueryVo()));
        CarNumVo saveVo = new CarNumVo();
        if (find != null && !find.isEmpty()) {
            Optional<CarNumExtVo> any = find.stream().flatMap(e -> e.getCarNumExtVos().stream())
                    .filter(e -> e.getCarNumType() == carNumExtVo.getCarNumType()).findAny();
            if (any.isPresent()) {
                return Status.WARN_ALREAD_EXIST;
            }
            if (find.size() == 1) {
                saveVo = find.get(0);
            }
        }
        carNumVo.setCarNumExtVos(null);//为下一个copy属性准备
        BeanUtilIgnore.copyPropertiesIgnoreNull(carNumVo, saveVo);
        saveVo.getCarNumExtVos().add(carNumExtVo);
        try {
            carNumMapper.save(saveVo);
            fileResourceService.upload(file, uuid);
            //上传收费记录
            chargeRecord(saveVo, carNumExtVo, account);
        } catch (Exception e) {
            status = Status.WARN_ALREAD_EXIST;
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

    public Page<CarNumVo> query(CarNumQueryVo queryVo) {
        PageRequest pageRequest = PageResponse.getPageRequest(queryVo.getPage(), queryVo.getLimit());
        Specification<CarNumVo> sp = (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();
            if (StringUtils.isNotEmpty(queryVo.getCarNum())) {
                predicate.add(cb.like(root.get("carNum").as(String.class), "%" + queryVo.getCarNum() + "%"));
            }
            if (queryVo.getCarType() != null) {
                predicate.add(cb.equal(root.get("carType").as(Integer.class), queryVo.getCarType().ordinal()));
            }
            if (queryVo.getStartTime() != null && queryVo.getEndTime() != null) {
                Predicate p1 = cb.between(root.get("time").as(Long.class), queryVo.getStartTime(), queryVo.getEndTime());
                Predicate p2 = cb.isNull(root.get("time").as(Long.class));
                predicate.add(cb.and(p1,p2));
            }
            if (StringUtils.isNotEmpty(queryVo.getLeavePass())) {
                Predicate p1 = cb.equal(root.get("leavePass").as(String.class), queryVo.getLeavePass());
                Predicate p2 = cb.equal(root.get("enterPass").as(String.class), queryVo.getLeavePass());
                predicate.add(cb.or(p1,p2));
            }
            if (queryVo.getLeaveEndTime() != null && queryVo.getLeaveStartTime() != null) {
                predicate.add(cb.between(root.get("lTime").as(Long.class), queryVo.getLeaveStartTime(), queryVo.getLeaveEndTime()));
            }
            query.where(predicate.toArray(new Predicate[0]));
            return query.getRestriction();
        };
        return carNumMapper.findAll(sp, pageRequest);
    }

    private void chargeRecord(CarNumVo carNumVo, CarNumExtVo carNumExtVo,Account account) {
        if (carNumExtVo.getCarNumType() != CarNumType.LEAVE_BIG) {
            return;
        }
        String username = "";
        if (account != null) {
            username = account.getUsername();
        }
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setCarNum(carNumVo.getCarNum());
        chargeRecord.setCarType(CarType.TEMP_CAR_A);
        chargeRecord.setChargeType(ChargeType.CASH_PAYMENT);
        chargeRecord.setEnterTime(carNumVo.getTime());
        chargeRecord.setLeaveTime(carNumExtVo.getTime());
        chargeRecord.setFee(carNumVo.getFee());
        chargeRecord.setChargePersonnel(username);
        chargeRecordService.upload(chargeRecord);
    }

    public List<Object> statisticsAccess(Integer count) {
        return carNumMapper.statisticsAccess(count);
    }
}
