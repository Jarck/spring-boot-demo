package com.hello.world.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hello.world.dao.CompanyMapper;
import com.hello.world.dto.PageDto;
import com.hello.world.dto.condition.SearchCompanyDto;
import com.hello.world.dto.create.CreateCompanyDto;
import com.hello.world.dto.edit.EditCompanyDto;
import com.hello.world.dto.result.CompanyDto;
import com.hello.world.service.ICompanyService;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jarck-lou
 * @date 2018/9/9 17:47
 **/
@Service
public class CompanyServiceImpl implements ICompanyService {
  @Autowired
  private CompanyMapper companyMapper;

  @Override
  public CompanyDto searchWithId(Long companyId) {
    CompanyDto companyDto = companyMapper.selectByPrimaryKey(companyId);

    return companyDto;
  }

  @Override
  public CompanyDto searchCompanyAndCityWithId(Long companyId) {
    return companyMapper.searchCompanyAndCityWithId(companyId);
  }

  @Override
  public List<CompanyDto> searchWithName(String name) {
    List<CompanyDto> companyList = companyMapper.searchWithName(name);

    return companyList;
  }

  @Override
  public List<CompanyDto> searchWithCityId(Long cityId) {
    List<CompanyDto> companyList = companyMapper.searchWithCityId(cityId);

    return companyList;
  }

  @Override
  public List<CompanyDto> searchCondition(SearchCompanyDto searchCompanyDto) {
    List<CompanyDto> companyList = companyMapper.searchCompanyAndCity(searchCompanyDto);

    return companyList;
  }

  @Override
  public PageInfo<CompanyDto> searchCondition(SearchCompanyDto searchCompanyDto, PageDto pageDto) {
    PageHelper.startPage(pageDto.getPageNum(), pageDto.getPageSize());
    PageHelper.orderBy(pageDto.getOrderBy() + " " + (pageDto.isDesc() ? "desc" : "acs"));

    List<CompanyDto> companyList = companyMapper.searchCompanyAndCity(searchCompanyDto);

    PageInfo<CompanyDto> companyPage = new PageInfo<>(companyList);

    return companyPage;
  }

  @Override
  public CompanyDto createCompany(CreateCompanyDto companyDto) {
    long i = companyMapper.createCompany(companyDto);

    return companyMapper.searchCompanyAndCityWithId(companyDto.getId());
  }

  @Override
  public CompanyDto updateCompany(EditCompanyDto editCompanyDto) throws NotFoundException {
    CompanyDto companyDto = companyMapper.selectByPrimaryKey(editCompanyDto.getId());

    if (companyDto == null) {
      throw new NotFoundException("公司不存在");
    }

    long i = companyMapper.update(editCompanyDto);

    return companyMapper.searchCompanyAndCityWithId(editCompanyDto.getId());
  }

  @Override
  public boolean exitsCompanyName(String name) {
    int count = companyMapper.countByName(name);

    return count > 0;
  }
}
