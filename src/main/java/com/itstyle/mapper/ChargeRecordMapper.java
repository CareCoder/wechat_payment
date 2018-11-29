package com.itstyle.mapper;

import com.itstyle.domain.report.ChargeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChargeRecordMapper extends JpaRepository<ChargeRecord, Long>, JpaSpecificationExecutor<ChargeRecord> {
}
