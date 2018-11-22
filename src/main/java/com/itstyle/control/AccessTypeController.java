package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.caryard.AccessType;
import com.itstyle.domain.caryard.ChannelType;
import com.itstyle.domain.caryard.ResponseAccessType;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.exception.AssertUtil;
import com.itstyle.exception.BusinessException;
import com.itstyle.service.AccessTypeService;
import com.itstyle.utils.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/type")
public class AccessTypeController {

    private AccessTypeService accessTypeService;

    @Autowired
    public AccessTypeController(AccessTypeService accessTypeService) {
        this.accessTypeService = accessTypeService;
    }

    @GetMapping("/list")
    @ResponseBody
    public PageResponse<ResponseAccessType> list(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                 @RequestParam(value = "limit", required = false, defaultValue = "20") int limit) {
        return accessTypeService.list(page, limit);
    }

    @PostMapping("/save")
    @ResponseBody
    public Response save(AccessType accessType) {
        AssertUtil.assertNotNull(accessType, () -> new BusinessException("access type is null"));
        AssertUtil.assertNotNull(accessType.getChannelName(), () -> new BusinessException("通道名称不能为空"));
        AssertUtil.assertNotNull(accessType.getChannelTypeId(), () -> new BusinessException("通道类型不能为空"));
        AssertUtil.assertNotNull(accessType.getIp(), () -> new BusinessException("IP地址不能为空"));
        accessTypeService.save(accessType);
        return Response.build(Status.NORMAL, null, null);
    }

    @PostMapping("/edit")
    @ResponseBody
    public Response edit(AccessType accessType) {
        AssertUtil.assertNotNull(accessType, () -> new BusinessException("access type is null"));
        AssertUtil.assertNotNull(accessType.getChannelName(), () -> new BusinessException("通道名称不能为空"));
        AssertUtil.assertNotNull(accessType.getChannelTypeId(), () -> new BusinessException("通道类型不能为空"));
        AssertUtil.assertNotNull(accessType.getIp(), () -> new BusinessException("IP地址不能为空"));
        accessTypeService.edit(accessType);
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public Response delete(@PathVariable("id") Long id) {
        AssertUtil.assertNotNull(id, () -> new BusinessException("删除的id不能为空"));
        accessTypeService.delete(id);
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/all/channel")
    @ResponseBody
    public PageResponse<ChannelType> getAll() {
        List<ChannelType> allChannelType = accessTypeService.getAllChannelType();
        return new PageResponse<>(0L, allChannelType);
    }

    @GetMapping("/info")
    public String info() {
        return "/backend/access-type-info";
    }

    @GetMapping("/access-type-add.html")
    public String accessTypeAddUI() {
        return "/backend/access-type-add";
    }

    @GetMapping("/access-type-edit.html")
    public String accessTypeEditUI(Long id, Model model) {
        AccessType accessType = accessTypeService.getById(id);
        model.addAttribute("access_type_info", accessType);
        return "/backend/access-type-edit";
    }

    @GetMapping("/getAll")
    @ResponseBody
    public PageResponse<ResponseAccessType> getAllAccessType() {
        return new PageResponse<>(0L, accessTypeService.listNoPage());
    }

}
