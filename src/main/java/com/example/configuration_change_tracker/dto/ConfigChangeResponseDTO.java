package com.example.configuration_change_tracker.dto;

import com.example.configuration_change_tracker.enums.ConfigChangeTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class ConfigChangeResponseDTO {

  private String id;

  private ConfigChangeTypeEnum type;

  private String oldValue;

  private String newValue;

  private boolean critical;

  private LocalDateTime changedAt;

  private String changedBy;
}
