package com.itstyle.service.impl;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.caryard.AccessType;
import com.itstyle.domain.caryard.ChannelType;
import com.itstyle.domain.caryard.ResponseAccessType;
import com.itstyle.mapper.AccessTypeMapper;
import com.itstyle.mapper.ChannelTypeMapper;
import com.itstyle.service.AccessTypeService;
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
public class AccessTypeServiceImpl implements AccessTypeService {

    private AccessTypeMapper accessTypeMapper;
    private ChannelTypeMapper channelTypeMapper;

    @Autowired
    public AccessTypeServiceImpl(AccessTypeMapper accessTypeMapper, ChannelTypeMapper channelTypeMapper) {
        this.accessTypeMapper = accessTypeMapper;
        this.channelTypeMapper = channelTypeMapper;
    }

    @Override
    public PageResponse<ResponseAccessType> list(int page, int limit) {
        Page<AccessType> all = accessTypeMapper.findAll(PageResponse.getPageRequest(page, limit));
        List<AccessType> content = all.getContent();
        List<ChannelType> channelTypes = channelTypeMapper.findAll();
        Map<Long, String> mapChannelType = channelTypes.stream().collect(Collectors.toMap(ChannelType::getId, ChannelType::getName));
        List<ResponseAccessType> collect = content.stream().map(accessType -> {
            ResponseAccessType responseAccessType = new ResponseAccessType();
            BeanUtilIgnore.copyPropertiesIgnoreNull(accessType, responseAccessType);
            responseAccessType.setChannelTypeName(mapChannelType.get(accessType.getChannelTypeId()));
            return responseAccessType;
        }).collect(Collectors.toList());
        return new PageResponse<>(all.getTotalElements(), collect);
    }

    @Override
    public void save(AccessType accessType) {
        accessTypeMapper.save(accessType);
    }

    @Override
    public void edit(AccessType accessType) {
        accessTypeMapper.save(accessType);
    }

    @Override
    public void delete(Long id) {
        accessTypeMapper.delete(id);
    }

    @Override
    public AccessType getById(Long id) {
        return accessTypeMapper.findOne(id);
    }

    @Override
    public List<ChannelType> getAllChannelType() {
        return channelTypeMapper.findAll();
    }
}
