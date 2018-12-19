package com.itstyle.service;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.car.manager.enums.ChargeType;
import com.itstyle.domain.report.ChargeRecord;
import com.itstyle.mapper.ChargeRecordMapper;
import com.itstyle.utils.hibernate.BaseDaoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ChargeRecordService extends BaseDaoService<ChargeRecord, Long> {
    @Resource
    private ChargeRecordMapper chargeRecordMapper;

    @PostConstruct
    private void init() {
        this.jpaRepository = chargeRecordMapper;
    }

    public void upload(ChargeRecord chargeRecord) {
        chargeRecord.setTime(System.currentTimeMillis());
        add(chargeRecord);
    }

    public PageResponse<ChargeRecord> query(int page, int limit, ChargeType chargeType, CarType carType,
                                            String chargePersonnel, Long startTime, Long endTime) {
        PageRequest pageRequest = PageResponse.getPageRequest(page, limit);
        Specification<ChargeRecord> sp = (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();
            if (chargeType != null) {
                Predicate p1 = cb.equal(root.get("chargeType").as(Integer.class), chargeType.ordinal());
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
            query.orderBy(cb.desc(root.get("id")));
            query.where(predicate.toArray(new Predicate[0]));
            return query.getRestriction();
        };
        Page<ChargeRecord> all = chargeRecordMapper.findAll(sp, pageRequest);
        return PageResponse.build(all);
    }

    public List<Object> statisticsTemp(Integer carType, Integer count) {
        return chargeRecordMapper.statisticsTemp(carType, count);
    }
}
