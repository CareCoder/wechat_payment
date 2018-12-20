package com.itstyle.mapper;

import com.itstyle.domain.report.DeleteRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeleteRecordMapper extends JpaRepository<DeleteRecord, Long>, JpaSpecificationExecutor<DeleteRecord> {

}
