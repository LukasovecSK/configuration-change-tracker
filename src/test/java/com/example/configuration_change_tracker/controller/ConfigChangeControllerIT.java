package com.example.configuration_change_tracker.controller;

import com.example.configuration_change_tracker.enums.ConfigChangeTypeEnum;
import com.example.configuration_change_tracker.persistence.entity.ConfigChangeEntity;
import com.example.configuration_change_tracker.persistence.repository.ConfigChangeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.configuration_change_tracker.util.DataProvider.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class ConfigChangeControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ConfigChangeRepository repository;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setup() {
    repository.deleteAll();

    var entity1 =
        ConfigChangeEntity.builder().id(ID).type(ConfigChangeTypeEnum.APPROVAL_POLICY).newValue("1000").oldValue("500")
            .critical(true).changedBy("user1").build();

    repository.save(entity1);
  }

  @Test
  void shouldAddConfigChange() throws Exception {
    mockMvc.perform(post("/api/config-changes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createCreateDto(true))))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isString());

    assertEquals(2, repository.count());
  }

  @Test
  void shouldEditConfigChange() throws Exception {
    mockMvc.perform(put("/api/config-changes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createUpdateDto(false))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ID));

    var entity = repository.findById(ID).orElse(null);
    assertNotNull(entity);
    assertFalse(entity.isCritical());
  }

  @Test
  void shouldGetConfigChangeById() throws Exception {
    mockMvc.perform(get("/api/config-changes/{id}", ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ID))
        .andExpect(jsonPath("$.type").value("APPROVAL_POLICY"))
        .andExpect(jsonPath("$.newValue").value("1000"))
        .andExpect(jsonPath("$.oldValue").value("500"))
        .andExpect(jsonPath("$.changedBy").value("user1"));
  }

  @Test
  void shouldReturn404WhenConfigChangeNotFoundWhenGet() throws Exception {
    mockMvc.perform(get("/api/config-changes/{id}", NOT_EXISTING_ID))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldDeleteConfigChangeById() throws Exception {
    mockMvc.perform(delete("/api/config-changes/{id}", ID))
        .andExpect(status().isOk());

    var entities = repository.findAll();
    assertEquals(0, entities.size());
  }

  @Test
  void shouldReturn404WhenConfigChangeNotFoundWhenDelete() throws Exception {
    mockMvc.perform(delete("/api/config-changes/{id}", NOT_EXISTING_ID)).andExpect(status().isNotFound());
  }

  @Test
  void shouldGetConfigChangesAndReturnOneRecord() throws Exception {
    mockMvc.perform(get("/api/config-changes?type=APPROVAL_POLICY", ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(ID))
        .andExpect(jsonPath("$[0].type").value("APPROVAL_POLICY"))
        .andExpect(jsonPath("$[0].newValue").value("1000"))
        .andExpect(jsonPath("$[0].oldValue").value("500"))
        .andExpect(jsonPath("$[0].changedBy").value("user1"));
  }

  @Test
  void shouldGetConfigChangesAndReturnEmptyList() throws Exception {
    mockMvc.perform(get("/api/config-changes?type=CREDIT_LIMIT", ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }
}
