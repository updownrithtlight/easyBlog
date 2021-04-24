package com.technerd.easyblog.web.controller.admin;

import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.entity.Permission;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.enums.LogTypeEnum;
import com.technerd.easyblog.model.enums.ResultCodeEnum;
import com.technerd.easyblog.model.vo.CommonEnum;
import com.technerd.easyblog.service.PermissionService;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <pre>
 *     后台权限管理控制器
 * </pre>
 *
 * @author : saysky
 * @date : 2017/12/10
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/permission")
@Api(value = "后台权限管理控制器")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;

    /**
     *
     * @return
     */
    @GetMapping(value = "/getAll")
    @ApiOperation(value = "权限列表")
    public JsonResult<List<Permission>> permissions() {
        //权限列表
        List<Permission> permissions = permissionService.findPermissionListWithLevel(null);
        return new JsonResult<List<Permission>>(CommonEnum.SUCCESS.getCode(), permissions);
    }

    /**
     * 新增/修改权限
     *
     * @param permission permission对象
     * @return 重定向到/admin/permission
     */
    @PostMapping(value = "/save")
    @SystemLog(description = "保存权限", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "新增/修改权限")
    public JsonResult savePermission(@RequestBody Permission permission) {
        permissionService.insertOrUpdate(permission);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.save-success"));
    }

    /**
     * 删除权限
     *
     * @param permissionId 权限Id
     * @return JsonResult
     */
    @GetMapping(value = "/delete")
    @SystemLog(description = "删除权限", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "删除权限")
    public JsonResult checkDelete(@RequestParam("id") Long permissionId) {
        permissionService.delete(permissionId);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.delete-success"));
    }


    /**
     * 查询详情
     *
     * @param permissionId permissionId
     */
    @GetMapping(value = "/get")
    @ApiOperation("查询详情")
    public JsonResult<Permission> get( @RequestParam("id") Long permissionId) {
        Permission permission = permissionService.get(permissionId);
        return new JsonResult<Permission>(CommonEnum.SUCCESS.getCode(), permission);
    }
}
