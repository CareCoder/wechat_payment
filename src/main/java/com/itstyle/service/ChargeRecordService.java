package com.itstyle.service;

import com.itstyle.domain.report.ChargeRecord;
import com.itstyle.mapper.ChargeRecordMapper;
import com.itstyle.utils.hibernate.BaseDaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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
        add(chargeRecord);
    }
}
