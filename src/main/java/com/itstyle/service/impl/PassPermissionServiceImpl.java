package com.itstyle.service.impl;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.caryard.AccessType;
import com.itstyle.domain.caryard.ChannelType;
import com.itstyle.domain.caryard.PassCarStatus;
import com.itstyle.domain.caryard.ResponsePassCarStatus;
import com.itstyle.exception.BusinessException;
import com.itstyle.mapper.AccessTypeMapper;
import com.itstyle.mapper.ChannelTypeMapper;
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
    private ChannelTypeMapper channelTypeMapper;
    private AccessTypeMapper accessTypeMapper;

    @Autowired
    public PassPermissionServiceImpl(PassPermissionMapper passPermissionMapper,
                                     ChannelTypeMapper channelTypeMapper,
                                     AccessTypeMapper accessTypeMapper) {
        this.passPermissionMapper = passPermissionMapper;
        this.channelTypeMapper = channelTypeMapper;
        this.accessTypeMapper = accessTypeMapper;
    }

    @Override
    public PageResponse<ResponsePassCarStatus> list(int page, int limit) {
        Page<PassCarStatus> all = passPermissionMapper.findAll(PageResponse.getPageRequest(page, limit));
        List<PassCarStatus> content = all.getContent();
        List<ChannelType> channelTypes = channelTypeMapper.findAll();
        Map<Long, String> mapChannelType = channelTypes.stream().collect(Collectors.toMap(ChannelType::getId, ChannelType::getName));
        List<AccessType> accessAll = accessTypeMapper.findAll();
        Map<Long, AccessType> accessTypeMap = accessAll.stream().collect(Collectors.toMap(AccessType::getId, e -> e));
        List<ResponsePassCarStatus> collect = content.stream().map(passCarStatus -> {
            ResponsePassCarStatus responsePassCarStatus = new ResponsePassCarStatus();
            BeanUtilIgnore.copyPropertiesIgnoreNull(passCarStatus, responsePassCarStatus);
            responsePassCarStatus.setChannelName(accessTypeMap.get(passCarStatus.getAccessTypeId()).getChannelName());
            responsePassCarStatus.setChannelTypeName(mapChannelType.get(accessTypeMap.get(passCarStatus.getAccessTypeId()).getChannelTypeId()));
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
    public AccessType getAccessTypeId(Long id) {
        return accessTypeMapper.findOne(id);
    }


}
