package com.example.configuration_change_tracker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ErrorResponseDTO {

  private String message;
  private List<String> details;
}
