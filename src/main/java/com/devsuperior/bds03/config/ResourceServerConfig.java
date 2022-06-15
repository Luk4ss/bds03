package com.devsuperior.bds03.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{
	
	// Objeto do spring que representa o ambiente de execução da aplicação.
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JwtTokenStore tokenStore;
	
	private static final String[] PUBLIC = {"/ouath/token", "/h2-console/**"};
	
	private static final String[] OPERATOR_GET = {"/departments/**", "/employess/**"};
	


	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		// Configuração para liberar o H2
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		http.authorizeRequests()
		.antMatchers(PUBLIC).permitAll()
		.antMatchers(HttpMethod.GET, OPERATOR_GET).hasAnyRole("OPERATOR", "ADMIN")
		.anyRequest().hasRole("ADMIN");
	}
	
	
}
