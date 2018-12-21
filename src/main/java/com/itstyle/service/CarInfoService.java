package com.itstyle.service;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.car.manager.CarInfo;
import com.itstyle.domain.car.manager.enums.CarType2;
import com.itstyle.domain.report.DeleteRecord;
import com.itstyle.mapper.CarInfoMapper;
import com.itstyle.utils.BeanUtilIgnore;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CarInfoService {
    @Resource
    private CarInfoMapper carInfoMapper;

    @Resource
    private DeleteRecordService deleteRecordService;

    public PageResponse<CarInfo> list(int page, int limit, String type, String queryStr) {
        PageRequest pageRequest = PageResponse.getPageRequest(page, limit);
        Specification<CarInfo> sp = (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();
            if (StringUtils.isNotEmpty(queryStr)) {
                Predicate p1 = cb.equal(root.get("carNum").as(String.class), queryStr);
                Predicate p2 = cb.equal(root.get("phone").as(String.class), queryStr);
                predicate.add(cb.or(p1, p2));
            }
            if (type.equals("free")) {
                Predicate p = cb.equal(root.get("isFree").as(Boolean.class), true);
                predicate.add(p);
            }else if (type.equals("blacklist")){
                Predicate p = cb.equal(root.get("isBlackList").as(Boolean.class), true);
                predicate.add(p);
            }
            query.where(predicate.toArray(new Predicate[0]));
            return query.getRestriction();
        };
        Page<CarInfo> all = carInfoMapper.findAll(sp, pageRequest);
        return PageResponse.build(all);
    }

    public List<CarInfo> createTimeQuery(Long startTime, Long endTime) {
        Assert.notNull(startTime, "startTime is null");
        Assert.notNull(endTime, "endTime is null");
        Specification<CarInfo> sp = (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();
            Predicate p1 = cb.between(root.get("createTime").as(Date.class), new Date(startTime), new Date(endTime));
            predicate.add(p1);
            query.orderBy(cb.desc(root.get("id")));
            query.where(predicate.toArray(new Predicate[0]));
            return query.getRestriction();
        };
        return carInfoMapper.findAll(sp);
    }

    public CarInfo getById(Long id) {
        return carInfoMapper.findOne(id);
    }

    public CarInfo getByCarNum(String carNum) {
        return carInfoMapper.findByCarNum(carNum);
    }

    public void save(CarInfo carInfo) {
        carInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        carInfoMapper.save(carInfo);
    }

    public void delete(Long id) {
        CarInfo one = carInfoMapper.getOne(id);

        carInfoMapper.delete(id);
        CarType2 carType2;
        if (one.getIsBlackList() != null && one.getIsBlackList()) {
            carType2 = CarType2.BLACK_LIST_CAR;
        }else{
            carType2 = CarType2.FREE_CAR;
        }
        deleteRecordService.upload(one.getCarNum(), carType2);
    }

    public void update(CarInfo carInfo) {
        Assert.notNull(carInfo.getId(), "id is null");
        CarInfo one = carInfoMapper.findOne(carInfo.getId());
        BeanUtilIgnore.copyPropertiesIgnoreNull(carInfo, one);
        one.setModifyTime(new Timestamp(System.currentTimeMillis()));
        carInfoMapper.save(one);
    }

    public List<CarInfo> getBlackList() {
        return carInfoMapper.findByIsBlackListIs(true);
    }

    public List<CarInfo> getFree() {
        return carInfoMapper.findByIsFreeIs(true);
    }
}
