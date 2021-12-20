package com.codergeezer.core.base.config;

import java.io.IOException;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * @author haidv
 * @version 1.0
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

  @Value("${cors.config.allow.origin:*}")
  private String allowOrigins;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletResponse response = (HttpServletResponse) res;
    HttpServletRequest request = (HttpServletRequest) req;
    List<String> str = List.of(allowOrigins.split(","));
    var origin = request.getHeader(HttpHeaders.ORIGIN);
    if (allowOrigins.equals("*")) {
      response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
    }
    if (origin != null && str.contains(origin)) {
      response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
    }
    response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
    response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "PUT, POST, GET, OPTIONS, DELETE, HEAD");
    response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
    response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
    response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
    chain.doFilter(req, res);
  }

  @Override
  public void destroy() {}
}
