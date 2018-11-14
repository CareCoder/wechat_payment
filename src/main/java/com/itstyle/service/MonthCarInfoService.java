package com.itstyle.service;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.mapper.MonthCarInfoMapper;
import com.itstyle.utils.hibernate.BaseDaoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;

@Service
public class MonthCarInfoService extends BaseDaoService<MonthCarInfo, Long>{
    @Resource
    private MonthCarInfoMapper monthCarInfoMapper;

    @PostConstruct
    private void init() {
        jpaRepository = monthCarInfoMapper;
    }

    public PageResponse<MonthCarInfo> queryLimit(int page, int limit, String queryStr) {
        PageRequest pageRequest = PageResponse.getPageRequest(page, limit);
        Specification<MonthCarInfo> sp = (root, query, cb) -> {
            if (StringUtils.isNotEmpty(queryStr)) {
                Predicate p1 = cb.equal(root.get("carNum").as(String.class), queryStr);
                Predicate p2 = cb.equal(root.get("phone").as(String.class), queryStr);
                query.where(cb.or(p1, p2));
            }
            return query.getRestriction();
        };
        Page<MonthCarInfo> all = monthCarInfoMapper.findAll(sp, pageRequest);
        return PageResponse.build(all);
    }

    public void payment(Long day, Long id) {
        MonthCarInfo one = findById(id);
        Long endTime = one.getEndTime();
        endTime = endTime + (day * 24 * 60 * 60 * 1000);
        one.setEndTime(endTime);
        update(one.getId(), one);
    }

    public void edit(MonthCarInfo monthCarInfo) {
        if (monthCarInfo.getId() == null) {
            //add
            monthCarInfo.setStartTime(System.currentTimeMillis());
            add(monthCarInfo);
        }else{
            //update 这个接口不得修改 startTime 和 endTime ，如果需要修改需要去续费接口
            monthCarInfo.setStartTime(null);
            monthCarInfo.setEndTime(null);
            update(monthCarInfo.getId(), monthCarInfo);
        }
    }
}
