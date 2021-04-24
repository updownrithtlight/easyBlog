package com.technerd.easyblog.web.controller.admin;

import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.entity.Menu;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.enums.LogTypeEnum;
import com.technerd.easyblog.model.enums.MenuTypeEnum;
import com.technerd.easyblog.model.enums.ResultCodeEnum;
import com.technerd.easyblog.service.MenuService;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <pre>
 *     后台菜单管理控制器
 * </pre>
 *
 * @author : saysky
 * @date : 2018/1/30
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/menu")
@Api(value = "后台菜单管理控制器")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    LocaleMessageUtil localeMessageUtil;



    /**
     * 新增/修改菜单
     *
     * @param menu menu
     * @return
     */
    @PostMapping(value = "/save")
    @SystemLog(description = "保存菜单", type = LogTypeEnum.OPERATION)
    @ApiOperation("新增/修改菜单")
    public JsonResult saveMenu(@RequestBody Menu menu) {
        menuService.insertOrUpdate(menu);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.reply-success"));
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/get")
    @ApiOperation(value = "菜单详情")
    public JsonResult<Menu> get(@RequestParam("id") Long id) {
        Menu menu = menuService.get(id);
        return new JsonResult<Menu>(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.reply-success"),menu);
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/delete")
    @SystemLog(description = "删除菜单", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "删除菜单")
    public JsonResult removeMenu(@RequestParam("id") Long id) {
        //1.先查看该菜单是否有子节点，如果有不能删除
        List<Menu> childMenus = menuService.findByMenuPid(id);
        if (childMenus == null || childMenus.size() == 0) {
            menuService.delete(id);
        } else {
            return new JsonResult<Menu>(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.must-delete-parent-node"));
        }
        return new JsonResult<Menu>(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.reply-success"));
    }

}
