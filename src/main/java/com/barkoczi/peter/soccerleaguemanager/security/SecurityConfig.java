package com.barkoczi.peter.soccerleaguemanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenServices jwtTokenServices;

    public SecurityConfig(JwtTokenServices jwtTokenServices) {
        this.jwtTokenServices = jwtTokenServices;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
//                    .antMatchers("/auth/signin").permitAll()
//                    .antMatchers("/auth/signup").permitAll()
//                    .antMatchers("/auth/getUsers").permitAll()
//                    .antMatchers("/teams/all").permitAll()
//                    .antMatchers("/users").hasRole("admin")
                    .antMatchers("/**").permitAll()
//                    .antMatchers(HttpMethod.PATCH, "/match/**").hasAnyRole("admin", "referee")
//                    .antMatchers(HttpMethod.PATCH, "/update_user").hasRole("admin")
//                    .antMatchers(HttpMethod.GET, "/vehicles/**").authenticated()
//                    .antMatchers(HttpMethod.DELETE, "/delete_user/*").hasRole("admin")
//                    .antMatchers(HttpMethod.GET, "/me").authenticated()
                    .anyRequest().denyAll()
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenServices), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
