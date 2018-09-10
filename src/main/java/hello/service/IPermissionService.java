package hello.service;

import hello.dto.create.CreatePermissionDto;
import hello.entity.Permission;

import java.util.List;

/**
 * @author jarck-lou
 * @date 2018/9/9 20:01
 **/
public interface IPermissionService {
  /**
   * find by id
   *
   * @param permissionId
   * @return
   */
  Permission searchWithId(Long permissionId);

  /**
   * 通过权限名查询
   *
   * @param name
   * @return
   */
  List<Permission> searchWithName(String name);

  /**
   * 通过角色id查询
   *
   * @param roleId
   * @return
   */
  List<Permission> searchWithRoleId(Long roleId);

  /**
   * 通过用户id查询
   *
   * @param userId
   * @return
   */
  List<Permission> searchWithUserId(Long userId);

  /**
   * 创建权限
   *
   * @param createPermissionDto
   * @return
   */
  Long insertPermission(CreatePermissionDto createPermissionDto);
}
