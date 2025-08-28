package com.example.configuration_change_tracker.controller;

import com.example.configuration_change_tracker.dto.ConfigChangeCreateDTO;
import com.example.configuration_change_tracker.dto.ConfigChangeResponseDTO;
import com.example.configuration_change_tracker.dto.ConfigChangeUpdateDTO;
import com.example.configuration_change_tracker.dto.IdResponse;
import com.example.configuration_change_tracker.enums.ConfigChangeTypeEnum;
import com.example.configuration_change_tracker.service.ConfigChangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
public class ConfigChangeController implements IConfigChangeController {

  private final ConfigChangeService service;

  public ConfigChangeController(ConfigChangeService service) {
    this.service = service;
  }

  private static final Logger logger = LoggerFactory.getLogger(ConfigChangeController.class);

  @Override
  public ResponseEntity<IdResponse> addConfigChange(ConfigChangeCreateDTO configChangeCreateDTO) {
    logger.info("Incoming request: POST /api/config-changes, body={}", configChangeCreateDTO);

    var idResponse = service.addConfigChange(configChangeCreateDTO);

    logger.info("Successfully added config change with id={}", idResponse.getId());

    return ResponseEntity.status(201).body(idResponse);
  }

  @Override
  public ResponseEntity<IdResponse> editConfigChange(ConfigChangeUpdateDTO configChangeUpdateDTO) {
    logger.info("Incoming request: PUT /api/config-changes, body={}", configChangeUpdateDTO);

    var idResponse = service.editConfigChange(configChangeUpdateDTO);

    logger.info("Successfully updated config change with id={}", idResponse.getId());

    return ResponseEntity.ok().body(idResponse);
  }

  @Override
  public ResponseEntity<IdResponse> deleteConfigChangeById(String id) {
    logger.info("Incoming request: DELETE /api/config-changes/{}", id);

    var idResponse = service.deleteConfigChangeById(id);

    logger.info("Successfully deleted config change with id={}", id);

    return ResponseEntity.ok().body(idResponse);
  }

  @Override
  public ResponseEntity<ConfigChangeResponseDTO> getConfigChangeById(String id) {
    logger.info("Incoming request: GET /api/config-changes/{}", id);

    var configChange = service.getConfigChangeById(id);

    logger.info("Successfully returned config change with id={}, body={}", id, configChange);

    return ResponseEntity.ok().body(configChange);
  }

  @Override
  public ResponseEntity<List<ConfigChangeResponseDTO>> getConfigChanges(ConfigChangeTypeEnum type, LocalDateTime changedFrom,
      LocalDateTime changedUntil) {
    logger.info("Incoming request: GET /api/config-changes?type={}&changedFrom={}&changedUntil={}", type, changedFrom, changedUntil);

    var configChanges = service.getConfigChanges(type, changedFrom, changedUntil);

    logger.info("Successfully returned {} config changes with ids={}", configChanges.size(),
        configChanges.stream().map(ConfigChangeResponseDTO::getId).toList());

    return ResponseEntity.ok().body(configChanges);
  }
}
