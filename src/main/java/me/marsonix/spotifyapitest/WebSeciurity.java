package me.marsonix.spotifyapitest;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Sso
public class WebSeciurity extends WebSecurityConfigurerAdapter {



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http .csrf().disable() .authorizeRequests() .anyRequest().permitAll();
        http.authorizeRequests().antMatchers("**").authenticated();
      //  http.anonymous().disable().csrf().disable().oauth2Login().authorizationEndpoint();
    }
}
