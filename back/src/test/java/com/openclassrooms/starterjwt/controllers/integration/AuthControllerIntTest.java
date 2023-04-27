package com.openclassrooms.starterjwt.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntTest {
    @Autowired
    private MockMvc mockMvc;
    private ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Test
    public void testAuthenticateAdmin() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.admin", is(true)))
                .andDo(print());
    }

    @Test
    public void testAuthenticateUser() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("robert@mail.com");
        loginRequest.setPassword("robert");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.admin", is(false)))
                .andDo(print());
    }

    @Test
    public void testLoginBadCredentials() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("isWrongPassword");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    // @Test
    // @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    // public void testRegisterUserOk() throws Exception {

    // SignupRequest signupRequest = new SignupRequest();

    // signupRequest.setEmail("newReguser" + UUID.randomUUID().toString() +
    // "@mail.com");
    // signupRequest.setFirstName("newUse");
    // signupRequest.setLastName("newUse");
    // signupRequest.setPassword("newUse");

    // mockMvc.perform(post("/api/auth/register")
    // .contentType(MediaType.APPLICATION_JSON)
    // .content(ow.writeValueAsString(signupRequest)))
    // .andExpect(status().isOk())
    // .andDo(print());
    // }

    @Test
    public void testRegisterUserEmailAlreadyTaken() throws Exception {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("yoga@studio.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}