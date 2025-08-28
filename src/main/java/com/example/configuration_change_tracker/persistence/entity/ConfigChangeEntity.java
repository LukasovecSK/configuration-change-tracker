package com.example.configuration_change_tracker.persistence.entity;

import com.example.configuration_change_tracker.enums.ConfigChangeTypeEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CONFIG_CHANGE")
public class ConfigChangeEntity {

  @Id
  private String id;

  @Enumerated(EnumType.STRING)
  private ConfigChangeTypeEnum type;

  private String oldValue;

  private String newValue;

  private boolean critical;

  @UpdateTimestamp
  private LocalDateTime changedAt;

  private String changedBy;

}
