package com.openclassrooms.starterjwt.controllers.unit;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private UserController userController;

    // Test to find a user by ID
    @Test
    public void userFindByIdOk() {
        User user = new User();

        // define the call to the userService and the userMapper to return a userDto
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(new UserDto());

        // call the findById method of the userController with the id
        ResponseEntity<?> responseEntity = userController.findById(String.valueOf(1L));

        // verify that the response status code is 200
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    // Test to not find a user by inexistent ID
    @Test
    public void userFindByIdNotFound() {
        // define the findById method of the userService that returns null
        when(userService.findById(0L)).thenReturn(null);

        // call the findById method of the userController with the id
        ResponseEntity<?> responseEntity = userController.findById(String.valueOf(0L));

        // verify that the response status code is 404
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    // Test to not find a user by ID with a non-numeric ID
    @Test
    public void userFindByIdBadRequest() {
        // call the findById method of the userController with the invalidId
        ResponseEntity<?> responseEntity = userController.findById("notanumber");
        // verify that the response status code is 400
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    // Test to delete a user with a valid ID and an authorized user
    @Test
    public void deleteUserOk() {
        long id = 1L;
        // create a user
        User user = new User();
        user.setEmail("test@example.com");
        // create a list of authorities
        List<GrantedAuthority> authorities = new ArrayList<>();
        // add the role ROLE_USER to the list of authorities
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // define the behavior of the userService and the userMapper
        when(userService.findById(id)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(new UserDto());

        // create a user details object with the email of the user
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(),
                "password",
                authorities);
        // create an authentication token with the user details
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                null);
        // set the authentication token in the security context
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // call the delete method of the userController with the id
        ResponseEntity<?> responseEntity = userController.save(String.valueOf(id));
        // verify that the response status code is 200
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // verify that the userService method delete was called
        verify(userService, times(1)).delete(id);
    }

}
