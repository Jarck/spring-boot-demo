package com.hello.world.dto.result;

import com.hello.world.entity.Company;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author jarck-lou
 * @date 2018/9/9 17:37
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class CompanyDto extends Company {

  @ApiModelProperty(value = "公司对应的城市")
  private CityDto city;
}
