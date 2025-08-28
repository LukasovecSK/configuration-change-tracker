package com.example.configuration_change_tracker.util;

import com.example.configuration_change_tracker.dto.ConfigChangeCreateDTO;
import com.example.configuration_change_tracker.dto.ConfigChangeResponseDTO;
import com.example.configuration_change_tracker.dto.ConfigChangeUpdateDTO;
import com.example.configuration_change_tracker.dto.IdResponse;
import com.example.configuration_change_tracker.enums.ConfigChangeTypeEnum;
import com.example.configuration_change_tracker.persistence.entity.ConfigChangeEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DataProvider {

  public static final String ID = "123456";
  public static final String NOT_EXISTING_ID = "non-existing-id";

  public static IdResponse createIdResponse(String id) {
    return IdResponse.builder().id(id).build();
  }

  public static ConfigChangeCreateDTO createCreateDto(boolean critical) {
    return ConfigChangeCreateDTO.builder().type(ConfigChangeTypeEnum.CREDIT_LIMIT).newValue("1000").changedBy("user1").critical(critical).build();
  }

  public static ConfigChangeUpdateDTO createUpdateDto(boolean critical) {
    return ConfigChangeUpdateDTO.builder().id(ID).type(ConfigChangeTypeEnum.CREDIT_LIMIT).newValue("2000").changedBy("user2").critical(critical)
        .build();
  }

  public static ConfigChangeResponseDTO createResponseDto(boolean critical) {
    return ConfigChangeResponseDTO.builder().id(ID).type(ConfigChangeTypeEnum.APPROVAL_POLICY).newValue("3000").changedBy("user3").critical(critical)
        .build();
  }

  public static ConfigChangeEntity createEntity(ConfigChangeTypeEnum type, String newValue, String oldValue, String changedBy, boolean critical) {
    return ConfigChangeEntity.builder().id(ID).type(type).newValue(newValue).oldValue(oldValue)
        .changedBy(changedBy).critical(critical).build();
  }
}
