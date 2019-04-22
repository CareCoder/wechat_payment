package com.itstyle.service.impl;

import com.itstyle.common.PageResponse;
import com.itstyle.common.WebSocketData;
import com.itstyle.domain.car.manager.enums.WebSocketAction;
import com.itstyle.domain.caryard.AccessType;
import com.itstyle.domain.caryard.ChannelType;
import com.itstyle.domain.caryard.PassCarStatus;
import com.itstyle.domain.caryard.ResponsePassCarStatus;
import com.itstyle.exception.AssertUtil;
import com.itstyle.exception.BusinessException;
import com.itstyle.handler.MyTextWebSocketHandler;
import com.itstyle.mapper.AccessTypeMapper;
import com.itstyle.mapper.ChannelTypeMapper;
import com.itstyle.mapper.PassPermissionMapper;
import com.itstyle.service.ExternalInterfaceService;
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

    @Autowired
    private PassPermissionMapper passPermissionMapper;
    @Autowired
    private ChannelTypeMapper channelTypeMapper;
    @Autowired
    private AccessTypeMapper accessTypeMapper;
    @Autowired
    private ExternalInterfaceService externalInterfaceService;

    @Override
    public PageResponse<ResponsePassCarStatus> list(int page, int limit) {
        Page<PassCarStatus> all = passPermissionMapper.findAll(PageResponse.getPageRequest(page, limit));
        List<PassCarStatus> content = all.getContent();
        return new PageResponse<>(all.getTotalElements(), convert(content));
    }

    @Override
    public List<ResponsePassCarStatus> list() {
        List<PassCarStatus> all = passPermissionMapper.findAll();
        return convert(all);
    }

    @Override
    public void save(PassCarStatus passCarStatus) {
        List<PassCarStatus> byAccessTypeId = passPermissionMapper.findByAccessTypeId(passCarStatus.getAccessTypeId());
        AssertUtil.assertTrue(byAccessTypeId.size() == 0, () -> new BusinessException("通道名称已存在，请重新选择"));
        PassCarStatus save = passPermissionMapper.save(passCarStatus);
        if (save == null) {
            throw new BusinessException("修改失败，请稍后重试");
        }
        notifyClient();
    }

    @Override
    public void update(PassCarStatus passCarStatus) {
        List<PassCarStatus> byAccessTypeId = passPermissionMapper.findByAccessTypeId(passCarStatus.getAccessTypeId());
        if (byAccessTypeId.size() == 1 && !byAccessTypeId.get(0).getId().equals(passCarStatus.getId())) {
            throw new BusinessException("通道名称已存在，请重新修改");
        }
        if (byAccessTypeId.size() > 1) {
            throw new BusinessException("通道名称已存在，请重新选择");
        }
        PassCarStatus save = passPermissionMapper.save(passCarStatus);
        if (save == null) {
            throw new BusinessException("修改失败，请稍后重试");
        }
        notifyClient();
    }

    @Override
    public void delete(Long id) {
        passPermissionMapper.delete(id);
        notifyClient();
    }

    @Override
    public PassCarStatus getById(Long id) {
        return passPermissionMapper.findOne(id);
    }

    @Override
    public AccessType getAccessTypeId(Long id) {
        return accessTypeMapper.findOne(id);
    }

    private List<ResponsePassCarStatus> convert(List<PassCarStatus> all) {
        List<ChannelType> channelTypes = channelTypeMapper.findAll();
        Map<Long, String> mapChannelType = channelTypes.stream().collect(Collectors.toMap(ChannelType::getId, ChannelType::getName));
        List<AccessType> accessAll = accessTypeMapper.findAll();
        Map<Long, AccessType> accessTypeMap = accessAll.stream().collect(Collectors.toMap(AccessType::getId, e -> e));
        return all.stream().map(passCarStatus -> {
            ResponsePassCarStatus responsePassCarStatus = new ResponsePassCarStatus();
            BeanUtilIgnore.copyPropertiesIgnoreNull(passCarStatus, responsePassCarStatus);
            responsePassCarStatus.setChannelName(accessTypeMap.get(passCarStatus.getAccessTypeId()).getChannelName());
            responsePassCarStatus.setChannelTypeName(mapChannelType.get(accessTypeMap.get(passCarStatus.getAccessTypeId()).getChannelTypeId()));
            responsePassCarStatus.setIp(accessTypeMap.get(passCarStatus.getAccessTypeId()).getIp());
            responsePassCarStatus.setCamera(accessTypeMap.get(passCarStatus.getAccessTypeId()).getCamera());
            return responsePassCarStatus;
        }).collect(Collectors.toList());
    }

    @Override
    public void generateDefault(Long accessTypeId) {
        PassCarStatus passCarStatus = new PassCarStatus();
        passCarStatus.setAccessTypeId(accessTypeId);
        passCarStatus.setBlackCar(1);
        passCarStatus.setBlueCar(1);
        passCarStatus.setGreenCar(1);
        passCarStatus.setYellowCar(1);
        passCarStatus.setMonlyCar_A(1);
        passCarStatus.setMonlyCar_B(1);
        passCarStatus.setMonlyCar_C(1);
        passCarStatus.setSpecialCar(1);
        save(passCarStatus);
    }

    @Override
    public void deleteByAccessTypeId(Long accessTypeId) {
        passPermissionMapper.deleteByAccessTypeId(accessTypeId);
    }

    private void notifyClient() {
        MyTextWebSocketHandler.sendDataToAllUser(externalInterfaceService.getAccessAuthoritySetup(),WebSocketAction.UPDATE_ACCESS_AUTHORITY);
    }
}
