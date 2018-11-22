package com.itstyle.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itstyle.common.PageResponse;
import com.itstyle.common.YstCommon;
import com.itstyle.dao.RedisDao;
import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.mapper.MonthCarInfoMapper;
import com.itstyle.utils.hibernate.BaseDaoService;
import com.itstyle.vo.incrementmonly.response.MonlyCarAddInfo;
import com.itstyle.vo.incrementmonly.response.MonlyCarRenewInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MonthCarInfoService extends BaseDaoService<MonthCarInfo, Long>{
    @Resource
    private MonthCarInfoMapper monthCarInfoMapper;
    @Resource
    private RedisDao redisDao;
    @Resource
    private Gson gson;

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
        one.setModifyTime(new Date());
        update(one.getId(), one);
    }

    public void edit(MonthCarInfo monthCarInfo) {
        if (monthCarInfo.getId() == null) {
            //add
            if (monthCarInfo.getStartTime() == null) {
                monthCarInfo.setStartTime(System.currentTimeMillis());
            }
            monthCarInfo.setCreateTime(new Date());
            add(monthCarInfo);
        }else{
            //update 这个接口不得修改 startTime 和 endTime ，如果需要修改需要去续费接口
            monthCarInfo.setStartTime(null);
            monthCarInfo.setEndTime(null);
            update(monthCarInfo.getId(), monthCarInfo);
        }
    }

    /**
     * 查找区域时间内新增的车
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    public List<MonthCarInfo> getCarAddInfo(Long startTime, Long endTime) {
        Specification<MonthCarInfo> sp = (root, query, cb) -> {
            if (startTime != null && endTime != null) {
                Predicate p1 = cb.ge(root.get("createTime").as(Long.class), startTime);
                Predicate p2 = cb.le(root.get("createTime").as(Long.class), endTime);
                query.where(cb.and(p1, p2));
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
                Predicate p1 = cb.ge(root.get("modifyTime").as(Long.class), startTime);
                Predicate p2 = cb.le(root.get("modifyTime").as(Long.class), endTime);
                query.where(cb.and(p1, p2));
            }
            return query.getRestriction();
        };
        return monthCarInfoMapper.findAll(sp);
    }


    public List<MonthCarInfo> list() {
        return monthCarInfoMapper.findAll();
    }
}
