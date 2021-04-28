package com.technerd.easyblog.web.controller.portal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.technerd.easyblog.entity.Post;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.enums.*;
import com.technerd.easyblog.model.vo.SearchVo;
import com.technerd.easyblog.service.PostService;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import com.technerd.easyblog.utils.PageUtil;
import com.technerd.easyblog.web.controller.common.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <pre>
 *     前台文章管理控制器
 * </pre>
 *
 * @author : technerd
 */
@Slf4j
@RestController
@RequestMapping(value = "/portal/post")
@Api(value = "前台文章管理控制器")
public class PostPortalController extends BaseController {

    @Autowired
    private PostService postService;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;


    @PostMapping
    @ApiOperation(value = "文章列表")
    public JsonResult<Page<Post>> posts(@RequestBody  SearchVo searchVo ) {
        Page page = PageUtil.initMpPage(searchVo);
        Page<Post> posts = postService.findAll(page);
        return new JsonResult<Page<Post>>(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.query-success"),posts);
    }


    /**
     *
     * @param postId
     * @return
     */
    @GetMapping(value = "/get")
    @ApiOperation(value = "文章页面")
    public JsonResult<Post> get(@RequestParam("id") Long postId) {
        Post post = postService.get(postId);
        return new JsonResult<Post>(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.query-success"),post);
    }


}
