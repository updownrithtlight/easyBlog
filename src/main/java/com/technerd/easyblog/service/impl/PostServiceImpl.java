package com.technerd.easyblog.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.technerd.easyblog.common.constant.RedisKeyExpire;
import com.technerd.easyblog.common.constant.RedisKeys;
import com.technerd.easyblog.entity.*;
import com.technerd.easyblog.mapper.*;
import com.technerd.easyblog.model.dto.Archive;
import com.technerd.easyblog.model.dto.CountDTO;
import com.technerd.easyblog.model.dto.PostSimpleDto;
import com.technerd.easyblog.model.enums.PostStatusEnum;
import com.technerd.easyblog.model.enums.PostTypeEnum;
import com.technerd.easyblog.service.CategoryService;
import com.technerd.easyblog.service.PostService;
import com.technerd.easyblog.utils.RedisUtil;
import com.technerd.easyblog.utils.SensUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <pre>
 *     ???????????????????????????
 * </pre>
 *
 * @author : saysky
 * @date : 2017/11/14
 */
@Service
@Slf4j
public class PostServiceImpl implements PostService {


    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostCategoryRefMapper postCategoryRefMapper;

    @Autowired
    private PostTagRefMapper postTagRefMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LinkMapper linkMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private RestHighLevelClient highLevelClient;


    @Override
    public Post updatePostStatus(Long postId, Integer status) {
        Post post = this.get(postId);
        post.setPostStatus(status);
        postMapper.updateById(post);
        return post;
    }

    @Override
    @Async
    public void updatePostView(Long postId) {
        postMapper.incrPostViews(postId);
    }

