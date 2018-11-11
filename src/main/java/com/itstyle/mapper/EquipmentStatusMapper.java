package com.itstyle.mapper;

import com.itstyle.domain.caryard.EquipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentStatusMapper extends JpaRepository<EquipmentStatus, Long> {
}
