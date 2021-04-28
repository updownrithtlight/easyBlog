package com.technerd.easyblog.web.controller.portal;

import com.technerd.easyblog.entity.Tag;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.enums.ResultCodeEnum;
import com.technerd.easyblog.service.TagService;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import com.technerd.easyblog.web.controller.common.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <pre>
 *     标签控制器
 * </pre>
 *
 * @author : saysky
 * @date : 2017/12/10
 */
@Slf4j
@RestController
@RequestMapping(value = "/portal/tag")
@Api(value = "前台标签管理控制器")
public class TagPortalController extends BaseController {

    @Autowired
    private TagService tagService;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;



    @GetMapping("/list")
    @ApiOperation(value = "标签列表")
    public JsonResult<List<Tag>> tags(@RequestParam Long userId) {
        List<Tag> all = tagService.findByUserId(userId);
        return new JsonResult<List<Tag>>(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.query-success"),all);
    }


}
