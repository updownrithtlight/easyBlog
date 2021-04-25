package com.technerd.easyblog.web.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.entity.Tag;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.enums.LogTypeEnum;
import com.technerd.easyblog.model.enums.ResultCodeEnum;
import com.technerd.easyblog.model.vo.SearchVo;
import com.technerd.easyblog.service.TagService;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import com.technerd.easyblog.utils.PageUtil;
import com.technerd.easyblog.web.controller.common.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * <pre>
 *     后台标签管理控制器
 * </pre>
 *
 * @author : saysky
 * @date : 2017/12/10
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/tag")
@Api(value = "后台标签管理控制器")
public class TagController extends BaseController {

    @Autowired
    private TagService tagService;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;


    @PostMapping("/list")
    @ApiOperation(value = "标签列表")
    public JsonResult<Page<Tag>> tags(@RequestBody SearchVo searchVo) {
        Page page = PageUtil.initMpPage(searchVo);
        Page<Tag> tagPage = tagService.findAll( page);
        return new JsonResult<Page<Tag>>(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.save-success"),tagPage);
    }

    /**
     * 新增/修改标签
     *
     * @param tag tag
     */
    @PostMapping(value = "/save")
    @SystemLog(description = "保存标签", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "保存标签")
    public JsonResult saveTag(@RequestBody Tag tag) {
        Long userId = getLoginUserId();
        //1.判断该标签是否为当前用户
        if (tag.getId() != null) {
            Tag checkId = tagService.get(tag.getId());
            if (checkId != null && !checkId.getUserId().equals(userId)) {
                return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.permission-denied"));
            }
        }
        //2.添加或者保存
        tag.setUserId(userId);
        tagService.insertOrUpdate(tag);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.save-success"));

    }

    /**
     * 删除标签
     *
     * @param tagId 标签Id
     * @return JsonResult
     */
    @GetMapping(value = "/delete")
    @SystemLog(description = "删除标签", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "删除标签")
    public JsonResult checkDelete(@RequestParam("id") Long tagId) {
        tagService.delete(tagId);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.delete-success"));
    }

    /**
     * 标签详情
     * @param tagId
     * @return
     */
    @GetMapping(value = "/get")
    @ApiOperation(value = "获取标签详情")
    public JsonResult<Tag> get(@RequestParam("id") Long tagId) {
        //当前修改的标签
        Tag tag = tagService.get(tagId);
        return new JsonResult<Tag>(ResultCodeEnum.SUCCESS.getCode(),"",tag);
    }
}