    @Override
    public Integer countByPostTypeAndStatus(String postType, Integer status) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("post_type", postType);
        queryWrapper.eq("post_status", status);
        return postMapper.selectCount(queryWrapper);
    }

    @Override
    public List<Post> findByPostTypeAndStatus(String postType, Integer status) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("post_type", postType);
        queryWrapper.eq("post_status", status);
        return postMapper.selectList(queryWrapper);
    }

    @Override
    public Page<Post> pagingByPostTypeAndStatus(String postType, Integer status, Page<Post> page) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("post_type", postType);
        queryWrapper.eq("post_status", status);
        return (Page<Post>) postMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Async
    public void updateAllSummary(Integer postSummary) {
        Map<String, Object> map = new HashMap<>();
        map.put("post_type", PostTypeEnum.POST_TYPE_POST.getValue());
        List<Post> posts = postMapper.selectByMap(map);
        for (Post post : posts) {
            String text = HtmlUtil.cleanHtmlTag(post.getPostContent());
            if (text.length() > postSummary) {
                postMapper.updatePostSummary(post.getId(), text.substring(0, postSummary));
            } else {
                postMapper.updatePostSummary(post.getId(), text);
            }
        }
    }

    @Override
    public List<Post> findAllPosts(String postType) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("post_type", postType);
        queryWrapper.orderByDesc("create_time");
        return postMapper.selectList(queryWrapper);
    }

    @Override
    public List<Post> findPostByStatus(Integer status, String postType) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("post_status", status);
        queryWrapper.eq("post_type", postType);
        return postMapper.selectList(queryWrapper);
    }

    @Override
    public Post findByPostId(Long postId, String postType) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", postId);
        queryWrapper.eq("post_type", postType);
        return postMapper.selectOne(queryWrapper);
    }

    @Override
    public Post findByPostUrl(String postUrl) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("post_url", postUrl);
        return postMapper.selectOne(queryWrapper);
    }

    @Override
    public Post findByPostUrl(String postUrl, String postType) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("post_url", postUrl);
        queryWrapper.eq("post_type", postType);
        return postMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Post> findPostLatest() {
        return postMapper.findTopFive();
    }

    @Override
    public Post findNextPost(Long postId, String postType) {
        return postMapper.findByPostIdAfter(postId, postType);
    }

    @Override
    public Post findPreciousPost(Long postId, String postType) {
        return postMapper.findByPostIdBefore(postId, postType);
    }

    @Override
    public List<Archive> findPostGroupByYearAndMonth() {
        List<Archive> archives = postMapper.findPostGroupByYearAndMonth();
        for (int i = 0; i < archives.size(); i++) {
            archives.get(i).setPosts(this.findPostByYearAndMonth(archives.get(i).getYear(), archives.get(i).getMonth()));
        }
        return archives;
    }

    @Override
    public List<Archive> findPostGroupByYear() {
        List<Archive> archives = postMapper.findPostGroupByYear();
        for (int i = 0; i < archives.size(); i++) {
            archives.get(i).setPosts(this.findPostByYear(archives.get(i).getYear()));
        }
        return archives;
    }

    @Override
    public List<PostSimpleDto> findPostByYearAndMonth(String year, String month) {
        return postMapper.findPostByYearAndMonth(year, month);
    }

    @Override
    public List<PostSimpleDto> findPostByYear(String year) {
        return postMapper.findPostByYear(year);
    }

    @Override
    public Page<Post> findPostByYearAndMonth(String year, String month, Page<Post> page) {
        return page.setRecords(postMapper.pagingPostByYearAndMonth(year, month, null));
    }

    @Override
    public Page<Post> findPostByCategory(Category category, Page<Post> page) {
        //?????????Id??????
        List<Long> ids = categoryService.selectChildCateId(category.getId());
        ids.add(category.getId());

        //???????????????????????????
        List<Post> postList = postMapper.pagingPostByCategoryIdsAndPostStatus(ids, PostStatusEnum.PUBLISHED.getCode(), page);
        for (int i = 0; i < postList.size(); i++) {
            List<Category> categories = categoryService.findByPostId(postList.get(i).getId());
            postList.get(i).setCategories(categories);
        }
        return page.setRecords(postList);
    }

    @Override
    public Page<Post> findPostsByTags(Tag tag, Page<Post> page) {
        //???????????????????????????
        List<Post> postList = postMapper.pagingPostsByTagIdAndPostStatus(tag.getId(), PostStatusEnum.PUBLISHED.getCode(), page);
        for (int i = 0; i < postList.size(); i++) {
            List<Category> categories = categoryService.findByPostId(postList.get(i).getId());
            postList.get(i).setCategories(categories);
        }
        return page.setRecords(postList);
    }


    @Override
    public Page<Post> findPostsByEs(Map<String, Object> criteria, Page<Post> page) {

        //search request
        SearchRequest searchRequest = new SearchRequest("blog");

        //search builder
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
        for (String key : criteria.keySet()) {
            booleanQueryBuilder.must(QueryBuilders.matchQuery(key, criteria.get(key)));
        }

        sourceBuilder.query(booleanQueryBuilder);
        sourceBuilder.from(Integer.parseInt(String.valueOf((page.getCurrent() - 1) * page.getSize())));
        sourceBuilder.size(Integer.parseInt(String.valueOf(page.getSize())));
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //sort
        sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));

        //highlight
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("postTitle");
        highlightTitle.preTags("<span class=\"highlight\">");
        highlightTitle.postTags("</span>");
        highlightBuilder.field(highlightTitle);
        sourceBuilder.highlighter(highlightBuilder);

        // add builder into request
        searchRequest.indices("blog");
        searchRequest.source(sourceBuilder);

        //response
        SearchResponse searchResponse = null;
        List<Post> postList = new ArrayList<>();
        try {
            searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            //search hits
            SearchHits hits = searchResponse.getHits();
            long totalHits = hits.getTotalHits();

            SearchHit[] searchHits = hits.getHits();
            page.setTotal((int) totalHits);
            for (SearchHit hit : searchHits) {
                String str = hit.getSourceAsString();
                Post esPost = JSONObject.parseObject(str, Post.class);
                //??????
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                HighlightField highlight = highlightFields.get("postTitle");
                if (highlight != null) {
                    Text[] fragments = highlight.fragments();
                    String fragmentString = fragments[0].string();
                    esPost.setPostTitle(fragmentString);
                }
                postList.add(esPost);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("ES?????????: {}", e);
        }
        return page.setRecords(postList);
    }

    @Override
    public List<Post> listSameCategoryPosts(Post post) {
        //?????????????????????????????????
        List<Category> categories = post.getCategories();
        List<Post> tempPosts = new ArrayList<>();
        for (Category category : categories) {
            tempPosts.addAll(postMapper.findPostsByCategoryId(category.getId()));
        }
        //?????????????????????
        tempPosts.remove(post);
        //?????????????????????
        List<Post> allPosts = new ArrayList<>();
        for (int i = 0; i < tempPosts.size(); i++) {
            if (!allPosts.contains(tempPosts.get(i))) {
                allPosts.add(tempPosts.get(i));
            }
        }
        //?????????????????????
        allPosts = allPosts.stream().sorted(Comparator.comparing(Post::getPostViews).reversed()).collect(Collectors.toList());
        return allPosts;
    }


    @Override
    public Long getTotalPostViews() {
        return postMapper.getPostViewsSum();
    }

    @Override
    public Integer countArticleByUserIdAndStatus(Long userId, Integer status) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("post_status", status);
        queryWrapper.eq("post_type", PostTypeEnum.POST_TYPE_POST.getValue());
        return postMapper.selectCount(queryWrapper);
    }

    @Override
    public String buildRss(List<Post> posts) {
        String rss = "";
        try {
            rss = SensUtils.getRss(posts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rss;
    }

    @Override
    public String buildArticleSiteMap() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("post_type", PostTypeEnum.POST_TYPE_POST.getValue());
        map.put("post_status", PostStatusEnum.PUBLISHED.getCode());
        List<Post> posts = postMapper.selectByMap(map);
        return SensUtils.getSiteMap(posts);
    }

    @Override
    public void resetCommentSize(Long postId) {
        postMapper.resetCommentSize(postId);
    }

    @Override
    public void incrPostLikes(Long postId) {
        postMapper.incrPostLikes(postId);
    }

    @Override
    public void deleteByUserId(Long userId) {
        postMapper.deleteByUserId(userId);
    }

    @Override
    public Integer countByUserId(Long userId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("post_type", PostTypeEnum.POST_TYPE_POST.getValue());
        return postMapper.selectCount(queryWrapper);
    }


    @Override
    public BaseMapper<Post> getRepository() {
        return postMapper;
    }

    @Override
    public Post insert(Post post) {
        post.setPostViews(0L);
        post.setCommentSize(0L);
        post.setPostLikes(0L);
        postMapper.insert(post);
        //????????????????????????
        if (post.getCategories() != null) {
            for (int i = 0; i < post.getCategories().size(); i++) {
                postCategoryRefMapper.insert(new PostCategoryRef(post.getId(), post.getCategories().get(i).getId()));
            }
        }
        //????????????????????????
        if (post.getTags() != null) {
            for (int i = 0; i < post.getTags().size(); i++) {
                postTagRefMapper.insert(new PostTagRef(post.getId(), post.getTags().get(i).getId()));
            }
        }
        //????????????
        RedisUtil.del(RedisKeys.USER_POST_RANKING_BY_VIEWS + post.getUserId());
        RedisUtil.del(RedisKeys.ALL_POST_RANKING_BY_VIEWS);
        return post;
    }

    @Override
    public Post update(Post post) {
        postMapper.updateById(post);
        if (post.getCategories() != null) {
            //???????????????????????????
            postCategoryRefMapper.deleteByPostId(post.getId());
            //???????????????????????????
            for (int i = 0; i < post.getCategories().size(); i++) {
                PostCategoryRef postCategoryRef = new PostCategoryRef(post.getId(), post.getCategories().get(i).getId());
                postCategoryRefMapper.insert(postCategoryRef);
                // ????????????
                RedisUtil.del(RedisKeys.POST_CATEGORY + post.getId());
            }
        }
        if (post.getTags() != null) {
            //???????????????????????????
            postTagRefMapper.deleteByPostId(post.getId());
            //???????????????????????????
            for (int i = 0; i < post.getTags().size(); i++) {
                PostTagRef postTagRef = new PostTagRef(post.getId(), post.getTags().get(i).getId());
                postTagRefMapper.insert(postTagRef);
                // ????????????
                RedisUtil.del(RedisKeys.POST_TAG + post.getId());
            }
        }
        return post;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long postId) {
        Post post = this.get(postId);
        if (post != null) {
            postTagRefMapper.deleteByPostId(postId);
            postCategoryRefMapper.deleteByPostId(postId);
            postMapper.deleteById(post.getId());
        }
        //????????????
        RedisUtil.del(RedisKeys.USER_POST_RANKING_BY_VIEWS + post.getUserId());
        RedisUtil.del(RedisKeys.ALL_POST_RANKING_BY_VIEWS);
    }

    @Override
    public QueryWrapper<Post> getQueryWrapper(Post post) {
        //?????????????????????
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        if (post != null) {
            if (StrUtil.isNotBlank(post.getPostTitle())) {
                queryWrapper.like("post_title", post.getPostTitle());
            }
            if (StrUtil.isNotBlank(post.getPostContent())) {
                queryWrapper.like("post_content", post.getPostContent());
            }
            if (StrUtil.isNotBlank(post.getPostType())) {
                queryWrapper.eq("post_type", post.getPostType());
            }
            if (post.getUserId() != null && post.getUserId() != -1) {
                queryWrapper.eq("user_id", post.getUserId());
            }
            if (post.getPostSource() != null && post.getPostSource() != -1) {
                queryWrapper.eq("post_source", post.getPostSource());
            }
            if (post.getPostStatus() != null && post.getPostStatus() != -1) {
                queryWrapper.eq("post_status", post.getPostStatus());
            }
            if (post.getAllowComment() != null && post.getAllowComment() != -1) {
                queryWrapper.eq("allow_comment", post.getAllowComment());
            }
        }
        return queryWrapper;
    }

    @Override
    public Post insertOrUpdate(Post post) {
        if (post.getId() == null) {
            insert(post);
        } else {
            update(post);
        }
        return post;
    }

    @Override
    public Integer getTodayCount() {
        return postMapper.getTodayCount();
    }

    @Override
    public List<PostSimpleDto> getPostRankingByPostView(Integer limit) {
        Object value = RedisUtil.get(RedisKeys.ALL_POST_RANKING_BY_VIEWS);
        // ?????????????????????????????????????????????
        if (StringUtils.isNotEmpty(value.toString())) {
            return JSON.parseArray(value.toString(), PostSimpleDto.class);
        }
        List<PostSimpleDto> postList = postMapper.getPostRankingByPostView(limit);
        RedisUtil.set(RedisKeys.ALL_POST_RANKING_BY_VIEWS, JSON.toJSONString(postList), RedisKeyExpire.ALL_POST_RANKING_BY_VIEWS);
        return postList;
    }

    @Override
    public List<PostSimpleDto> getPostRankingByUserIdAndPostView(Long userId, Integer limit) {
        Object value = RedisUtil.get(RedisKeys.USER_POST_RANKING_BY_VIEWS + userId);
        // ?????????????????????????????????????????????
        if (StringUtils.isNotEmpty(value.toString())) {
            return JSON.parseArray(value.toString(), PostSimpleDto.class);
        }
        List<PostSimpleDto> postList = postMapper.getPostRankingByUserIdAndPostView(userId, limit);
        RedisUtil.set(RedisKeys.USER_POST_RANKING_BY_VIEWS + userId, JSON.toJSONString(postList), RedisKeyExpire.USER_POST_RANKING_BY_VIEWS);
        return postList;
    }

    @Override
    public Page<Post> findPostByCateName(String cateName, Page<Post> page) {
        return page.setRecords(postMapper.findPostByCateName(cateName, page));
    }

    @Override
    public CountDTO getAllCount() {
        Object value = RedisUtil.get(RedisKeys.ALL_COUNT);
        // ?????????????????????????????????????????????
        if (StringUtils.isNotEmpty(value.toString())) {
            return JSON.parseObject(value.toString(), CountDTO.class);
        }
        CountDTO countDTO = new CountDTO();
        countDTO.setUserCount(userMapper.selectCount(null));
        countDTO.setPostCount(postMapper.selectCount(null));
        countDTO.setCategoryCount(categoryService.getTotalCount());
        countDTO.setTagCount(tagMapper.selectCount(null));
        countDTO.setCommentCount(commentMapper.selectCount(null));
        countDTO.setLinkCount(linkMapper.selectCount(null));
        countDTO.setViewCount(this.getTotalPostViews());
        RedisUtil.set(RedisKeys.ALL_COUNT, JSON.toJSONString(countDTO), RedisKeyExpire.ALL_COUNT);
        return countDTO;
    }
}

