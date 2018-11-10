package com.itstyle.mapper;

import com.itstyle.domain.account.Account;
import com.itstyle.domain.caryard.PassCarStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassPermissionMapper extends JpaRepository<PassCarStatus, Long> {
}
