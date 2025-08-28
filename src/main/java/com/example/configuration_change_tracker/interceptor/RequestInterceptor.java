package com.example.configuration_change_tracker.interceptor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestInterceptor extends OncePerRequestFilter {

  private static final String CORRELATION_ID = "correlationId";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException, jakarta.servlet.ServletException {

    String correlationId = request.getHeader(CORRELATION_ID);
    if (correlationId == null || correlationId.isBlank()) {
      correlationId = UUID.randomUUID().toString();
    }

    MDC.put(CORRELATION_ID, correlationId);

    try {
      filterChain.doFilter(request, response);
    } finally {
      MDC.remove(CORRELATION_ID);
    }
  }
}
