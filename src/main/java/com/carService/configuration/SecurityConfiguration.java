package com.carService.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .requestMatchers("/appointment").authenticated()
                .requestMatchers("/myAppointments").authenticated()
                .requestMatchers("/cancelAppointment").authenticated()
                .requestMatchers("/deleteAppointment").hasAuthority("ADMIN")
                .requestMatchers("/myInvoices").authenticated()
                .requestMatchers("/addService").hasAuthority("ADMIN")
                .requestMatchers("/editService").hasAuthority("ADMIN")
                .requestMatchers("/deleteService").hasAuthority("ADMIN")
                .requestMatchers("/employees").hasAnyAuthority("ADMIN","EMPLOYEE")
                .requestMatchers("/editUser").hasAuthority("ADMIN")
                .requestMatchers("/deleteUser").hasAuthority("ADMIN")
                .requestMatchers("/addEmployee").hasAuthority("ADMIN")
                .requestMatchers("/addEmployeeToService").hasAuthority("ADMIN")
                .requestMatchers("/users").hasAuthority("ADMIN")
                .requestMatchers("/serviceAppointments").hasAnyAuthority("ADMIN","EMPLOYEE")
                .requestMatchers("/serviceInvoices").hasAnyAuthority("ADMIN","EMPLOYEE")
                .requestMatchers("/editInvoice").hasAnyAuthority("ADMIN","EMPLOYEE")
                .requestMatchers("/deleteInvoice").hasAnyAuthority("ADMIN","EMPLOYEE")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?id=error")
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll().and().cors().and().
                csrf().disable();;


        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
