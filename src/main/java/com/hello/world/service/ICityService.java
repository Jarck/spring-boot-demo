package com.hello.world.service;

import com.hello.world.dto.create.CreateCityDto;
import com.hello.world.dto.condition.SearchCityDto;
import com.hello.world.dto.result.CityDto;
import com.hello.world.entity.City;

import java.util.List;

/**
 * @author jarck-lou
 * @date 2018/9/1 12:52
 **/
public interface ICityService {
  /**
   * find by id
   *
   * @param cityId city id
   * @return cityDto
   */
  CityDto searchWithId(Long cityId);

  /**
   * 通过城市名查询
   *
   * @param cityName
   * @return
   */
  List<City> searchWithName(String cityName);

  /**
   * 按条件查询
   *
   * @param searchCityDto 搜索条件
   * @return
   */
  List<City> searchWithCondition(SearchCityDto searchCityDto);

  /**
   * 创建城市
   *
   * @param createCityDto
   * @return
   */
  Long createCity(CreateCityDto createCityDto);
}