package com.example.configuration_change_tracker.persistence.repository;

import com.example.configuration_change_tracker.persistence.entity.ConfigChangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigChangeRepository extends JpaRepository<ConfigChangeEntity, String>, JpaSpecificationExecutor<ConfigChangeEntity> {
}
