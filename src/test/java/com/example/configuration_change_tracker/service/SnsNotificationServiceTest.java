package com.example.configuration_change_tracker.service;

import com.example.configuration_change_tracker.enums.ConfigChangeTypeEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SnsNotificationServiceTest {

  @InjectMocks
  private SnsNotificationService service;

  @Mock
  private MetricsService metricsService;

  @Test
  void shouldPublishCriticalChangeAndNotThrowException() {
    Assertions.assertDoesNotThrow(() -> service.publishCriticalChange("id", ConfigChangeTypeEnum.CREDIT_LIMIT, "1000", "user1"));
    verify(metricsService, times(0)).incrementSnsPublishFailed();
  }
}
