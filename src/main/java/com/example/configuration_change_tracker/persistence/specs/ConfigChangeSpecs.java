package com.example.configuration_change_tracker.persistence.specs;

import com.example.configuration_change_tracker.enums.ConfigChangeTypeEnum;
import com.example.configuration_change_tracker.persistence.entity.ConfigChangeEntity;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

@UtilityClass
public class ConfigChangeSpecs {

  public static Specification<ConfigChangeEntity> hasType(ConfigChangeTypeEnum type) {
    return (root, query, cb) -> type == null ? null : cb.equal(root.get("type"), type);
  }

  public static Specification<ConfigChangeEntity> changedAfter(LocalDateTime from) {
    return (root, query, cb) -> from == null ? null : cb.greaterThanOrEqualTo(root.get("changedAt"), from);
  }

  public static Specification<ConfigChangeEntity> changedBefore(LocalDateTime until) {
    return (root, query, cb) -> until == null ? null : cb.lessThanOrEqualTo(root.get("changedAt"), until);
  }

  public static Specification<ConfigChangeEntity> and(Specification<ConfigChangeEntity> spec1, Specification<ConfigChangeEntity> spec2) {
    if (spec1 == null)
      return spec2;
    if (spec2 == null)
      return spec1;
    return spec1.and(spec2);
  }
}
