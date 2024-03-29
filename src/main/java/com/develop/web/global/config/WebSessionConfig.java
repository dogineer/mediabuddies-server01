package com.develop.web.global.config;

import com.develop.web.global.filter.session.AdminPageRequestRankFilter;
import com.develop.web.global.filter.session.PageRequestAuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "authentication.version", havingValue = "session")
@EnableWebSecurity
public class WebSessionConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("[+] SESSION version WebSecurityConfig Setup ");

        http.csrf().disable()
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .headers().frameOptions().disable();
    }

    @Bean
    public FilterRegistrationBean<PageRequestAuthFilter> pageRequestAuthFilterRegistrationBean() {
        FilterRegistrationBean<PageRequestAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new PageRequestAuthFilter());
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        registrationBean.addUrlPatterns("/admin/*", "/service/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AdminPageRequestRankFilter> adminPageRequestRankFilterRegistrationBean() {
        FilterRegistrationBean<AdminPageRequestRankFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AdminPageRequestRankFilter());
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        registrationBean.addUrlPatterns("/admin/*");
        return registrationBean;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Value("${CNPS.MC.URL}")
    private String mc;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        log.info("[+] CorsConfiguration Setup ");

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
