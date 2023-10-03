package com.openclassrooms.starterjwt.controllers.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntTests {

    @Autowired
    private MockMvc mockMvc;

    // Test for retrieving a user by ID (successful)
    @Test
    @WithMockUser(roles = "USER")
    public void userFindByIdOk() throws Exception {
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andReturn();
    }

    // Test for not finding a user by ID (not found)
    @Test
    @WithMockUser(roles = "ADMIN")
    public void userFindByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/user/0"))
                .andExpect(status().isNotFound());

    }

    // Test bad request for retrieving a user with a non-numeric ID
    @Test
    @WithMockUser(roles = "ADMIN")
    public void userFindByIdBadRequest() throws Exception {
        mockMvc.perform(get("/api/user/notanumber"))
                .andExpect(status().isBadRequest());

    }

    // Test for deleting a user by ID (successful)
    // Commented out because it deletes the user from the database
    // @Test
    // @WithMockUser(roles = "ADMIN")
    // public void userDeleteOk() throws Exception {
    // mockMvc.perform(delete("/api/user/1"))
    // .andExpect(status().isOk());
    // }

    // Test for deleting a user with a non-numeric ID (bad request)
    @Test
    @WithMockUser(roles = "ADMIN")
    public void userDeleteBadRequest() throws Exception {
        mockMvc.perform(delete("/api/user/notgoodId"))
                .andExpect(status().isBadRequest());
    }

    // Test for unauthorized user deletion (should return unauthorized status)
    @Test
    public void userDeleteUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
