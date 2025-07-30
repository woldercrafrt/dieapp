package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

@Configuration
public class OAuth2Config {
    
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(microsoftClientRegistration());
    }
    
    private ClientRegistration microsoftClientRegistration() {
        return ClientRegistration.withRegistrationId("microsoft")
                .clientId("afb20ab6-0235-4005-8b69-d3865c557f10")
                .clientSecret("RXn8Q~LwUpf2Pdfk2kFOt-BRkoz9gIOuYyb9vcrl")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("openid", "profile", "email")
                .authorizationUri("https://login.microsoftonline.com/4a0dd85f-78db-42f9-8bcf-693f3bb34580/oauth2/v2.0/authorize")
                .tokenUri("https://login.microsoftonline.com/4a0dd85f-78db-42f9-8bcf-693f3bb34580/oauth2/v2.0/token")
                .userInfoUri("https://graph.microsoft.com/v1.0/me")
                .userNameAttributeName("id")
                .jwkSetUri("https://login.microsoftonline.com/4a0dd85f-78db-42f9-8bcf-693f3bb34580/discovery/v2.0/keys")
                .clientName("Microsoft")
                .build();
    }
} 
