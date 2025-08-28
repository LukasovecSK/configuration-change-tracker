package com.example.configuration_change_tracker.mapper;

import com.example.configuration_change_tracker.dto.ConfigChangeCreateDTO;
import com.example.configuration_change_tracker.dto.ConfigChangeResponseDTO;
import com.example.configuration_change_tracker.dto.ConfigChangeUpdateDTO;
import com.example.configuration_change_tracker.persistence.entity.ConfigChangeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConfigChangeMapper {

  List<ConfigChangeResponseDTO> toListDto(List<ConfigChangeEntity> configChangeEntities);

  ConfigChangeResponseDTO toDto(ConfigChangeEntity entity);

  ConfigChangeEntity toEntity(ConfigChangeCreateDTO dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "changedAt", ignore = true)
  void updateEntityFromDto(ConfigChangeUpdateDTO dto, @MappingTarget ConfigChangeEntity entity);
}
