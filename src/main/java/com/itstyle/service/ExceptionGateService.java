package com.itstyle.service;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.exceptiongate.ExceptionGate;
import com.itstyle.mapper.ExceptionGateMapper;
import com.itstyle.utils.enums.Status;
import com.itstyle.utils.hibernate.BaseDaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ExceptionGateService extends BaseDaoService<ExceptionGate, Long> {
    @Resource
    private ExceptionGateMapper exceptionGateMapper;
    @Resource
    private FileResourceService fileResourceService;

    @PostConstruct
    private void init() {
        jpaRepository = exceptionGateMapper;
    }

    public Page<ExceptionGate> query(Integer page, Integer limit, Long startTime, Long endTime){
        PageRequest pageRequest = PageResponse.getPageRequest(page, limit);
        Specification<ExceptionGate> sp = (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();
            if (startTime != null) {
                predicate.add(cb.ge(root.get("operTime").as(Long.class), startTime));
            }
            if (endTime != null) {
                predicate.add(cb.le(root.get("operTime").as(Long.class), endTime));
            }
            query.orderBy(cb.desc(root.get("id")));
            query.where(predicate.toArray(new Predicate[0]));
            return query.getRestriction();
        };
        return exceptionGateMapper.findAll(sp, pageRequest);
    }

    public int upload(MultipartFile file, ExceptionGate exceptionGate) {
        int status = Status.NORMAL;
        String uuid = UUID.randomUUID().toString();
        fileResourceService.upload(file, uuid);
        exceptionGate.setOperImgUuid(uuid);
        add(exceptionGate);
        return status;
    }
}
