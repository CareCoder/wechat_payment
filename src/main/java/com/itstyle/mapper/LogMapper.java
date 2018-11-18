package com.itstyle.mapper;

import com.itstyle.domain.log.SysLogger;
import com.itstyle.domain.log.SysLoggerResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LogMapper extends JpaRepository<SysLogger, Long>, JpaSpecificationExecutor<SysLogger> {

}
