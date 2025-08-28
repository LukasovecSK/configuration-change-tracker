package com.example.configuration_change_tracker.mapper;

import com.example.configuration_change_tracker.enums.ConfigChangeTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.configuration_change_tracker.util.DataProvider.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ConfigChangeMapperTest {

  private final ConfigChangeMapper mapper = Mappers.getMapper(ConfigChangeMapper.class);

  @Test
  void shouldMapToDTO() {
    var entity = createEntity(ConfigChangeTypeEnum.CREDIT_LIMIT, "1000", "500", "user1", true);

    var dto = mapper.toDto(entity);

    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getType(), dto.getType());
    assertEquals(entity.getNewValue(), dto.getNewValue());
    assertEquals(entity.getOldValue(), dto.getOldValue());
    assertEquals(entity.getChangedBy(), dto.getChangedBy());
    assertEquals(entity.getChangedAt(), dto.getChangedAt());
  }

  @Test
  void shouldMapToEntity() {
    var dto = createCreateDto(true);

    var entity = mapper.toEntity(dto);

    assertEquals(dto.getType(), entity.getType());
    assertEquals(dto.getNewValue(), entity.getNewValue());
    assertEquals(dto.getOldValue(), entity.getOldValue());
    assertEquals(dto.getChangedBy(), entity.getChangedBy());
  }

  @Test
  void shouldUpdateEntityFromDto() {
    var dto = createUpdateDto(true);
    var entity = createEntity(ConfigChangeTypeEnum.APPROVAL_POLICY, "newValue", "oldValue", "user2", false);

    mapper.updateEntityFromDto(dto, entity);

    assertEquals(ID, entity.getId());
    assertEquals(dto.getType(), entity.getType());
    assertEquals(dto.getNewValue(), entity.getNewValue());
    assertEquals(dto.getOldValue(), entity.getOldValue());
    assertEquals(dto.getChangedBy(), entity.getChangedBy());
  }
}
