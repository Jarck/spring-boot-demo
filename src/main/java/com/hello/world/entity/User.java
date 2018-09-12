package com.hello.world.entity;

import com.hello.world.enums.UserStatusEnum;
import lombok.Data;

import java.util.Date;

/**
 * 用户
 *
 * @author jarck-lou
 * @date 2018/9/1 12:52
 **/
@Data
public class User {
  private Long id;
  private String name;
  private String phone;
  private Long cityId;
  private Long companyId;
  private UserStatusEnum status;
  private Date createdAt;
  private Date updatedAt;
}