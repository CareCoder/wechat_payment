package com.itstyle.service.impl;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.caryard.ChannelType;
import com.itstyle.domain.caryard.EquipmentStatus;
import com.itstyle.domain.caryard.PassCarStatus;
import com.itstyle.domain.caryard.ResponsePassCarStatus;
import com.itstyle.exception.BusinessException;
import com.itstyle.mapper.ChannelTypeMapper;
import com.itstyle.mapper.EquipmentStatusMapper;
import com.itstyle.mapper.PassPermissionMapper;
import com.itstyle.service.PassPermissionService;
import com.itstyle.utils.BeanUtilIgnore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PassPermissionServiceImpl implements PassPermissionService {

    private PassPermissionMapper passPermissionMapper;
    private EquipmentStatusMapper equipmentStatusMapper;
    private ChannelTypeMapper channelTypeMapper;

    @Autowired
    public PassPermissionServiceImpl(PassPermissionMapper passPermissionMapper,
                                     EquipmentStatusMapper equipmentStatusMapper,
                                     ChannelTypeMapper channelTypeMapper) {
        this.passPermissionMapper = passPermissionMapper;
        this.equipmentStatusMapper = equipmentStatusMapper;
        this.channelTypeMapper = channelTypeMapper;
    }

    @Override
    public PageResponse<ResponsePassCarStatus> list(int page, int limit) {
        Page<PassCarStatus> all = passPermissionMapper.findAll(PageResponse.getPageRequest(page, limit));
        List<PassCarStatus> content = all.getContent();
        List<ChannelType> channelTypes = channelTypeMapper.findAll();
        Map<Long, String> mapChannelType = channelTypes.stream().collect(Collectors.toMap(ChannelType::getId, ChannelType::getName));
        List<ResponsePassCarStatus> collect = content.stream().map(passCarStatus -> {
            ResponsePassCarStatus responsePassCarStatus = new ResponsePassCarStatus();
            BeanUtilIgnore.copyPropertiesIgnoreNull(passCarStatus, responsePassCarStatus);
            responsePassCarStatus.setChannelTypeName(mapChannelType.get(passCarStatus.getChannelTypeId()));
            return responsePassCarStatus;
        }).collect(Collectors.toList());
        return new PageResponse<>(all.getTotalElements(), collect);
    }

    @Override
    public void save(PassCarStatus passCarStatus) {
        PassCarStatus save = passPermissionMapper.save(passCarStatus);
        if (save == null) {
            throw new BusinessException("修改失败，请稍后重试");
        }
    }

    @Override
    public void update(PassCarStatus passCarStatus) {
        PassCarStatus save = passPermissionMapper.save(passCarStatus);
        if (save == null) {
            throw new BusinessException("修改失败，请稍后重试");
        }
    }

    @Override
    public void delete(Long id) {
        passPermissionMapper.delete(id);
    }

    @Override
    public PassCarStatus getById(Long id) {
        return passPermissionMapper.findOne(id);
    }

    @Override
    public PageResponse<EquipmentStatus> equipmentList(int page, int limit) {
        return PageResponse.build(equipmentStatusMapper.findAll(PageResponse.getPageRequest(page, limit)));
    }
}
