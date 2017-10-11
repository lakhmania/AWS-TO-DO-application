package com.csye6225.demo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private BasicAuthEntryPoint basicAuthEntryPoint;

  @Autowired
  private UserAccountService userAccountService;

  protected void configure(HttpSecurity http) throws Exception {
      http
              .csrf()
              .disable()
              .authorizeRequests()
              .antMatchers("/").authenticated()
              .antMatchers("/user/register").permitAll()
              .anyRequest().authenticated()
              .and()
              .httpBasic()
              .authenticationEntryPoint(basicAuthEntryPoint);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth)
          throws Exception {
    auth.authenticationProvider(authenticationProvider());
  }

  @Bean
  public HttpSessionStrategy httpSessionStrategy() {

    return new HeaderHttpSessionStrategy();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider
            = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userAccountService);
    authProvider.setPasswordEncoder(encoder());
    return authProvider;
  }

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder(11);
  }

}
