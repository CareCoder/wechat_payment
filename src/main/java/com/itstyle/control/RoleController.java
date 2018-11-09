package com.itstyle.control;

import com.google.gson.Gson;
import com.itstyle.common.PageResponse;
import com.itstyle.domain.account.Role;
import com.itstyle.domain.account.req.DeleteRoleIds;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.exception.AssertUtil;
import com.itstyle.exception.BusinessException;
import com.itstyle.service.RoleService;
import com.itstyle.utils.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController {

    private RoleService roleService;
    private Gson gson;

    @Autowired
    public RoleController(RoleService roleService, Gson gson) {
        this.roleService = roleService;
        this.gson = gson;
    }

    @PostMapping("/save")
    @ResponseBody
    public String save(Role role) {
        AssertUtil.assertNotNull(role, () -> new BusinessException("角色类型不能为空"));
        AssertUtil.assertNotEmpty(role.getName(), () -> new BusinessException("角色类型名称不能为空"));
        Role insert = roleService.insert(role);
        Response response = Response.build(Status.NORMAL, null, gson.toJsonTree(insert));
        return gson.toJson(response);
    }

    @PostMapping("/delete")
    @ResponseBody
    public String delete(DeleteRoleIds ids) {
        AssertUtil.assertNotNull(ids, () -> new BusinessException("删除id不能为空"));
        AssertUtil.assertNotNull(ids.getIds(), () -> new BusinessException("删除id不能为空"));
        roleService.delete(ids.getIds());
        Response response = Response.build(Status.NORMAL, null, null);
        return gson.toJson(response);
    }

    @GetMapping("/list")
    @ResponseBody
    public PageResponse<Role> list() {
        List<Role> list = roleService.list();
        return new PageResponse<>(0L, list);
    }
}
