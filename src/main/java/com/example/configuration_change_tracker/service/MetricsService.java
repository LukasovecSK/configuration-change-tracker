package com.example.configuration_change_tracker.service;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

  private final MeterRegistry meterRegistry;

  public MetricsService(MeterRegistry meterRegistry) {
    this.meterRegistry = meterRegistry;
  }

  public void incrementSnsPublishFailed() {
    meterRegistry.counter("config_change.sns.publish.failed", "SNS").increment();
  }
}
