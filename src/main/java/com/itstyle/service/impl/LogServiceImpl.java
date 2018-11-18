package com.itstyle.service.impl;

import com.itstyle.common.PageResponse;
import com.itstyle.common.SystemLogQueue;
import com.itstyle.domain.account.Role;
import com.itstyle.domain.log.SysLogger;
import com.itstyle.domain.log.SysLoggerResponse;
import com.itstyle.mapper.LogMapper;
import com.itstyle.mapper.RoleMapper;
import com.itstyle.service.LogService;
import com.itstyle.utils.BeanUtilIgnore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LogServiceImpl implements LogService {

    private LogMapper logMapper;
    private RoleMapper roleMapper;

    @Autowired
    public LogServiceImpl(LogMapper logMapper, RoleMapper roleMapper) {
        this.logMapper = logMapper;
        this.roleMapper = roleMapper;
    }


    @Override
    public Integer doSystemLogSave() {
        List<SysLogger> list = new ArrayList<>();
        Queue<SysLogger> queue = SystemLogQueue.SYSTEM_LOG_QUEUE;
//        while (!queue.isEmpty()) {
//            SysLogger log = queue.poll();
//            if (log == null) {
//                continue;
//            }
//            list.add(log);
//        }
        for (int i = 0; i < 50; i++) {
            SysLogger log = queue.poll();
            if (log == null) {
                continue;
            }
            list.add(log);
        }
        if (list.size() == 0) {
            return 0;
        }
        List<SysLogger> loggers = logMapper.save(list);
        return loggers.size();
    }

    @Override
    public PageResponse<SysLoggerResponse> list(int page, int limit, Date start, Date end, Long roleId) {
        PageRequest pageRequest = PageResponse.getPageRequest(page, limit);
        Specification<SysLogger> sp = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Path<Date> logDate = root.get("logDate");
            Path<Long> id = root.get("roleId");
            Predicate between = cb.between(logDate, start, end);
            predicates.add(between);
            Predicate equal = cb.equal(id, roleId);
            predicates.add(equal);
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        };
        Page<SysLogger> all = logMapper.findAll(sp, pageRequest);
        List<SysLogger> content = all.getContent();
        List<Role> roles = roleMapper.findAll();
        Map<Long, String> mapRole = roles.stream().collect(Collectors.toMap(Role::getId, Role::getName));
        List<SysLoggerResponse> collect = content.stream().map(sysLogger -> {
            SysLoggerResponse sysLoggerResponse = new SysLoggerResponse();
            BeanUtilIgnore.copyPropertiesIgnoreNull(sysLogger, sysLoggerResponse);
            sysLoggerResponse.setRoleName(mapRole.get(sysLoggerResponse.getRoleId()));
            return sysLoggerResponse;
        }).collect(Collectors.toList());
        return new PageResponse<>(all.getTotalElements(), collect);
    }
}
