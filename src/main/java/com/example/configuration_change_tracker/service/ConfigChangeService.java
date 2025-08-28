package com.example.configuration_change_tracker.service;

import com.example.configuration_change_tracker.dto.ConfigChangeCreateDTO;
import com.example.configuration_change_tracker.dto.ConfigChangeResponseDTO;
import com.example.configuration_change_tracker.dto.ConfigChangeUpdateDTO;
import com.example.configuration_change_tracker.dto.IdResponse;
import com.example.configuration_change_tracker.enums.ConfigChangeTypeEnum;
import com.example.configuration_change_tracker.mapper.ConfigChangeMapper;
import com.example.configuration_change_tracker.persistence.entity.ConfigChangeEntity;
import com.example.configuration_change_tracker.persistence.repository.ConfigChangeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.example.configuration_change_tracker.persistence.specs.ConfigChangeSpecs.*;

@Service
public class ConfigChangeService {

  private final ConfigChangeMapper mapper;
  private final ConfigChangeRepository repository;
  private final SnsNotificationService snsNotificationService;

  private static final String ENTITY_NOT_FOUND_MESSAGE = "ConfigChangeEntity with id = %s not found";

  public ConfigChangeService(ConfigChangeMapper configChangeMapper, ConfigChangeRepository repository,
      SnsNotificationService snsNotificationService) {
    this.mapper = configChangeMapper;
    this.repository = repository;
    this.snsNotificationService = snsNotificationService;
  }

  public IdResponse addConfigChange(ConfigChangeCreateDTO configChangeCreateDTO) {
    var configChangeEntityToBeSaved = mapper.toEntity(configChangeCreateDTO);
    configChangeEntityToBeSaved.setId(UUID.randomUUID().toString());

    var savedEntity = repository.save(configChangeEntityToBeSaved);

    if (savedEntity.isCritical()) {
      snsNotificationService.publishCriticalChange(savedEntity.getId(), savedEntity.getType(), savedEntity.getNewValue(), savedEntity.getChangedBy());
    }

    return IdResponse.builder().id(savedEntity.getId()).build();
  }

  public IdResponse editConfigChange(ConfigChangeUpdateDTO configChangeUpdateDTO) {
    var configChangeEntityToBeUpdated = repository.findById(configChangeUpdateDTO.getId())
        .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE.formatted(configChangeUpdateDTO.getId())));

    mapper.updateEntityFromDto(configChangeUpdateDTO, configChangeEntityToBeUpdated);

    var updatedEntity = repository.save(configChangeEntityToBeUpdated);

    if (updatedEntity.isCritical()) {
      snsNotificationService.publishCriticalChange(updatedEntity.getId(), updatedEntity.getType(), updatedEntity.getNewValue(),
          updatedEntity.getChangedBy());
    }

    return IdResponse.builder().id(updatedEntity.getId()).build();
  }

  public IdResponse deleteConfigChangeById(String id) {
    if (!repository.existsById(id)) {
      throw new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE.formatted(id));
    }
    repository.deleteById(id);
    return IdResponse.builder().id(id).build();
  }

  public ConfigChangeResponseDTO getConfigChangeById(@NotBlank(message = "Id must be provided") String id) {
    var configChangeEntity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE.formatted(id)));

    return mapper.toDto(configChangeEntity);
  }

  public List<ConfigChangeResponseDTO> getConfigChanges(ConfigChangeTypeEnum type, LocalDateTime changedFrom, LocalDateTime changedUntil) {
    Specification<ConfigChangeEntity> spec = null;

    if (type != null)
      spec = hasType(type);
    if (changedFrom != null)
      spec = and(spec, changedAfter(changedFrom));
    if (changedUntil != null)
      spec = and(spec, changedBefore(changedUntil));

    var entities = spec == null ? repository.findAll() : repository.findAll(spec);
    return mapper.toListDto(entities);
  }
}
