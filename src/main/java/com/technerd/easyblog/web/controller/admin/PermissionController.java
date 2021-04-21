package com.technerd.easyblog.web.controller.admin;

import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.entity.Permission;
import com.technerd.easyblog.model.enums.LogTypeEnum;
import com.technerd.easyblog.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 查询所有权限并渲染permission页面
     *
     * @return 模板路径admin/admin_permission
     */
    @GetMapping
    public String permissions(Model model) {
        //权限列表
        List<Permission> permissions = permissionService.findPermissionListWithLevel(null);
        model.addAttribute("permissions", permissions);
        return "admin/admin_permission";
    }

    /**
     * 新增/修改权限
     *
     * @param permission permission对象
     * @return 重定向到/admin/permission
     */
    @PostMapping(value = "/save")
    @SystemLog(description = "保存权限", type = LogTypeEnum.OPERATION)
    public String savePermission(@ModelAttribute Permission permission) {
        permissionService.insertOrUpdate(permission);
        return "redirect:/admin/permission";
    }

    /**
     * 删除权限
     *
     * @param permissionId 权限Id
     * @return JsonResult
     */
    @GetMapping(value = "/delete")
    @SystemLog(description = "删除权限", type = LogTypeEnum.OPERATION)
    public String checkDelete(@RequestParam("id") Long permissionId) {
        permissionService.delete(permissionId);
        return "redirect:/admin/permission";
    }


    /**
     * 跳转到修改页面
     *
     * @param permissionId permissionId
     * @param model  model
     * @return 模板路径admin/admin_permission
     */
    @GetMapping(value = "/edit")
    public String toEditPermission(Model model, @RequestParam("id") Long permissionId) {
        //更新的权限
        Permission permission = permissionService.get(permissionId);
        model.addAttribute("updatePermission", permission);

        //所有权限
        List<Permission> permissions = permissionService.findPermissionListWithLevel(null);
        model.addAttribute("permissions", permissions);
        return "admin/admin_permission";
    }
}
