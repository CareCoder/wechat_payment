package com.itstyle.mapper;

import com.itstyle.domain.account.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleMapper extends JpaRepository<Role, Long> {
}
