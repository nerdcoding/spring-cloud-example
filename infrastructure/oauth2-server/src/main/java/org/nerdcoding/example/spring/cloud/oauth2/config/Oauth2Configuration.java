/*
 * Oauth2Configuration.java
 *
 * Copyright (c) 2018, Tobias Koltsch. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 and
 * only version 2 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-2.0.html>.
 */

package org.nerdcoding.example.spring.cloud.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * Configuration for token based authentication and authorization (see implemented {@link AuthorizationServerConfigurer}).
 */
@Configuration
public class Oauth2Configuration extends AuthorizationServerConfigurerAdapter {

    private static final String SIGNING_KEY =
            "MIICXAIBAAKBgQCzjH0f73kMe3jdE57CH2tqnYQl2uuPvpiAOnmpShExzWdSW3IGd4p68VHJXMmDI3JXF3Ar5oY8BXwKkbLdjLFn6pF" +
                    "21AY9q17TgXK/7hP71MoSu3QNtgOcmJ5PqvWFvFqP7yA0PXfKtsDqTliTTaf1ljr2GqpS6hOsFoHmmTrBaQIDAQABAoGAZF" +
                    "e1be2VhuZSS6s1ZGPO0kypl8ZbM4BfFfqYF4YvSdfzUFGOzhJsr/zBqnlSnRloQ0f0BnTUvCKMihOXFL4WPn6m9S/C4//4J" +
                    "7o6QiV/vddMu8HdP1tWF3UDoH+wrL1oQ1RrQtNlXsgj0CX7e3P5WmlNR1qXb59zDGh1wY45zOkCQQDorpoesS1dwWTzgNo79" +
                    "QW8eBI/+je9KEuNts";

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Autowired
    public Oauth2Configuration(final AuthenticationManager authenticationManager, final UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Defines the `protected resources` (microservices) registered for this OAuth2 authentication server.
     *
     * @param clients the client details configurer
     */
    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("ms-product") // key of the registered microservice
                .secret("mysecret") // password of the registered microservice
                .authorizedGrantTypes("refresh_token", "password", "client_credentials")
                .scopes("webclient", "mobileclient")
                .accessTokenValiditySeconds(360); // 5 min.

    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    /**
     * This {@link DefaultTokenServices} is configured to use JWTs as token instead of the default generated UUIDs.
     *
     * @return A {@link DefaultTokenServices} for JWTs.
     */
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGNING_KEY);
        return converter;
    }


}
