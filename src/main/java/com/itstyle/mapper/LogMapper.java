package com.itstyle.mapper;

import com.itstyle.domain.log.SysLogger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogMapper extends JpaRepository<SysLogger, Long> {

}
