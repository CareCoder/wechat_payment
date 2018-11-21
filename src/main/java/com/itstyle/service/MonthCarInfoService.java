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
import java.util.ArrayList;
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
        update(one.getId(), one);
        addCarRenewInfo(one);
    }

    public void edit(MonthCarInfo monthCarInfo) {
        if (monthCarInfo.getId() == null) {
            //add
            monthCarInfo.setStartTime(System.currentTimeMillis());
            add(monthCarInfo);
            addCarInfo(monthCarInfo);
        }else{
            //update 这个接口不得修改 startTime 和 endTime ，如果需要修改需要去续费接口
            monthCarInfo.setStartTime(null);
            monthCarInfo.setEndTime(null);
            update(monthCarInfo.getId(), monthCarInfo);
        }
    }

    /**
     * 新增月租车后,写入redis
     * @param monthCarInfo 车辆信息
     */
    private void addCarInfo(MonthCarInfo monthCarInfo) {
        MonlyCarAddInfo carAddInfo = MonlyCarAddInfo.convert(monthCarInfo);
        String redisStr = redisDao.get(YstCommon.MONLY_CAR_ADD_INFO);
        List<MonlyCarAddInfo> mcis = gson.fromJson(redisStr, new TypeToken<List<MonlyCarAddInfo>>(){}.getType());
        if (mcis == null) {
            mcis = new ArrayList<>();
        }
        mcis.add(carAddInfo);
        redisDao.set(YstCommon.MONLY_CAR_ADD_INFO, gson.toJson(mcis));
    }

    /**
     * 月租车续费后,写入redis
     * @param monthCarInfo 车辆信息
     */
    private void addCarRenewInfo(MonthCarInfo monthCarInfo) {
        MonlyCarRenewInfo carRenewInfo = MonlyCarRenewInfo.convert(monthCarInfo);
        String redisStr = redisDao.get(YstCommon.MONLY_CAR_RENEW_INFO);
        List<MonlyCarRenewInfo> list = gson.fromJson(redisStr, new TypeToken<List<MonlyCarRenewInfo>>() {
        }.getType());
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(carRenewInfo);
        redisDao.set(YstCommon.MONLY_CAR_RENEW_INFO, gson.toJson(list));
    }

    public List<MonthCarInfo> list() {
        return monthCarInfoMapper.findAll();
    }
}
