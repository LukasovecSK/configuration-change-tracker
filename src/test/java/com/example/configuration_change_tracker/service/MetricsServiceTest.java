package com.example.configuration_change_tracker.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricsServiceTest {

  @InjectMocks
  private MetricsService service;

  @Mock
  private MeterRegistry meterRegistry;

  @Test
  void shouldIncrementSnsPublishFailedCounter() {
    Counter counter = mock(Counter.class);
    when(meterRegistry.counter("config_change.sns.publish.failed", "SNS")).thenReturn(counter);

    service.incrementSnsPublishFailed();

    verify(counter).increment();
  }
}
