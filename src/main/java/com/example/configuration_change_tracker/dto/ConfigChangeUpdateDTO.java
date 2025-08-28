package com.example.configuration_change_tracker.dto;

import com.example.configuration_change_tracker.enums.ConfigChangeTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ConfigChangeUpdateDTO {

  @NotNull(message = "Must be provided")
  private String id;

  private ConfigChangeTypeEnum type;

  private String oldValue;

  private String newValue;

  private boolean critical;

  private String changedBy;

}
