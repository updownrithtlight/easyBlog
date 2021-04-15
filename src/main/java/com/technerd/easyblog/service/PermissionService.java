package com.technerd.easyblog.service;



import com.technerd.easyblog.entity.Permission;

import java.util.List;
import java.util.Set;

/**
 * <pre>
 *     权限逻辑接口
 * </pre>
 *
 * @author : saysky
 * @date : 2017/11/14
 */
public interface PermissionService {

    /**
     * 根据角色Id获得权限列表
     *
     * @param roleId 角色Id
     * @return 权限列表
     */
    List<Permission> listPermissionsByRoleId(Long roleId);

    /**
     * 获得某个用户的权限URL列表
     * @param userId
     * @return
     */
    Set<String> findPermissionUrlsByUserId(Long userId);

    /**
     * 获得某个用户的用户ID和资源类型
     * @param userId
     * @param resourceType
     * @return
     */
    List<Permission> findPermissionTreeByUserIdAndResourceType(Long userId, String resourceType);

    /**
     * 根据资源类型获得权限
     * @param resourceType
     * @return
     */
    List<Permission> findByResourceType(String resourceType);



    void insert(Permission entity) ;
    void insertOrUpdate(Permission entity) ;

    /**
     * 修改
     *
     * @param entity
     * @return
     */
    void update(Permission entity) ;

    /**
     *
     * @param id
     */
    void delete(Long id) ;

}
