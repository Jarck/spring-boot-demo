package com.hello.world.dto.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author jarck-lou
 * @date 2018/9/9 14:21
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePermissionDto {
  private Long id;
  @NotBlank(message = "权限名不能为空")
  private String name;
  @NotBlank(message = "权限不能为空")
  private String permission;
  private String resourceType;
}
