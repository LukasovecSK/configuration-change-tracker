package com.example.configuration_change_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ConfigChangeTrackerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConfigChangeTrackerApplication.class, args);
  }

}
