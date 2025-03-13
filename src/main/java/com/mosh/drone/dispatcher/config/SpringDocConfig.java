package com.mosh.drone.dispatcher.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
  public static final String SCHEME_NAME = "bearerAuth";
  private static final String BEARER_FORMAT = "JWT";
  private static final String SCHEME = "bearer";

  @Value("${swagger.title:API Docs}")
  private String swaggerTitle;

  @Value("${swagger.server.url:}")
  private String swaggerServerUrl;

  @Value("${swagger.description:Description}")
  private String swaggerDescription;

  @Value("${swagger.version:v1.0.0}")
  private String swaggerVersion;

  @Bean
  public OpenAPI caseOpenAPI() {
    return new OpenAPI()
        .info(
            new Info().title(swaggerTitle).description(swaggerDescription).version(swaggerVersion))
        .addServersItem(new Server().url(swaggerServerUrl))
        .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
        .components(
            new Components()
                .addSecuritySchemes(
                    SCHEME_NAME,
                    new SecurityScheme()
                        .name(SCHEME_NAME)
                        .type(SecurityScheme.Type.HTTP)
                        .bearerFormat(BEARER_FORMAT)
                        .in(SecurityScheme.In.HEADER)
                        .scheme(SCHEME)));
  }

  @Bean
  public OperationCustomizer customize() {
    return (operation, handlerMethod) -> {
      operation.addParametersItem(
          new Parameter().in("header").description("client-id").name("client-id"));

      return operation;
    };
  }
}
