package com.technerd.easyblog.web.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.entity.Category;
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

import javax.servlet.http.HttpServletRequest;

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
@RequestMapping(value = "/admin/notice")
@Api(value = "后台公告管理控制器")
public class NoticeController extends BaseController {


    @Autowired
    private PostService postService;

    @Autowired
    LocaleMessageUtil localeMessageUtil;

    @Autowired
    private HttpServletRequest request;
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
     * 保存公告
     *
     * @param post post
     */
    @PostMapping(value = "/save")
    @SystemLog(description = "保存公告", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "保存公告")
    public JsonResult pushPage(@ModelAttribute Post post) {
        String msg = localeMessageUtil.getMessage("code.admin.common.save-success");
        //发表用户
        Object admin_id = request.getAttribute("admin_id");
        long id = Long.parseLong(admin_id.toString());
        Long userId = id;
        post.setUserId(userId);
        post.setCreateBy(userId+"");
        post.setPostType(PostTypeEnum.POST_TYPE_NOTICE.getValue());
        if (null != post.getId()) {
            post.setPostViews(postService.get(post.getId()).getPostViews());
            msg = localeMessageUtil.getMessage("code.admin.common.update-success");
        }
        postService.insertOrUpdate(post);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), msg);
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

    /**
     * 处理移至回收站的请求
     *
     * @param postId 公告编号
     * @return 重定向到/admin/post
     */
    @PostMapping(value = "/throw")
    @SystemLog(description = "将公告移到回收站", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "发布公告")
    public JsonResult moveToTrash(@RequestParam("id") Long postId) {
        try {
            Post post = postService.get(postId);
            if (post == null) {
                return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.post-not-exist"));
            }
            postService.updatePostStatus(postId, PostStatusEnum.RECYCLE.getCode());
        } catch (Exception e) {
            log.error("删除公告到回收站失败：{}", e.getMessage());
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.operation-failed"));
        }
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.operation-success"));
    }

    /**
     * 处理公告为发布的状态
     *
     * @param postId 公告编号
     * @return 重定向到/admin/post
     */
    @PostMapping(value = "/revert")
    @SystemLog(description = "将公告改为发布的状态", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "发布公告")
    public JsonResult moveToPublish(@RequestParam("id") Long postId) {
        try {
            Post post = postService.get(postId);
            if (post == null) {
                return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.post-not-exist"));
            }

            postService.updatePostStatus(postId, PostStatusEnum.PUBLISHED.getCode());
        } catch (Exception e) {
            log.error("发布公告失败：{}", e.getMessage());
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.operation-failed"));
        }
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.operation-success"));
    }

    /**
     *
     * @param postId
     * @return
     */
    @PostMapping(value = "/delete")
    @SystemLog(description = "删除公告", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "删除公告")
    public JsonResult removePost(@RequestParam("id") Long postId) {
        Post post = postService.get(postId);
        if (post == null) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.post-not-exist"));
        }
        postService.delete(postId);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.delete-success"));
    }


}
