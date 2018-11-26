package com.itstyle.mapper;

import com.itstyle.domain.caryard.AccessType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessTypeMapper extends JpaRepository<AccessType, Long> {

    List<AccessType> findByChannelName(String channelName);
}
