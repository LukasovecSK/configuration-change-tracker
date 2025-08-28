package com.example.configuration_change_tracker.controller;

import com.example.configuration_change_tracker.service.ConfigChangeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.configuration_change_tracker.enums.ConfigChangeTypeEnum.APPROVAL_POLICY;
import static com.example.configuration_change_tracker.util.DataProvider.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfigChangeControllerTest {

  @InjectMocks
  private ConfigChangeController controller;

  @Mock
  private ConfigChangeService configChangeService;

  @Test
  void shouldAddConfigChangeAndReturnResponseId() {
    var dto = createCreateDto(true);
    when(configChangeService.addConfigChange(dto)).thenReturn(createIdResponse(ID));

    var idResponse = controller.addConfigChange(dto);

    assertNotNull(idResponse.getBody());
    assertEquals(ID, idResponse.getBody().getId());
    verify(configChangeService).addConfigChange(dto);
  }

  @Test
  void shouldUpdateConfigChangeAndReturnResponseId() {
    var dto = createUpdateDto(true);
    when(configChangeService.editConfigChange(dto)).thenReturn(createIdResponse(ID));

    var idResponse = controller.editConfigChange(dto);

    assertNotNull(idResponse.getBody());
    assertEquals(ID, idResponse.getBody().getId());
    verify(configChangeService).editConfigChange(dto);
  }

  @Test
  void shouldDeleteConfigChangeByIdAndReturnResponseId() {
    when(configChangeService.deleteConfigChangeById(ID)).thenReturn(createIdResponse(ID));

    var idResponse = controller.deleteConfigChangeById(ID);

    assertNotNull(idResponse.getBody());
    assertEquals(ID, idResponse.getBody().getId());
    verify(configChangeService).deleteConfigChangeById(ID);
  }

  @Test
  void shouldGetConfigChangeByIdAndReturnResponseDto() {
    when(configChangeService.getConfigChangeById(ID)).thenReturn(createResponseDto(true));

    var response = controller.getConfigChangeById(ID);

    assertNotNull(response.getBody());
    assertEquals(ID, response.getBody().getId());
    verify(configChangeService).getConfigChangeById(ID);
  }

  @Test
  void shouldGetConfigChangesAndReturnResponseDtoList() {
    when(configChangeService.getConfigChanges(APPROVAL_POLICY, null, null)).thenReturn(List.of(createResponseDto(true)));

    var response = controller.getConfigChanges(APPROVAL_POLICY, null, null);

    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals(ID, response.getBody().getFirst().getId());
    verify(configChangeService).getConfigChanges(APPROVAL_POLICY, null, null);
  }
}
