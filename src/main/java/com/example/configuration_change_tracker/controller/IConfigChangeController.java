package com.example.configuration_change_tracker.controller;

import com.example.configuration_change_tracker.dto.ConfigChangeCreateDTO;
import com.example.configuration_change_tracker.dto.ConfigChangeResponseDTO;
import com.example.configuration_change_tracker.dto.ConfigChangeUpdateDTO;
import com.example.configuration_change_tracker.dto.IdResponse;
import com.example.configuration_change_tracker.enums.ConfigChangeTypeEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/config-changes")
public interface IConfigChangeController {

  @PostMapping(consumes = {"application/json"})
  ResponseEntity<IdResponse> addConfigChange(@Valid @RequestBody ConfigChangeCreateDTO configChangeCreateDTO);

  @PutMapping(consumes = {"application/json"})
  ResponseEntity<IdResponse> editConfigChange(@Valid @RequestBody ConfigChangeUpdateDTO configChangeUpdateDTO);

  @DeleteMapping(value = "/{id}")
  ResponseEntity<IdResponse> deleteConfigChangeById(@PathVariable("id") @NotBlank(message = "Id must be provided") String id);

  @GetMapping(value = "/{id}")
  ResponseEntity<ConfigChangeResponseDTO> getConfigChangeById(@PathVariable("id") @NotBlank(message = "Id must be provided") String id);

  @GetMapping(produces = {"application/json"})
  ResponseEntity<List<ConfigChangeResponseDTO>> getConfigChanges(@RequestParam(required = false) ConfigChangeTypeEnum type,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime changedFrom,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime changedUntil);

}
