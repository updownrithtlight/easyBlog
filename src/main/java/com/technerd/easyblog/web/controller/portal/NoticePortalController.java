package com.technerd.easyblog.web.controller.portal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.entity.Post;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.enums.LogTypeEnum;
import com.technerd.easyblog.model.enums.PostStatusEnum;
import com.technerd.easyblog.model.enums.PostTypeEnum;
import com.technerd.easyblog.model.enums.ResultCodeEnum;
import com.technerd.easyblog.model.vo.CommonEnum;
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
 *     后台公告管理控制器
 * </pre>
 *
 * @author : saysky
 * @date : 2017/12/10
 */
@Slf4j
@RestController
@RequestMapping(value = "/portal/notice")
@Api(value = "后台公告管理控制器")
public class NoticePortalController extends BaseController {


    @Autowired
    private PostService postService;

    @Autowired
    LocaleMessageUtil localeMessageUtil;

    /**
     *
     * @param searchVo
     * @return
     */
    @PostMapping(value = "/list")
    @ApiOperation("查询所有公告")
    public JsonResult<Page<Post>> categories(@RequestBody SearchVo searchVo) {
        Page page = PageUtil.initMpPage(searchVo);
        Page<Post> categoryPage = postService.findAll( page);
        return new JsonResult<Page<Post>>(CommonEnum.SUCCESS.getCode(),categoryPage) ;
    }


    /**
     * 跳转到修改公告
     *
     * @param postId 公告编号
     * @return admin/admin_page_editor
     */
    @GetMapping(value = "/get")
    @ApiOperation(value = "查询详情")
    public JsonResult<Post> editPage(@RequestParam("id") Long postId) {
        Post post = postService.get(postId);
        return new JsonResult<Post>(ResultCodeEnum.SUCCESS.getCode(), post);
    }



}
