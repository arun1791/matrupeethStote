package com.matrupeeth.store.config;

import com.matrupeeth.store.security.JwtAuthenticationFilter;
import com.matrupeeth.store.security.JwtAuthinciateEntrypoint;
import com.matrupeeth.store.services.Impl.CustomeUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.persistence.Column;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    private JwtAuthinciateEntrypoint jwtAuthinciateEntrypoint;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

//    @Bean
//    public UserDetailsService userDetailsService() {
//        //user create
//        UserDetails normalUser = User.builder()
//                .username("Arun")
//                .password(passwordEncoder().encode("normal"))
//                .roles("NORMAL")
////                .passwordEncoder(passwordEncoder())
//                .build();
//        UserDetails adminUser = User.builder()
//                .username("Neha")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(normalUser, adminUser);
//    }
    @Autowired
    private UserDetailsService userDetailsService ;
    private final String [] PUBLIC_URLS={
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs",
            "/v2/api-docs"

    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


//        httpSecurity
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .formLogin()
//                .loginPage("login.html")
//                .loginProcessingUrl("/process-url")
//                .defaultSuccessUrl("/dashborad")
//                .failureUrl("error")
//                .and()
//                .logout()
//                .logoutUrl("/logout");
        httpSecurity
                .csrf()
                .disable()
//                .cors()
//                .and()
//                .disable()
                .authorizeRequests()
                .antMatchers("/auth/login")
                .permitAll()
                .antMatchers("/auth/google")
                .permitAll()
                .antMatchers(HttpMethod.POST,"/user/")
                .permitAll()
                .antMatchers(HttpMethod.DELETE,"/user/**").hasRole("ADMIN")
                .antMatchers(PUBLIC_URLS)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthinciateEntrypoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return  httpSecurity.build();


    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return  daoAuthenticationProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager    authenticationManager(AuthenticationConfiguration builder) throws Exception {
    return  builder.getAuthenticationManager();
    }

    @Bean
    public FilterRegistrationBean corsFilter() throws Exception
    {
        UrlBasedCorsConfigurationSource source =new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
//        configuration.setAllowedOrigins();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader("Accept");
        configuration.addAllowedHeader("GET");
        configuration.addAllowedHeader("POST");
        configuration.addAllowedHeader("PUT");
        configuration.addAllowedHeader("DELETE");
        configuration.addAllowedHeader("OPTIONS");
        configuration.setMaxAge(3600L);

        source.registerCorsConfiguration("/**",configuration);
        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean(new CorsFilter(source));
        filterRegistrationBean.setOrder(-1);
        return  filterRegistrationBean;
    }
}