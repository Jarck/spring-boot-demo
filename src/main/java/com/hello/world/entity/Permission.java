package com.hello.world.entity;

import com.hello.world.enums.PermissionAvailableEnum;
import lombok.Data;

import java.util.Date;

/**
 * 权限
 *
 * @author jarck-lou
 * @date 2018/9/1 12:52
 **/
@Data
public class Permission {
  private Long id;
  private String name;
  private String permission;
  private String resourceType;
  private PermissionAvailableEnum available;
  private Date createdAt;
  private Date updatedAt;
}