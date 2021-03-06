package com.hello.world.auth;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;

/**
 * Shiro工具类
 *
 * @author jarck-lou
 * @date 2018/9/1 12:52
 **/
public final class ShiroKit {
  /**
   * 散列算法
   */
  public static final String HASH_ALGORITHM_NAME = "MD5";
  /**
   * 循环次数
   */
  public static final int HASH_ITERATIONS = 1024;
  private static final String NAMES_DELITETER = ",";

  private ShiroKit() {

  }

  /**
   * Shiro密码加密工具类
   *
   * @param credentials 密码
   * @param saltSource  密码盐
   * @return 加密后字符串
   */
  public static String md5(String credentials, String saltSource) {
    ByteSource salt = new Md5Hash(saltSource);
    return new SimpleHash(HASH_ALGORITHM_NAME, credentials, salt, HASH_ITERATIONS).toString();
  }

  /**
   * 获取随机盐值
   *
   * @param length 字节长度, 一个字节2位16进制数表示
   * @return salt
   */
  public static String getRandomSalt(int length) {
    return new SecureRandomNumberGenerator().nextBytes(length).toHex();
  }

  /**
   * 获取当前Subject
   *
   * @return Subject
   */
  public static Subject getSubject() {
    return SecurityUtils.getSubject();
  }

  /**
   * 验证当前用户是否属于该角色?, 使用时与lacksRole搭配使用
   *
   * @param roleName 角色名
   * @return 属于该角色: true, 否则false
   */
  public static boolean hasRole(String roleName) {
    return getSubject() != null && roleName != null
            && roleName.length() > 0 && getSubject().hasRole(roleName);
  }

  /**
   * 与hasRole标签逻辑相反, 当用户不属于该角色时验证通过
   *
   * @param roleName 角色名
   * @return 不属于该角色: true, 否则false
   */
  public static boolean lacksRole(String roleName) {
    return !hasRole(roleName);
  }

  /**
   * 验证当前用户是否属于以下任意一个角色
   *
   * @param roleNames 角色列表
   * @return 属于: true, 否则false
   */
  public static boolean hasAnyRoles(String roleNames) {
    boolean hasAnyRole = false;
    Subject subject = getSubject();
    if (subject != null && roleNames != null && roleNames.length() > 0) {
      for (String role : roleNames.split(NAMES_DELITETER)) {
        if (subject.hasRole(role.trim())) {
          hasAnyRole = true;
          break;
        }
      }
    }

    return hasAnyRole;
  }

  /**
   * 验证当前用户是否拥有指定权限, 使用时与lacksPermission搭配使用
   *
   * @param permission 权限名
   * @return 拥有权限: true, 否则false
   */
  public static boolean hasPermission(String permission) {
    return getSubject() != null && permission != null
            && permission.length() > 0
            && getSubject().isPermitted(permission);
  }

  /**
   * 与hasPermission标签逻辑相反, 当前用户没有指定权限时, 验证通过
   *
   * @param permission 权限名
   * @return 不拥有该权限: true, 否则false
   */
  public static boolean lacksPermission(String permission) {
    return !hasPermission(permission);
  }

  /**
   * 已认证通过的用户。不包含已记住的用户, 这是与user标签的区别所在。与notAuthenticated搭配使用
   *
   * @return 通过身份验证: true, 否则false
   */
  public static boolean isAuthenticated() {
    return getSubject() != null && getSubject().isAuthenticated();
  }

  /**
   * 未认证通过用户，与authenticated标签相对应。与guest标签的区别是，该标签包含已记住用户
   *
   * @return 没有通过身份验证: true, 否则false
   */
  public static boolean notAuthenticated() {
    return !isAuthenticated();
  }

  /**
   * 认证通过或已记住的用户。与guset搭配使用
   *
   * @return 用户: true, 否则false
   */
  public static boolean isUser() {
    return getSubject() != null && getSubject().getPrincipal() != null;
  }

  /**
   * 验证当前用户是否为“访客”, 即未认证（包含未记住）的用户。用user搭配使用
   *
   * @return 访客: true, 否则false
   */
  public static boolean isGuest() {
    return !isUser();
  }

  /**
   * 输出当前用户信息, 通常为登录帐号信息
   *
   * @return 当前用户信息
   */
  public static String principal() {
    if (getSubject() != null) {
      Object principal = getSubject().getPrincipal();

      return principal.toString();
    }

    return "";
  }
}
