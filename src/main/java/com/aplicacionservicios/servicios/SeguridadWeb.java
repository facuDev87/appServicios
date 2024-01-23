/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aplicacionservicios.servicios;

import com.aplicacionservicios.servicios.servicios.ProveedorServicio;
import com.aplicacionservicios.servicios.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author facun
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadWeb {

    @Autowired
    public ProveedorServicio proveedorServicio;
    
    @Autowired
    public UsuarioServicio usuarioServicio;

    @Autowired  // encriptaion de contraseña?? 
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(clienteServicio)//recibe la contraseña desde el servicio
//                .passwordEncoder(new BCryptPasswordEncoder());// devuelve la contraseña encriptada antes de persistirla
//        
//        auth.userDetailsService(proveedorServicio)
//                .passwordEncoder(new BCryptPasswordEncoder());
//        
        auth.userDetailsService(usuarioServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/admin/*").hasRole("ADMIN")
               // .requestMatchers("/citas/*").hasAnyRole("CLIENTE","PROVEEDOR", "MOD","ADMIN")
                .requestMatchers("/css/*", "/js/*", "/img/*", "/**")
                .permitAll()
                .and().formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/logincheck")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/inicio")
                .permitAll()
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
                .and().csrf()
                .disable();

        return http.build();
    }

}