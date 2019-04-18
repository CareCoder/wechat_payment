package com.itstyle.mapper;

import com.itstyle.domain.caryard.PassCarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PassPermissionMapper extends JpaRepository<PassCarStatus, Long> {

    @Query(value = "select * from pass_car_status a where a.access_type_id = ?1", nativeQuery = true)
    List<PassCarStatus> findByAccessTypeId(Long id);

    @Transactional
    void deleteByAccessTypeId(Long accessTypeId);
}
