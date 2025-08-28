package com.example.configuration_change_tracker.service;

import com.example.configuration_change_tracker.dto.ConfigChangeResponseDTO;
import com.example.configuration_change_tracker.dto.ConfigChangeUpdateDTO;
import com.example.configuration_change_tracker.enums.ConfigChangeTypeEnum;
import com.example.configuration_change_tracker.mapper.ConfigChangeMapper;
import com.example.configuration_change_tracker.persistence.repository.ConfigChangeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.configuration_change_tracker.util.DataProvider.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfigChangeServiceTest {

  @InjectMocks
  private ConfigChangeService service;

  @Mock
  private ConfigChangeMapper mapper;

  @Mock
  private ConfigChangeRepository repository;

  @Mock
  private SnsNotificationService snsNotificationService;

  @Test
  void shouldSaveConfigChangeAndReturnId() {
    var createDto = createCreateDto(false);
    var entity =
        createEntity(createDto.getType(), createDto.getNewValue(), createDto.getOldValue(), createDto.getChangedBy(), createDto.isCritical());

    when(mapper.toEntity(createDto)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(entity);

    var idResponse = service.addConfigChange(createDto);

    verify(repository).save(entity);
    verify(snsNotificationService, times(0)).publishCriticalChange(idResponse.getId(), entity.getType(), entity.getNewValue(), entity.getChangedBy());
  }

  @Test
  void shouldSaveCriticalConfigChangeAndCallSns() {
    var createDto = createCreateDto(true);
    var entity =
        createEntity(createDto.getType(), createDto.getNewValue(), createDto.getOldValue(), createDto.getChangedBy(), createDto.isCritical());

    when(mapper.toEntity(createDto)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(entity);

    var idResponse = service.addConfigChange(createDto);

    verify(repository).save(entity);
    verify(snsNotificationService).publishCriticalChange(idResponse.getId(), entity.getType(), entity.getNewValue(), entity.getChangedBy());
  }

  @Test
  void shouldUpdateConfigChangeAndReturnId() {
    var updateDto = createUpdateDto(false);
    var entity =
        createEntity(updateDto.getType(), updateDto.getNewValue(), updateDto.getOldValue(), updateDto.getChangedBy(), updateDto.isCritical());

    when(repository.findById(ID)).thenReturn(Optional.of(entity));
    when(repository.save(entity)).thenReturn(entity);

    var idResponse = service.editConfigChange(updateDto);

    assertEquals(ID, idResponse.getId());
    verify(mapper).updateEntityFromDto(updateDto, entity);
    verify(repository).save(entity);
    verify(snsNotificationService, times(0)).publishCriticalChange(idResponse.getId(), entity.getType(), entity.getNewValue(), entity.getChangedBy());
  }

  @Test
  void shouldThrowIfEntityNotFoundWhenUpdate() {
    var dto = ConfigChangeUpdateDTO.builder().id(NOT_EXISTING_ID).build();
    when(repository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.editConfigChange(dto));
  }

  @Test
  void shouldUpdateCriticalConfigChangeAndCallSns() {
    var updateDto = createUpdateDto(true);
    var entity =
        createEntity(updateDto.getType(), updateDto.getNewValue(), updateDto.getOldValue(), updateDto.getChangedBy(), updateDto.isCritical());

    when(repository.findById(ID)).thenReturn(Optional.of(entity));
    when(repository.save(entity)).thenReturn(entity);

    var idResponse = service.editConfigChange(updateDto);

    assertEquals(ID, idResponse.getId());
    verify(mapper).updateEntityFromDto(updateDto, entity);
    verify(repository).save(entity);
    verify(snsNotificationService).publishCriticalChange(idResponse.getId(), entity.getType(), entity.getNewValue(), entity.getChangedBy());
  }

  @Test
  void shouldDeleteConfigChangeByIdAndReturnId() {
    when(repository.existsById(ID)).thenReturn(true);

    var idResponse = service.deleteConfigChangeById(ID);

    assertEquals(ID, idResponse.getId());
    verify(repository).deleteById(ID);
  }

  @Test
  void shouldThrowIfEntityNotFoundWhenDelete() {
    when(repository.existsById(NOT_EXISTING_ID)).thenReturn(false);

    assertThrows(EntityNotFoundException.class, () -> service.deleteConfigChangeById(NOT_EXISTING_ID));
  }

  @Test
  void shouldGetConfigChangeByIdAndReturnDTO() {
    when(repository.findById(ID)).thenReturn(Optional.of(createEntity(ConfigChangeTypeEnum.CREDIT_LIMIT, "1000", "500", "user1", false)));

    when(mapper.toDto(any())).thenReturn(ConfigChangeResponseDTO.builder().id(ID).build());
    var responseDTO = service.getConfigChangeById(ID);

    assertEquals(ID, responseDTO.getId());
    verify(repository).findById(ID);
  }

  @Test
  void shouldThrowIfEntityNotFoundWhenGet() {
    when(repository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.getConfigChangeById(NOT_EXISTING_ID));
  }

  @Test
  void shouldGetConfigChangesWhenAllParamsPresentAndReturnDTOs() {
    when(repository.findAll(any(Specification.class))).thenReturn(
        List.of(createEntity(ConfigChangeTypeEnum.CREDIT_LIMIT, "1000", "500", "user1", false)));
    when(mapper.toListDto(any())).thenReturn(List.of(ConfigChangeResponseDTO.builder().id(ID).build()));

    var configChanges =
        service.getConfigChanges(ConfigChangeTypeEnum.CREDIT_LIMIT, LocalDateTime.now().minusDays(1), LocalDateTime.now());

    assertEquals(1, configChanges.size());
    verify(repository).findAll(any(Specification.class));
    verify(mapper).toListDto(any());
  }

  @Test
  void shouldGetConfigChangesWhenAllParamsNullAndReturnDTOs() {
    when(repository.findAll()).thenReturn(List.of(createEntity(ConfigChangeTypeEnum.CREDIT_LIMIT, "1000", "500", "user1", false)));
    when(mapper.toListDto(any())).thenReturn(List.of(ConfigChangeResponseDTO.builder().id(ID).build()));

    var configChanges =
        service.getConfigChanges(null, null, null);

    assertEquals(1, configChanges.size());
    verify(repository).findAll();
    verify(mapper).toListDto(any());
  }
}
