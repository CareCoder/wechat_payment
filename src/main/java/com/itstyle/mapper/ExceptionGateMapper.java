package com.itstyle.mapper;

import com.itstyle.domain.exceptiongate.ExceptionGate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExceptionGateMapper extends JpaRepository<ExceptionGate, Long>, JpaSpecificationExecutor<ExceptionGate> {
}
