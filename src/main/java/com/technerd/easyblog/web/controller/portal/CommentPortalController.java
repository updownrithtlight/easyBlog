package com.technerd.easyblog.web.controller.portal;

import cn.hutool.core.lang.Validator;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HtmlUtil;
import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.entity.Comment;
import com.technerd.easyblog.entity.Post;
import com.technerd.easyblog.entity.User;
import com.technerd.easyblog.exception.MyBlogException;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.dto.EasyConst;
import com.technerd.easyblog.model.enums.*;
import com.technerd.easyblog.service.CommentService;
import com.technerd.easyblog.service.MailService;
import com.technerd.easyblog.service.PostService;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import com.technerd.easyblog.utils.OwoUtil;
import com.technerd.easyblog.web.controller.common.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <pre>
 *     前台评论管理控制器
 * </pre>
 *
 * @author : technerd
 */
@Slf4j
@RestController
@RequestMapping(value = "/portal/comment")
@Api(value = "前台评论管理控制器")
public class CommentPortalController extends BaseController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MailService mailService;

    @Autowired
    private PostService postService;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;





    /**
     * 将评论改变为发布状态
     * 评论状态有两种：待审核1，回收站2
     * 对待审核转发布的，发邮件
     *
     * @param commentId 评论编号
     * @return 重定向到/admin/comment
     */
    @PostMapping(value = "/revert")
    @SystemLog(description = "回滚评论", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "回滚评论")
    public JsonResult moveToPublish(@RequestParam("id") Long commentId) {
        User loginUser = new User();
        //评论
        Comment comment = commentService.get(commentId);
        //检查权限
        basicCheck(comment);

        Post post = postService.get(comment.getPostId());
        Comment result = commentService.updateCommentStatus(commentId, CommentStatusEnum.PUBLISHED.getCode());
        //判断是否启用邮件服务
        new NoticeToAuthor(result, post, loginUser, comment.getCommentStatus()).start();

        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.operation-success"));
    }

    /**
     * 删除评论
     *
     * @param commentId commentId
     * @return string
     */
    @PostMapping(value = "/delete")
    @SystemLog(description = "删除评论", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "删除评论")
    public JsonResult moveToAway(@RequestParam("id") Long commentId) {
        //评论
        Comment comment = commentService.get(commentId);
        //检查权限
        basicCheck(comment);

        if (Objects.equals(comment.getCommentStatus(), CommentStatusEnum.RECYCLE.getCode())) {
            commentService.delete(commentId);
        } else {
            commentService.updateCommentStatus(commentId, CommentStatusEnum.RECYCLE.getCode());
        }
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.delete-success"));
    }


    /**
     * 管理员回复评论，并通过评论
     *
     * @param commentId      被回复的评论
     * @param commentContent 回复的内容
     * @return 重定向到/admin/comment
     */
    @PostMapping(value = "/reply")
    @SystemLog(description = "回复评论", type = LogTypeEnum.OPERATION)
    @ApiOperation("回复评论")
    public JsonResult replyComment(@RequestParam("id") Long commentId,
                                   @RequestParam("commentContent") String commentContent,
                                   @RequestParam("userAgent") String userAgent,
                                   HttpServletRequest request) {
        //博主信息
        User loginUser = new User();
        //被回复的评论
        Comment lastComment = commentService.get(commentId);
        if (lastComment == null) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.comment-not-exist"));
        }

        Post post = postService.get(lastComment.getPostId());
        if (post == null) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.post-not-exist"));
        }
        //修改被回复的评论的状态
        if (Objects.equals(lastComment.getCommentStatus(), CommentStatusEnum.CHECKING.getCode())) {
            lastComment.setCommentStatus(CommentStatusEnum.PUBLISHED.getCode());
            commentService.insertOrUpdate(lastComment);
        }

        //保存评论
        Comment comment = new Comment();
        comment.setUserId(loginUser.getId());
        comment.setPostId(lastComment.getPostId());
        comment.setCommentAuthor(loginUser.getUserDisplayName());
        comment.setCommentAuthorEmail(loginUser.getUserEmail());
        comment.setCommentAuthorUrl(EasyConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()));
        comment.setCommentAuthorIp(ServletUtil.getClientIP(request));
        comment.setCommentAuthorAvatar(loginUser.getUserAvatar());
        String lastContent = "<a href='#comment-id-" + lastComment.getId() + "'>@" + lastComment.getCommentAuthor() + "</a> ";
        comment.setCommentContent(lastContent + OwoUtil.markToImg(HtmlUtil.escape(commentContent)));
        comment.setCommentAgent(userAgent);
        comment.setCommentParent(commentId);
        comment.setCommentStatus(CommentStatusEnum.PUBLISHED.getCode());
        //判断是否是博主
        if (Objects.equals(loginUser.getId(), post.getUserId())) {
            comment.setIsAdmin(1);
        } else {
            comment.setIsAdmin(0);
        }
        comment.setAcceptUserId(lastComment.getUserId());
        comment.setPathTrace(lastComment.getPathTrace() + lastComment.getId() + "/");
        commentService.insertOrUpdate(comment);
        //邮件通知
        new EmailToAuthor(comment, lastComment, post, loginUser, commentContent).start();
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.reply-success"));

    }

    /**
     * 批量删除
     *
     * @param ids 评论ID列表
     * @return
     */
    @DeleteMapping(value = "/batchDelete")
    @ResponseBody
    @SystemLog(description = "批量删除评论", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "批量删除评论")
    public JsonResult batchDelete(@RequestParam("ids") List<Long> ids) {
        Long loginUserId = 0L;
        //批量操作
        //1、防止恶意操作
        if (ids == null || ids.size() == 0 || ids.size() >= 100) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), "参数不合法!");
        }
        //2、检查用户权限
        //文章作者才可以删除
        List<Comment> commentList = commentService.findByBatchIds(ids);
        for (Comment comment : commentList) {
            if (!Objects.equals(comment.getUserId(), loginUserId) && !Objects.equals(comment.getAcceptUserId(), loginUserId)) {
                return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.permission-denied"));
            }
        }
        //3、如果当前状态为回收站，则删除；否则，移到回收站
        for (Comment comment : commentList) {
            if (Objects.equals(comment.getCommentStatus(), PostStatusEnum.RECYCLE.getCode())) {
                commentService.delete(comment.getId());
            } else {
                comment.setCommentStatus(PostStatusEnum.RECYCLE.getCode());
                commentService.update(comment);
            }
        }
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.delete-success"));
    }

    /**
     * 检查文章是否存在和用户是否有权限控制
     *
     * @param comment
     */
    private void basicCheck(Comment comment) {
        Long loginUserId = 0L ;
        if (comment == null) {
            throw new MyBlogException(localeMessageUtil.getMessage("code.admin.common.comment-not-exist"));
        }
        //文章
        Post post = postService.get(comment.getPostId());
        if (post == null) {
            throw new MyBlogException(localeMessageUtil.getMessage("code.admin.common.post-not-exist"));
        }
        //检查权限，文章的作者和收到评论的可以删除
        if (!Objects.equals(post.getUserId(), loginUserId) && !Objects.equals(comment.getAcceptUserId(), loginUserId)) {
            throw new MyBlogException(localeMessageUtil.getMessage("code.admin.common.permission-denied"));
        }
    }

    /**
     * 异步发送邮件回复给评论者
     */
    class EmailToAuthor extends Thread {

        private Comment comment;
        private Comment lastComment;
        private Post post;
        private User user;
        private String commentContent;

        private EmailToAuthor(Comment comment, Comment lastComment, Post post, User user, String commentContent) {
            this.comment = comment;
            this.lastComment = lastComment;
            this.post = post;
            this.user = user;
            this.commentContent = commentContent;
        }

        @Override
        public void run() {
            if (StringUtils.equals(EasyConst.OPTIONS.get(BlogPropertiesEnum.SMTP_EMAIL_ENABLE.getProp()), TrueFalseEnum.TRUE.getValue()) && StringUtils.equals(EasyConst.OPTIONS.get(BlogPropertiesEnum.COMMENT_REPLY_NOTICE.getProp()), TrueFalseEnum.TRUE.getValue())) {
                if (Validator.isEmail(lastComment.getCommentAuthorEmail())) {
                    Map<String, Object> map = new HashMap<>(8);
                    map.put("blogTitle", EasyConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()));
                    map.put("commentAuthor", lastComment.getCommentAuthor());
                    map.put("pageName", post.getPostTitle());
                    if (StringUtils.equals(post.getPostType(), PostTypeEnum.POST_TYPE_POST.getValue())) {
                        map.put("pageUrl", EasyConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()) + "/article/" + post.getId() + "#comment-id-" + comment.getId());
                    } else if (StringUtils.equals(post.getPostType(), PostTypeEnum.POST_TYPE_NOTICE.getValue())) {
                        map.put("pageUrl", EasyConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()) + "/notice/" + post.getId() + "#comment-id-" + comment.getId());
                    } else {
                        map.put("pageUrl", EasyConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()) + "/p/" + post.getPostUrl() + "#comment-id-" + comment.getId());
                    }
                    map.put("commentContent", lastComment.getCommentContent());
                    map.put("replyAuthor", user.getUserDisplayName());
                    map.put("replyContent", commentContent);
                    map.put("blogUrl", EasyConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()));
                    mailService.sendTemplateMail(
                            lastComment.getCommentAuthorEmail(), "您在" + EasyConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()) + "的评论有了新回复", map, "common/mail_template/mail_reply.ftl");
                }
            }
        }
    }

    /**
     * 异步通知评论者审核通过
     */
    class NoticeToAuthor extends Thread {

        private Comment comment;
        private Post post;
        private User user;
        private Integer status;

        private NoticeToAuthor(Comment comment, Post post, User user, Integer status) {
            this.comment = comment;
            this.post = post;
            this.user = user;
            this.status = status;
        }

        @Override
        public void run() {
            if (StringUtils.equals(EasyConst.OPTIONS.get(BlogPropertiesEnum.SMTP_EMAIL_ENABLE.getProp()), TrueFalseEnum.TRUE.getValue()) && StringUtils.equals(EasyConst.OPTIONS.get(BlogPropertiesEnum.COMMENT_REPLY_NOTICE.getProp()), TrueFalseEnum.TRUE.getValue())) {
                try {
                    //待审核的评论变成已通过，发邮件
                    if (status == 1 && Validator.isEmail(comment.getCommentAuthorEmail())) {
                        Map<String, Object> map = new HashMap<>(6);
                        if (StringUtils.equals(post.getPostType(), PostTypeEnum.POST_TYPE_POST.getValue())) {
                            map.put("pageUrl", EasyConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()) + "/article/" + post.getId() + "#comment-id-" + comment.getId());
                        } else if (StringUtils.equals(post.getPostType(), PostTypeEnum.POST_TYPE_NOTICE.getValue())) {
                            map.put("pageUrl", EasyConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()) + "/notice/" + post.getId() + "#comment-id-" + comment.getId());
                        } else {
                            map.put("pageUrl", EasyConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()) + "/p/" + post.getPostUrl() + "#comment-id-" + comment.getId());
                        }
                        map.put("pageName", post.getPostTitle());
                        map.put("commentContent", comment.getCommentContent());
                        map.put("blogUrl", EasyConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp()));
                        map.put("blogTitle", EasyConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()));
                        map.put("author", user.getUserDisplayName());
                        mailService.sendTemplateMail(
                                comment.getCommentAuthorEmail(),
                                "您在" + EasyConst.OPTIONS.get(BlogPropertiesEnum.BLOG_TITLE.getProp()) + "的评论已审核通过！", map, "common/mail_template/mail_passed.ftl");
                    }
                } catch (Exception e) {
                    log.error("邮件服务器未配置：{}", e.getMessage());
                }
            }
        }
    }
}