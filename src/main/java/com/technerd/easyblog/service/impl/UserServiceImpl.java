package com.technerd.easyblog.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.technerd.easyblog.common.constant.RedisKeyExpire;
import com.technerd.easyblog.common.constant.RedisKeys;
import com.technerd.easyblog.entity.User;
import com.technerd.easyblog.exception.MyBlogException;
import com.technerd.easyblog.mapper.UserMapper;
import com.technerd.easyblog.service.RoleService;
import com.technerd.easyblog.service.UserService;
import com.technerd.easyblog.utils.Md5Util;
import com.technerd.easyblog.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * <pre>
 *     用户业务逻辑实现类
 * </pre>
 *
 * @author : saysky
 * @date : 2017/11/14
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

//    @Autowired
//    private RedisUtil redisUtil;

    @Override
    public User findByUserName(String userName) {

        return userMapper.findByUserName(userName);
    }

    @Override
    public User findByEmail(String userEmail) {

        return userMapper.findByEmail(userEmail);
    }

    @Override
    public void updatePassword(Long userId, String password) {
        User user = new User();
        user.setId(userId);
        user.setPassword(Md5Util.toMd5(password, "nerd", 10));
        userMapper.updateByPrimaryKeySelective(user);
//        redisUtil.del(RedisKeys.USER + userId);

    }


    @Override
    public User insert(User user) {
        //1.验证表单数据是否合法
        basicUserCheck(user);
        //2.验证用户名和邮箱是否存在
        checkUserNameAndUserName(user);
        String userPass = Md5Util.toMd5(user.getPassword(), "nerd", 10);
        user.setPassword(userPass);
        userMapper.insert(user);
        return user;
    }

    @Override
    public User update(User user) {
        //1.验证表单数据是否合法
        basicUserCheck(user);
        //2.验证用户名和邮箱是否存在
        checkUserNameAndUserName(user);

        if(user.getPassword() != null) {
            String userPass = Md5Util.toMd5(user.getPassword(), "sens", 10);
            user.setPassword(userPass);
        }
        userMapper.updateByPrimaryKeySelective(user);
//        redisUtil.del(RedisKeys.USER + user.getId());
        return user;
    }


    private void basicUserCheck(User user) {
        if (user.getName() == null || user.getEmail() == null || user.getNickName() == null) {
            throw new MyBlogException("请输入完整信息!");
        }
        String userName = user.getName();
        userName = userName.trim().replaceAll(" ", "-");
        if (userName.length() < 4 || userName.length() > 20) {
            throw new MyBlogException("用户名长度为4-20位!");
        }
        if (Strings.isNotEmpty(user.getPassword())) {
            if (user.getPassword().length() < 6 || user.getPassword().length() > 20) {
                throw new MyBlogException("用户密码为6-20位!");
            }
        }
        if (!Validator.isEmail(user.getEmail())) {
            throw new MyBlogException("电子邮箱格式不合法!");
        }
        if (user.getNickName().length() < 1 || user.getNickName().length() > 20) {
            throw new MyBlogException("昵称为2-20位");
        }
    }

    private void checkUserNameAndUserName(User user) {
        //验证用户名和邮箱是否存在
        if (user.getName() != null) {
            User nameCheck = findByUserName(user.getName());
            Boolean isExist = (user.getId() == null && nameCheck != null) ||
                    (user.getId() != null && nameCheck != null && !Objects.equals(nameCheck.getId(), user.getId()));
            if (isExist) {
                throw new MyBlogException("用户名已经存在");
            }
        }
        if (user.getEmail() != null) {
            User emailCheck = findByEmail(user.getEmail());
            Boolean isExist = (user.getId() == null && emailCheck != null) ||
                    (user.getId() != null && emailCheck != null && !Objects.equals(emailCheck.getId(), user.getId()));
            if (isExist) {
                throw new MyBlogException("电子邮箱已经存在");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long userId) {
//        //删除用户
//        User user = get(userId);
//        if (user != null) {
//            //1.修改用户状态为已删除
//            userMapper.deleteById(userId);
//            //2.修改用户和角色关联
//            roleService.deleteByUserId(userId);
//            //3.文章删除
//            postService.deleteByUserId(userId);
//            //4.评论删除
//            commentService.deleteByUserId(userId);
//            commentService.deleteByAcceptUserId(userId);
//            //5.分类和标签
//            categoryService.deleteByUserId(userId);
//            tagService.deleteByUserId(userId);
//        }
    }

    @Override
    public User insertOrUpdate(User entity) {
        if (entity.getId() == null) {
            insert(entity);
        } else {
            update(entity);
        }
        return entity;
    }

    @Override
    public User get(Long id) {
//        String value = redisUtil.get(RedisKeys.USER + id);
        // 先从缓存取，缓存没有从数据库取
//        if (StringUtils.isNotEmpty(value)) {
//            return JSON.parseObject(value, User.class);
//        }
        User user = userMapper.selectByPrimaryKey(id);
//        if(user != null) {
//            redisUtil.set(RedisKeys.USER + id, JSON.toJSONString(user), RedisKeyExpire.USER);
//        }
        return user;
    }
}
