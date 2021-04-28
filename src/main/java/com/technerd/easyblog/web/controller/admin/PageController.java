package com.technerd.easyblog.web.controller.admin;

import cn.hutool.core.util.RandomUtil;
import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.entity.Link;
import com.technerd.easyblog.entity.Post;
import com.technerd.easyblog.entity.User;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.enums.BlogPropertiesEnum;
import com.technerd.easyblog.model.enums.LogTypeEnum;
import com.technerd.easyblog.model.enums.PostTypeEnum;
import com.technerd.easyblog.model.enums.ResultCodeEnum;
import com.technerd.easyblog.service.LinkService;
import com.technerd.easyblog.service.LogService;
import com.technerd.easyblog.service.PostService;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import com.technerd.easyblog.web.controller.common.BaseController;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <pre>
 *     后台页面管理控制器
 * </pre>
 *
 * @author : saysky
 * @date : 2017/12/10
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/page")
@Api(value = "后台页面管理控制器")
public class PageController extends BaseController {

    @Autowired
    private LinkService linkService;

    @Autowired
    private PostService postService;

    @Autowired
    private LogService logService;


    @Autowired
    LocaleMessageUtil localeMessageUtil;

    /**
     * 保存页面
     *
     * @param post post
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "保存页面")
    public JsonResult pushPage(@RequestBody Post post) {
        super.save(post);
        //发表用户
        post.setPostType(PostTypeEnum.POST_TYPE_PAGE.getValue());
        postService.insertOrUpdate(post);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.save-success"));
    }


    /**
     * 获取页面详情
     * @param pageId
     * @return
     */
    @GetMapping(value = "/get")
    @ApiOperation(value = "获取页面详情")
    public JsonResult<Post> get(@RequestParam("id") Long pageId) {
        Post post = postService.get(pageId);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(),post);
    }

    /**
     * 检查该路径是否已经存在
     *
     * @param postUrl postUrl
     * @return JsonResult
     */
    @GetMapping(value = "/checkUrl")
    @ApiOperation(value = "检查该路径是否已经存在")
    public JsonResult checkUrlExists(@RequestParam("postUrl") String postUrl) {
        Post post = postService.findByPostUrl(postUrl, PostTypeEnum.POST_TYPE_PAGE.getValue());
        if (null != post) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.url-is-exists"));
        }
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), "");
    }
}
