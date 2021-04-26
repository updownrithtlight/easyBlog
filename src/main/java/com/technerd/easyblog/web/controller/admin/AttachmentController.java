package com.technerd.easyblog.web.controller.admin;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.entity.Attachment;
import com.technerd.easyblog.model.dto.AttachLocationEnum;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.dto.QueryCondition;
import com.technerd.easyblog.model.enums.LogTypeEnum;
import com.technerd.easyblog.model.enums.PostTypeEnum;
import com.technerd.easyblog.model.enums.ResultCodeEnum;
import com.technerd.easyblog.model.vo.SearchVo;
import com.technerd.easyblog.service.AttachmentService;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import com.technerd.easyblog.utils.PageUtil;
import com.technerd.easyblog.utils.SensUtils;
import com.technerd.easyblog.web.controller.common.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * <pre>
 *     后台附件控制器
 * </pre>
 *
 * @author : saysky
 * @date : 2017/12/19
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/attachment")
@Api(value = "后台附件控制器")
public class AttachmentController extends BaseController {

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;

    /**
     * 获取upload的所有图片资源并渲染页面
     *
     * @param model model
     * @return 模板路径admin/admin_attachment
     */
    @GetMapping
    public String attachments(Model model,
                              @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
                              @RequestParam(value = "size", defaultValue = "20") Integer pageSize,
                              @RequestParam(value = "sort", defaultValue = "createTime") String sort,
                              @RequestParam(value = "order", defaultValue = "desc") String order,
                              @ModelAttribute SearchVo searchVo) {
        Long userId = 0L;
        Attachment condition = new Attachment();
        condition.setUserId(userId);
        Page page = PageUtil.initMpPage(pageNumber, pageSize, sort, order);
        Page<Attachment> attachments = attachmentService.findAll(
                page,
                new QueryCondition<>(condition, searchVo)
        );
        model.addAttribute("attachments", attachments.getRecords());
        model.addAttribute("pageInfo", PageUtil.convertPageVo(page));
        return "admin/admin_attachment";
    }

    /**
     *
     * @param attachId
     * @return
     */
    @GetMapping(value = "/detail")
    @ApiOperation(value = "获取附件详情")
    public JsonResult<Attachment> attachmentDetail( @RequestParam("id") Long attachId) {
        Attachment attachment = attachmentService.get(attachId);
        return new JsonResult<Attachment>(ResultCodeEnum.SUCCESS.getCode(),attachment);
    }


}
