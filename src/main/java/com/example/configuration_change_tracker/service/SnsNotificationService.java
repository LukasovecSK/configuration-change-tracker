package com.example.configuration_change_tracker.service;

import com.example.configuration_change_tracker.enums.ConfigChangeTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SnsNotificationService {

  private final MetricsService metricsService;

  public SnsNotificationService(MetricsService metricsService) {
    this.metricsService = metricsService;
  }

  private static final Logger logger = LoggerFactory.getLogger(SnsNotificationService.class);

  @Async
  public void publishCriticalChange(String configChangeId, ConfigChangeTypeEnum type, String newValue, String changedBy) {
    String message = "SNS Message -> configChangeId=%s, type=%s, newValue=%s, changedBy=%s".formatted(
        configChangeId, type, newValue, changedBy);

    try {
      logger.info("Simulated SNS publish: {}", message);
    } catch (Exception e) {
      metricsService.incrementSnsPublishFailed();
      logger.error("Failed to publish SNS message for configChangeId={}", configChangeId, e);
    }
  }
}
