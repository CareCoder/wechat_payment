package com.itstyle.service.impl;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.caryard.AccessType;
import com.itstyle.domain.caryard.ChannelType;
import com.itstyle.domain.caryard.ResponseAccessType;
import com.itstyle.exception.AssertUtil;
import com.itstyle.exception.BusinessException;
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
        return new PageResponse<>(all.getTotalElements(), convert(content, mapChannelType));
    }

    @Override
    public List<ResponseAccessType> listNoPage() {
        List<AccessType> all = accessTypeMapper.findAll();
        List<ChannelType> channelTypes = channelTypeMapper.findAll();
        Map<Long, String> mapChannelType = channelTypes.stream().collect(Collectors.toMap(ChannelType::getId, ChannelType::getName));
        return convert(all, mapChannelType);
    }

    @Override
    public void save(AccessType accessType) {
        List<AccessType> byChannelName = accessTypeMapper.findByChannelName(accessType.getChannelName());
        AssertUtil.assertTrue(byChannelName.size() == 0, () -> new BusinessException("通道名称已存在，请重新添加"));
        accessTypeMapper.save(accessType);
    }

    @Override
    public void edit(AccessType accessType) {
        List<AccessType> byChannelName = accessTypeMapper.findByChannelName(accessType.getChannelName());
        if (byChannelName.size() == 1 && !byChannelName.get(0).getId().equals(accessType.getId())) {
            throw new BusinessException("通道名称已存在，请重新修改");
        }
        if (byChannelName.size() > 1) {
            throw new BusinessException("通道名称已存在，请重新修改");
        }
        accessTypeMapper.save(accessType);
    }

    @Override
    public AccessType delete(Long id) {
        AccessType one = accessTypeMapper.getOne(id);
        accessTypeMapper.delete(id);
        return one;
    }

    @Override
    public AccessType getById(Long id) {
        return accessTypeMapper.findOne(id);
    }

    @Override
    public List<ChannelType> getAllChannelType() {
        return channelTypeMapper.findAll();
    }

    private List<ResponseAccessType> convert(List<AccessType> all, Map<Long, String> mapChannelType) {
        return all.stream().map(accessType -> {
            ResponseAccessType responseAccessType = new ResponseAccessType();
            BeanUtilIgnore.copyPropertiesIgnoreNull(accessType, responseAccessType);
            responseAccessType.setChannelTypeName(mapChannelType.get(accessType.getChannelTypeId()));
            return responseAccessType;
        }).collect(Collectors.toList());
    }
}
