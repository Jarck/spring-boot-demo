package hello.dto.result;

import hello.entity.City;
import lombok.Data;

import java.io.Serializable;

/**
 * 城市DTO
 *
 * @author jarck-lou
 * @date 2018/9/1 12:52
 **/
@Data
public class CityDto extends City implements Serializable {
  public CityDto() {

  }

  public CityDto(City city) {
    this.setId(city.getId());
    this.setName(city.getName());
    this.setCreatedAt(city.getCreatedAt());
    this.setUpdatedAt(city.getUpdatedAt());
  }
}
