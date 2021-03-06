package com.hello.world.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hello.world.Application;
import com.hello.world.dto.edit.EditUserDto;
import com.hello.world.enums.UserStatusEnum;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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
 * @date 2019/01/08 17:01
 **/
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest extends BaseMock {

  private static DbSetupTracker dbSetupTracker = new DbSetupTracker();

  @Before
  public void setUp() {
    Operation operation = Operations.sequenceOf(
            Operations.deleteAllFrom("role_permission"),
            Operations.insertInto("role_permission")
                    .columns("role_id", "permission_id")
                    .values(1, 1)
                    .values(1, 2)
                    .values(1, 3)
                    .values(1, 4)
                    .build()
    );

    DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
    dbSetupTracker.launchIfNecessary(dbSetup);
  }

  @Test
  @Transactional
  public void testList() throws Exception {
    mockMvc.perform(get("/api/users").header("auth-token", token))
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
    mockMvc.perform(get("/api/users/1").header("auth-token", token))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("code").value("200"))
            .andExpect(jsonPath("msg").value("success"))
            .andExpect(jsonPath("$.data.name").value("admin"))
            .andReturn();
  }

  @Test
  @Transactional
  public void testCreate() throws Exception {
    mockMvc.perform(post("/api/users").header("auth-token", token)
            .param("name", "test2")
            .param("phone", "18812345673"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("code").value("200"))
            .andExpect(jsonPath("msg").value("success"))
            .andExpect(jsonPath("$.data.name").value("test2"))
            .andReturn();
  }

  @Test
  @Transactional
  public void testCreateByPhoneExist() throws Exception {
    mockMvc.perform(post("/api/users").header("auth-token", token)
            .param("name", "test2")
            .param("phone", "18812345671"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("code").value("400"))
            .andExpect(jsonPath("msg").value("请求参数错误"))
            .andExpect(jsonPath("$.data[0].phone").value("手机号码已存在"))
            .andReturn();
  }

  @Test
  @Transactional
  public void testUpdate() throws Exception {
    EditUserDto editUserDto = new EditUserDto();
    editUserDto.setId(1L);
    editUserDto.setName("test-update");
    editUserDto.setPhone("12345678901");

    ObjectMapper mapper = new ObjectMapper();
    String jsonInString = mapper.writeValueAsString(editUserDto);

    mockMvc.perform(put("/api/users").header("auth-token", token)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(jsonInString))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.name").value("test-update"))
            .andExpect(jsonPath("$.data.phone").value("12345678901"))
            .andReturn();
  }

  @Test
  @Transactional
  public void testUpdateByUserArchived() throws Exception {
    EditUserDto editUserDto = new EditUserDto();
    editUserDto.setId(1L);
    editUserDto.setName("test-update");
    editUserDto.setPhone("12345678901");
    editUserDto.setStatus(UserStatusEnum.ARCHIVED);

    ObjectMapper mapper = new ObjectMapper();
    String jsonInString = mapper.writeValueAsString(editUserDto);

    mockMvc.perform(put("/api/users").header("auth-token", token)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(jsonInString))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("data").isEmpty())
            .andReturn();
  }
}
