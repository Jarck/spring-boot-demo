package com.hello.world.resolver.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.hello.world.dto.condition.SearchCityDto;
import com.hello.world.entity.City;
import com.hello.world.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jarck-lou
 * @date 2018/9/1 12:52
 **/
@Component
public class CityQuery implements GraphQLQueryResolver {
  @Autowired
  private ICityService cityService;

  /**
   * 通过查询条件查询城市
   *
   * @param searchCityDto 查询条件
   * @return 城市列表
   */
  public List<City> searchCities(SearchCityDto searchCityDto) {
    List<City> cityList = cityService.searchWithCondition(searchCityDto);

    return cityList;
  }

  /**
   * 通过ID查询城市
   *
   * @param cityId 城市ID
   * @return 城市
   */
  public City searchCityWithId(Long cityId) {
    City city = cityService.searchWithId(cityId);

    return city;
  }

  /**
   * 通过城市名查询
   *
   * @param cityName 城市名
   * @return 城市列表
   */
  public List<City> searchCityWithName(String cityName) {
    List<City> cityList = cityService.searchWithName(cityName);

    return cityList;
  }
}
