package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;

import static org.mockito.Mockito.*;

class WebSecurityConfigTest {

    @Mock
    UserDetailsServiceImpl userDetailsService;

    @Mock
    AuthEntryPointJwt unauthorizedHandler;

    @Mock
    AuthenticationManagerBuilder authManagerBuilder;



    @InjectMocks
    WebSecurityConfig config;


    @Test
    void testConfigureAuthenticationManagerBuilder() throws Exception {

        AuthenticationManagerBuilder authManagerBuilder = mock(AuthenticationManagerBuilder.class);
        DaoAuthenticationConfigurer daoConfigurer = mock(DaoAuthenticationConfigurer.class);
        UserDetailsServiceImpl userDetailsService = mock(UserDetailsServiceImpl.class);


        when(authManagerBuilder.userDetailsService(userDetailsService)).thenReturn(daoConfigurer);
        when(daoConfigurer.passwordEncoder(any())).thenReturn(daoConfigurer);


        WebSecurityConfig config = new WebSecurityConfig();
        java.lang.reflect.Field udsField = WebSecurityConfig.class.getDeclaredField("userDetailsService");
        udsField.setAccessible(true);
        udsField.set(config, userDetailsService);


        config.configure(authManagerBuilder);


        verify(authManagerBuilder).userDetailsService(userDetailsService);
        verify(daoConfigurer).passwordEncoder(any());
    }
}