package org.nerdcoding.example.spring.cloud.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@EnableResourceServer
@EnableAuthorizationServer
public class OAuth2ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OAuth2ServerApplication.class, args);
	}

	/**
	 * Endpoint will be called by protected microservices to validate whose `access_token` and retrive information to
	 * the authenticated user.
	 *
	 * @param user Contains information about a authenticated user.
	 * @return Information for the calling microservice about the authenticated user.
	 */
	@RequestMapping(value = "/user", produces = "application/json;charset=UTF-8")
	public Map<String, Object> user(final OAuth2Authentication user) {
		final Map<String, Object> userInfo = new HashMap<>(2);
		userInfo.put("user", user.getPrincipal());
		userInfo.put("authorities", user.getUserAuthentication().getAuthorities());
		return userInfo;
	}


}