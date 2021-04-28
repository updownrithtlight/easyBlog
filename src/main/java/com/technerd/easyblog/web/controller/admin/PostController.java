package com.technerd.easyblog.web.controller.admin;

import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.entity.Post;
import com.technerd.easyblog.entity.User;
import com.technerd.easyblog.exception.MyBlogException;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.dto.EasyConst;
import com.technerd.easyblog.model.enums.*;
import com.technerd.easyblog.model.vo.SearchVo;
import com.technerd.easyblog.service.CategoryService;
import com.technerd.easyblog.service.PostService;
import com.technerd.easyblog.service.TagService;
import com.technerd.easyblog.service.UserService;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import com.technerd.easyblog.utils.PageUtil;
import com.technerd.easyblog.utils.EasyUtils;
import com.technerd.easyblog.web.controller.common.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * <pre>
 *     后台文章管理控制器
 * </pre>
 *
 * @author : saysky
 * @date : 2017/12/10
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/post")
@Api(value = "后台文章管理控制器")
public class PostController extends BaseController {

    @Autowired
    private PostService postService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;


    @Autowired
    private LocaleMessageUtil localeMessageUtil;


    @PostMapping
    @ApiOperation(value = "文章列表")
    public JsonResult<Page<Post>> posts(@RequestBody  SearchVo searchVo ) {
        Page page = PageUtil.initMpPage(searchVo);
        Page<Post> posts = postService.findAll(page);
        return new JsonResult<Page<Post>>(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.permission-denied"),posts);
    }


    /**
     *
     * @param post
     * @return
     */
    @PostMapping(value = "/save")
    @SystemLog(description = "保存文章", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "添加/更新文章")
    public JsonResult pushPost(@RequestBody Post post) {
        super.save(post);
        //1、如果开启了文章审核，非管理员文章默认状态为审核
        Boolean isOpenCheck = StringUtils.equals(EasyConst.OPTIONS.get(BlogPropertiesEnum.OPEN_POST_CHECK.getProp()), TrueFalseEnum.TRUE.getValue());
        if (isOpenCheck && !isAdmin()) {
            post.setPostStatus(PostStatusEnum.CHECKING.getCode());
        }
        post.setUserId(getUserId());

        //2、非管理员只能修改自己的文章，管理员都可以修改
        Post originPost = null;
        if (post.getId() != null) {
            originPost = postService.get(post.getId());
            if (!Objects.equals(originPost.getUserId(), getUserId()) && !isAdmin()) {
                return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.permission-denied"));
            }
            //以下属性不能修改
            post.setUserId(originPost.getUserId());
            post.setPostViews(originPost.getPostViews());
            post.setCommentSize(originPost.getCommentSize());
            post.setPostLikes(originPost.getPostLikes());
            post.setCommentSize(originPost.getCommentSize());
            post.setDelFlag(originPost.getDelFlag());
        }
        //3、提取摘要
        int postSummary = 100;
        if (StringUtils.isNotEmpty(EasyConst.OPTIONS.get(BlogPropertiesEnum.POST_SUMMARY.getProp()))) {
            postSummary = Integer.parseInt(EasyConst.OPTIONS.get(BlogPropertiesEnum.POST_SUMMARY.getProp()));
        }
        //文章摘要
        String summaryText = HtmlUtil.cleanHtmlTag(post.getPostContent());
        if (summaryText.length() > postSummary) {
            String summary = summaryText.substring(0, postSummary);
            post.setPostSummary(summary);
        } else {
            post.setPostSummary(summaryText);
        }

        //4、分类标签
//        List<Category> categories = categoryService.cateIdsToCateList(cateIds, user.getId());
//        post.setCategories(categories);
//        if (StringUtils.isNotEmpty(tagList)) {
//            List<Tag> tags = tagService.strListToTagList(user.getId(), StringUtils.deleteWhitespace(tagList));
//            post.setTags(tags);
//        }
//        //当没有选择文章缩略图的时候，自动分配一张内置的缩略图
//        if (StringUtils.equals(post.getPostThumbnail(), BlogPropertiesEnum.DEFAULT_THUMBNAIL.getProp())) {
//            String staticUrl = SensConst.OPTIONS.get(BlogPropertiesEnum.BLOG_STATIC_URL.getProp());
//            if (!Strings.isNullOrEmpty(staticUrl)) {
//                post.setPostThumbnail(staticUrl + "/static/images/thumbnail/img_" + RandomUtil.randomInt(0, 14) + ".jpg");
//            } else {
//                post.setPostThumbnail("/static/images/thumbnail/img_" + RandomUtil.randomInt(0, 14) + ".jpg");
//            }
//        }
        post.setPostType(PostTypeEnum.POST_TYPE_POST.getValue());
        postService.insertOrUpdate(post);
        if (isOpenCheck && !isAdmin()) {
            return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), "文章已提交审核");
        }
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.operation-success"));
    }

    /**
     * 限制一篇文章最多5个分类
     *
     * @param cateIdList
     * @param tagList
     */
    private void checkCategoryAndTag(List<Long> cateIdList, String tagList) {
        if (cateIdList.size() > 5) {
            throw new MyBlogException("每篇文章最多5个分类");
        }
        String[] tags = tagList.split(",");
        if (tags.length > 5) {
            throw new MyBlogException("每篇文章最多5个标签");
        }
        for (String tag : tags) {
            if (tag.length() > 20) {
                throw new MyBlogException("每个标签长度最多为20个字符");
            }
        }
    }

    /**
     * 处理移至回收站的请求
     *
     * @param postId 文章编号
     * @return 重定向到/admin/post
     */
    @PostMapping(value = "/throw")
    @SystemLog(description = "将文章移到回收站", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "将文章移到回收站")
    public JsonResult moveToTrash(@RequestParam("id") Long postId) {
        Post post = postService.get(postId);
        basicCheck(post);
        postService.updatePostStatus(postId, PostStatusEnum.RECYCLE.getCode());
        log.info("编号为" + postId + "的文章已被移到回收站");
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.operation-success"));
    }

    /**
     * 处理文章为发布的状态
     *
     * @param postId 文章编号
     * @return 重定向到/admin/post
     */
    @PostMapping(value = "/revert")
    @SystemLog(description = "将文章改为已发布", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "将文章改为已发布")
    public JsonResult moveToPublish(@RequestParam("id") Long postId) {
        Post post = postService.get(postId);
        basicCheck(post);
        postService.updatePostStatus(postId, PostStatusEnum.PUBLISHED.getCode());
        log.info("编号为" + postId + "的文章已改变为发布状态");
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.operation-success"));
    }

    /**
     * 审核通过文章
     *
     * @param postId 文章编号
     * @return 重定向到/admin/post
     */
    @PostMapping(value = "/pass")
    @SystemLog(description = "审核通过文章", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "审核通过文章")
    public JsonResult passCheck(@RequestParam("id") Long postId) {
        Post post = postService.get(postId);
        basicCheck(post);
        postService.updatePostStatus(postId, PostStatusEnum.PUBLISHED.getCode());
        log.info("编号为" + postId + "的文章已通过审核");
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.operation-success"));
    }


    /**
     * 处理删除文章的请求
     *
     * @param postId 文章编号
     * @return 重定向到/admin/post
     */
    @PostMapping(value = "/delete")
    @SystemLog(description = "删除文章", type = LogTypeEnum.OPERATION)
    @ApiOperation("删除文章")
    public JsonResult removePost(@RequestParam("id") Long postId) {
        Post post = postService.get(postId);
        basicCheck(post);
        postService.delete(postId);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.delete-success"));
    }

    /**
     * 批量删除
     *
     * @param ids 文章ID列表
     * @return 重定向到/admin/post
     */
    @DeleteMapping(value = "/batchDelete")
    @SystemLog(description = "批量删除文章", type = LogTypeEnum.OPERATION)
    @ApiOperation("批量删除文章")
    public JsonResult batchDelete(@RequestParam("ids") List<Long> ids) {

        //批量操作
        //1、防止恶意操作
        if (ids == null || ids.size() == 0 || ids.size() >= 100) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), "参数不合法!");
        }
        //2、检查用户权限
        //文章作者才可以删除
        List<Post> postList = postService.findByBatchIds(ids);
        for (Post post : postList) {
            if (!Objects.equals(post.getUserId(), 0L)) {
                return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.permission-denied"));
            }
        }
        //3、如果当前状态为回收站，则删除；否则，移到回收站
        for (Post post : postList) {
            if (Objects.equals(post.getPostStatus(), PostStatusEnum.RECYCLE.getCode())) {
                postService.delete(post.getId());
            } else {
                post.setPostStatus(PostStatusEnum.RECYCLE.getCode());
                postService.update(post);
            }
        }
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.delete-success"));
    }


    /**
     * 检查文章是否存在和用户是否有权限控制
     *
     * @param post
     */
    private void basicCheck(Post post) {
        if (post == null) {
            throw new MyBlogException(localeMessageUtil.getMessage("code.admin.common.post-not-exist"));
        }
        //只有创建者有权删除
        User user = new User();
        //管理员和文章作者可以删除
        Boolean isAdmin = true;
        if (!Objects.equals(post.getUserId(), user.getId()) && !isAdmin) {
            throw new MyBlogException(localeMessageUtil.getMessage("code.admin.common.permission-denied"));
        }
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
        return new JsonResult<Post>(ResultCodeEnum.SUCCESS.getCode(), "",post);
    }



    /**
     * 验证文章路径是否已经存在
     *
     * @param postUrl 文章路径
     * @return JsonResult
     */
    @GetMapping(value = "/postUrl")
    @ApiOperation(value = "验证文章路径")
    public JsonResult checkUrlExists(@RequestParam("postUrl") String postUrl) {
        postUrl = urlFilter(postUrl);
        Post post = postService.findByPostUrl(postUrl, PostTypeEnum.POST_TYPE_POST.getValue());
        if (null != post) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.url-is-exists"));
        }
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), "");
    }


    /**
     * 更新所有摘要
     *
     * @param postSummary 文章摘要字数
     * @return JsonResult
     */
    @PostMapping(value = "/resetAllSummary")
    @SystemLog(description = "更新所有摘要", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "更新所有摘要")
    public JsonResult resetAllSummary(@RequestParam("postSummary") Integer postSummary) {
        postService.updateAllSummary(postSummary);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.update-success"));
    }

    /**
     * 将所有文章推送到百度
     *
     * @param baiduToken baiduToken
     * @return JsonResult
     */
    @PostMapping(value = "/saveAllToBaidu")
    @SystemLog(description = "将所有文章推送到百度", type = LogTypeEnum.OPERATION)
    public JsonResult pushAllToBaidu(@RequestParam("baiduToken") String baiduToken) {
        if (StringUtils.isEmpty(baiduToken)) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.post.no-baidu-token"));
        }
        String blogUrl = EasyConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp());
        List<Post> posts = postService.findAllPosts(PostTypeEnum.POST_TYPE_POST.getValue());
        StringBuilder urls = new StringBuilder();
        for (Post post : posts) {
            urls.append(blogUrl);
            urls.append("/article/");
            urls.append(post.getPostUrl());
            urls.append("\n");
        }
        EasyUtils.baiduPost(blogUrl, baiduToken, urls.toString());
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.post.push-to-baidu-success"));
    }


    /**
     * 去除html，htm后缀，以及将空格替换成-
     *
     * @param url url
     * @return String
     */
    private static String urlFilter(String url) {
        if (null != url) {
            final boolean urlEndsWithHtmlPostFix = url.endsWith(".html") || url.endsWith(".htm");
            if (urlEndsWithHtmlPostFix) {
                return url.substring(0, url.lastIndexOf("."));
            }
        }
        return StringUtils.replaceAll(url, " ", "-");
    }
}
