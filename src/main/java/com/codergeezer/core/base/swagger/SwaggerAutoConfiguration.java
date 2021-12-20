package com.codergeezer.core.base.swagger;

import com.google.common.base.Predicates;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty("core.swagger.enabled")
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerAutoConfiguration {

    @Bean
    @ConditionalOnProperty("core.swagger.enabled")
    @ConditionalOnMissingBean
    public Docket createDocket(final SwaggerProperties properties) {
        var docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInfo(properties));
        if (!CollectionUtils.isEmpty(properties.getGlobalOperationParameters())) {
            docket.globalOperationParameters(buildGlobalParameters(properties.getGlobalOperationParameters()));
        }
        if (properties.isAuthenticationWithOauth2()) {
            docket.securitySchemes(this.securitySchema(properties.getAuthOauth2AccessTokenUri()))
                  .securityContexts(this.securityContext());
        } else {
            docket.securityContexts(properties.getTokenInfos()
                                              .stream()
                                              .map(v -> SecurityContext.builder()
                                                                       .securityReferences(
                                                                               Collections.singletonList(
                                                                                       new SecurityReference(
                                                                                               v.getName(),
                                                                                               new AuthorizationScope[0])))
                                                                       .build())
                                              .collect(Collectors.toList()))
                  .securitySchemes(properties.getTokenInfos().stream()
                                             .map(v -> new ApiKey(v.getName(), v.getKeyName(), v.getPassAs()))
                                             .collect(Collectors.toList()));
        }
        return docket.select()
                     .apis(RequestHandlerSelectors.basePackage(properties.getBasePackage()))
                     .paths(PathSelectors.any())
                     .paths(Predicates.not(PathSelectors.regex(properties.getExcludePath())))
                     .build();
    }

    private ApiInfo buildApiInfo(SwaggerProperties properties) {
        return new ApiInfoBuilder().title(properties.getTitle())
                                   .description(properties.getDescription())
                                   .version(properties.getVersion())
                                   .license(properties.getLicense())
                                   .licenseUrl(properties.getLicenseUrl())
                                   .contact(new Contact(properties.getContactName(),
                                                        properties.getContactUrl(),
                                                        properties.getContactEmail()))
                                   .termsOfServiceUrl(properties.getTermsOfServiceUrl())
                                   .build();
    }

    private List<SecurityContext> securityContext() {
        return Collections.singletonList(SecurityContext.builder().securityReferences(this.defaultAuth()).build());
    }

    private List<SecurityReference> defaultAuth() {
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[0];
        return Collections.singletonList(new SecurityReference("oauth2", authorizationScopes));
    }

    private List<OAuth> securitySchema(final String authOauth2AccessTokenUri) {
        final List<AuthorizationScope> authorizationScopeList = new ArrayList<>();
        final List<GrantType> grantTypes = new ArrayList<>();
        final var clientCredentialsGrant = new ClientCredentialsGrant(authOauth2AccessTokenUri);
        grantTypes.add(clientCredentialsGrant);
        return Collections.singletonList(new OAuth("oauth2", authorizationScopeList, grantTypes));
    }

    private List<Parameter> buildGlobalParameters(List<GlobalOperationParameter> globalOperationParameters) {
        final List<Parameter> parameters = new ArrayList<>();
        for (final GlobalOperationParameter globalOperationParameter : globalOperationParameters) {
            parameters.add(new ParameterBuilder().name(globalOperationParameter.getName())
                                                 .description(globalOperationParameter.getDescription())
                                                 .modelRef(new ModelRef(globalOperationParameter.getModelRef()))
                                                 .parameterType(globalOperationParameter.getParameterType())
                                                 .required(globalOperationParameter.isRequired())
                                                 .build());
        }
        return parameters;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = {"swagger.enabled", "swagger.authentication-with-oauth2"})
    public SecurityConfiguration security(SwaggerProperties swaggerProperties) {
        return SecurityConfigurationBuilder.builder()
                                           .clientId(swaggerProperties.getAuthOauth2DefaultClientId())
                                           .clientSecret(swaggerProperties.getAuthOauth2DefaultClientSecret())
                                           .useBasicAuthenticationWithAccessCodeGrant(Boolean.TRUE)
                                           .build();
    }
}
