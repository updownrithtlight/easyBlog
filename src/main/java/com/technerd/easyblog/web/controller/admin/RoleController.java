package com.technerd.easyblog.web.controller.admin;

import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.entity.Role;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.enums.LogTypeEnum;
import com.technerd.easyblog.model.enums.ResultCodeEnum;
import com.technerd.easyblog.service.PermissionService;
import com.technerd.easyblog.service.RoleService;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <pre>
 *     后台角色管理控制器
 * </pre>
 *
 * @author : saysky
 * @date : 2017/12/10
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/role")
@Api(value = "后台角色管理控制器")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;



    /**
     * 新增/修改角色
     *
     * @param role role对象
     * @return 重定向到/admin/role
     */
    @PostMapping(value = "/save")
    @SystemLog(description = "保存角色", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "保存角色")
    public JsonResult saveRole(@RequestBody Role role) {
        roleService.insertOrUpdate(role);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage(""));
    }

    /**
     * 删除角色
     *
     * @param roleId 角色Id
     * @return JsonResult
     */
    @DeleteMapping(value = "/delete")
    @SystemLog(description = "删除角色", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "删除角色")
    public JsonResult checkDelete(@RequestParam("id") Long roleId) {
        //判断这个角色有没有用户
        Integer userCount = roleService.countUserByRoleId(roleId);
        if (userCount != 0) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.role.delete-failed"));
        }
        roleService.delete(roleId);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.delete-success"));
    }


    /**
     *
     * @param roleId
     * @return
     */
    @GetMapping(value = "/get")
    @ApiOperation(value = "获取角色")
    public JsonResult<Role> get(@RequestParam("id") Long roleId) {
        Role role = roleService.findByRoleId(roleId);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), "",role);
    }
}
