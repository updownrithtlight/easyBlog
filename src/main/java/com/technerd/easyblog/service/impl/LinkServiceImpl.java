package com.technerd.easyblog.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.technerd.easyblog.common.constant.RedisKeyExpire;
import com.technerd.easyblog.common.constant.RedisKeys;
import com.technerd.easyblog.entity.Link;
import com.technerd.easyblog.mapper.LinkMapper;
import com.technerd.easyblog.service.LinkService;
import com.technerd.easyblog.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <pre>
 *     友情链接业务逻辑实现类
 * </pre>
 *
 * @author : saysky
 * @date : 2017/11/14
 */
@Service
public class LinkServiceImpl implements LinkService {


    @Autowired
    private LinkMapper linkMapper;


    @Override
    public BaseMapper<Link> getRepository() {
        return linkMapper;
    }

    @Override
    public QueryWrapper<Link> getQueryWrapper(Link link) {
        //对指定字段查询
        QueryWrapper<Link> queryWrapper = new QueryWrapper<>();
        if (link != null) {
            if (StrUtil.isNotBlank(link.getLinkName())) {
                queryWrapper.like("link_name", link.getLinkName());
            }
            if (StrUtil.isNotBlank(link.getLinkUrl())) {
                queryWrapper.like("link_url", link.getLinkUrl());
            }
        }
        return queryWrapper;
    }

    @Override
    public Link insertOrUpdate(Link entity) {
        if (entity.getId() == null) {
            insert(entity);
        } else {
            update(entity);
        }
        // 删除缓存
        RedisUtil.del(RedisKeys.ALL_LINK);
        return entity;
    }

    @Override
    public void delete(Long id) {
        linkMapper.deleteById(id);
        // 删除缓存
        RedisUtil.del(RedisKeys.ALL_LINK);
    }

    @Override
    public List<Link> findAll() {
        Object o = RedisUtil.get(RedisKeys.ALL_LINK);
        // 先从缓存取，缓存没有从数据库取
        if (StringUtils.isNotEmpty(o.toString())) {
            return JSON.parseArray(o.toString(), Link.class);
        }
        List<Link> linkList = linkMapper.selectList(null);
        RedisUtil.set(RedisKeys.ALL_LINK, JSON.toJSONString(linkList), RedisKeyExpire.ALL_LINK);
        return linkList;
    }
}
