package com.example.configuration_change_tracker.dto;

import com.example.configuration_change_tracker.enums.ConfigChangeTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ConfigChangeCreateDTO {

  @NotNull(message = "Must be provided")
  private ConfigChangeTypeEnum type;

  private String oldValue;

  @NotNull(message = "Must be provided")
  private String newValue;

  private boolean critical;

  @NotBlank(message = "Must be provided")
  private String changedBy;

}
