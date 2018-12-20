package com.itstyle.service;

import com.itstyle.domain.car.manager.enums.CarType2;
import com.itstyle.domain.report.DeleteRecord;
import com.itstyle.mapper.DeleteRecordMapper;
import com.itstyle.utils.hibernate.BaseDaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DeleteRecordService extends BaseDaoService<DeleteRecord, Long> {
    @Resource
    private DeleteRecordMapper deleteRecordMapper;

    @PostConstruct
    private void init() {
        this.jpaRepository = deleteRecordMapper;
    }

    public void upload(String carNum, CarType2 carType2) {
        DeleteRecord deleteRecord = new DeleteRecord();
        deleteRecord.setCarNum(carNum);
        deleteRecord.setCarType2(carType2);
        deleteRecord.setDeleteTime(System.currentTimeMillis());
        add(deleteRecord);
    }

    /**
     * 根据段查询出这个时间段删除的车辆
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    public List<DeleteRecord> query(Long startTime, Long endTime) {
        Assert.notNull(startTime, "startTime is null");
        Assert.notNull(endTime, "endTime is null");
        Specification<DeleteRecord> sp = (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();
            Predicate p1 = cb.between(root.get("deleteTime").as(Long.class), startTime, endTime);
            predicate.add(p1);
            query.orderBy(cb.desc(root.get("id")));
            query.where(predicate.toArray(new Predicate[0]));
            return query.getRestriction();
        };
        return deleteRecordMapper.findAll(sp);
    }
}
