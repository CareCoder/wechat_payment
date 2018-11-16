package com.itstyle.control;

import com.google.gson.Gson;
import com.itstyle.common.PageResponse;
import com.itstyle.common.YstCommon;
import com.itstyle.dao.RedisDao;
import com.itstyle.domain.caryard.EquipmentStatus;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.exception.AssertUtil;
import com.itstyle.exception.BusinessException;
import com.itstyle.utils.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/equipment")
public class EquipmentStatusController {

    private RedisDao redisDao;
    private Gson gson;

    @Autowired
    public EquipmentStatusController(RedisDao redisDao, Gson gson) {
        this.redisDao = redisDao;
        this.gson = gson;
    }

    @GetMapping("/page")
    public String equipmentPage() {
        return "/backend/equipment-info";
    }

    @GetMapping("/query")
    @ResponseBody
    public PageResponse<EquipmentStatus> equipmentList() {
        Map<Object, Object> objectObjectMap = redisDao.hgetAll(YstCommon.EQUIPMENT_STATUS);
        List<EquipmentStatus> collect = objectObjectMap.values().stream().map(o -> gson.fromJson(o.toString(), EquipmentStatus.class)).collect(Collectors.toList());
        return new PageResponse<>(0L, collect);
    }

    @PostMapping("/save")
    @ResponseBody
    public Response save(EquipmentStatus equipmentStatus) {
        AssertUtil.assertNotNull(equipmentStatus, () -> new BusinessException("equipment status is null"));
        AssertUtil.assertNotEmpty(equipmentStatus.getKey(), () -> new BusinessException("key is null"));
        redisDao.hset(YstCommon.EQUIPMENT_STATUS, equipmentStatus.getKey(), gson.toJson(equipmentStatus));
        return Response.build(Status.NORMAL, null, null);
    }

}
