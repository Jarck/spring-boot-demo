package com.hello.world.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hello.world.Application;
import com.hello.world.dto.create.CreateCompanyDto;
import com.hello.world.dto.edit.EditCompanyDto;
import com.hello.world.enums.CompanyStatusEnum;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author jarck-lou
 * @date 2019/02/12 15:59
 **/
@Profile("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompanyControllerTest extends BaseMock {
  @Autowired
  private static DbSetupTracker dbSetupTracker = new DbSetupTracker();

  @Before
  public void setUp() {
    Operation operation = Operations.sequenceOf(
            Operations.deleteAllFrom("role_permission"),
            Operations.insertInto("role_permission")
                    .columns("role_id", "permission_id")
                    .values(1, 17)
                    .values(1, 18)
                    .values(1, 19)
                    .values(1, 20)
                    .build()
    );

    DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
    dbSetupTracker.launchIfNecessary(dbSetup);
  }

  @Test
  @Transactional
  public void testList() throws Exception {
    mockMvc.perform(get("/api/companies").header("auth-token", token))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("code").value("200"))
            .andExpect(jsonPath("msg").value("success"))
            .andExpect(jsonPath("$.data.size").value(2))
            .andReturn();
  }

  @Test
  @Transactional
  public void testShow() throws Exception {
    mockMvc.perform(get("/api/companies/1").header("auth-token", token))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("code").value("200"))
            .andExpect(jsonPath("msg").value("success"))
            .andExpect(jsonPath("$.data.name").value("杭州xxx有限公司"))
            .andReturn();
  }

  @Test
  @Transactional
  public void testShowbyCompanyNotExist() throws Exception {
    mockMvc.perform(get("/api/companies/9999999").header("auth-token", token))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("code").value("200"))
            .andExpect(jsonPath("msg").value("success"))
            .andExpect(jsonPath("$.data").isEmpty())
            .andReturn();
  }

  @Test
  @Transactional
  public void testCreate() throws Exception {
    CreateCompanyDto createCompanyDto = new CreateCompanyDto();
    createCompanyDto.setName("createCompanyName");
    createCompanyDto.setShortName("createCompanyShortName");

    ObjectMapper mapper = new ObjectMapper();
    String jsonInString = mapper.writeValueAsString(createCompanyDto);

    mockMvc.perform(post("/api/companies").header("auth-token", token)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(jsonInString))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("code").value("200"))
            .andExpect(jsonPath("msg").value("success"))
            .andExpect(jsonPath("$.data.name").value("createCompanyName"))
            .andReturn();
  }

  @Test
  @Transactional
  public void testCreateByCompanyExist() throws Exception {
    CreateCompanyDto createCompanyDto = new CreateCompanyDto();
    createCompanyDto.setName("杭州xxx有限公司");
    createCompanyDto.setShortName("杭州");

    ObjectMapper mapper = new ObjectMapper();
    String jsonInString = mapper.writeValueAsString(createCompanyDto);

    mockMvc.perform(post("/api/companies").header("auth-token", token)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(jsonInString))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("code").value("400"))
            .andExpect(jsonPath("msg").value("请求参数错误"))
            .andExpect(jsonPath("$.data[0].name").value("公司名称已存在"))
            .andReturn();
  }

  @Test
  @Transactional
  public void testUpdate() throws Exception {
    EditCompanyDto editCompanyDto = new EditCompanyDto();
    editCompanyDto.setId(1L);
    editCompanyDto.setName("updateCompanyName");
    editCompanyDto.setStatus(CompanyStatusEnum.ARCHIVED);

    ObjectMapper mapper = new ObjectMapper();
    String jsonInString = mapper.writeValueAsString(editCompanyDto);

    mockMvc.perform(put("/api/companies").header("auth-token", token)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(jsonInString))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("code").value("200"))
            .andExpect(jsonPath("msg").value("success"))
            .andExpect(jsonPath("$.data.name").value("updateCompanyName"))
            .andExpect(jsonPath("$.data.status").value("ARCHIVED"))
            .andReturn();
  }

  @Test
  @Transactional
  public void testUpdateByCompanyNotExist() throws Exception {
    EditCompanyDto editCompanyDto = new EditCompanyDto();
    editCompanyDto.setId(9999999L);
    editCompanyDto.setName("companyNotExist");

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonInString = objectMapper.writeValueAsString(editCompanyDto);

    mockMvc.perform(put("/api/companies").header("auth-token", token)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(jsonInString))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("code").value("500"))
            .andExpect(jsonPath("msg").value("公司不存在"))
            .andReturn();
  }
}
