package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    UserController userController;
    @Mock
    UserMapper userMapper;
    @Mock
    UserService userService;
    @Mock
    Authentication authentication;
    @Mock
    SecurityContext securityContext;

    @BeforeEach
    void setUp(){
        userController = new UserController(
                userService,
                userMapper
        );
    }

    @Test
    @DisplayName("findById [User]")
    void whenUserIdentityIsPresent_thenReturnUserDto(){
        //GIVEN
        String id = "10";
        User user = new User();
        UserDto userDto = new UserDto();

        //WHEN
        when(userService.findById(Long.parseLong(id)))
                .thenReturn(user);

        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> userDtoEntity = ResponseEntity.ok().body(userDto);

        ResponseEntity<?> findUserById = userController.findById(id);
        //THEN
        assertThat(findUserById).isEqualTo(userDtoEntity);
        assertThat(findUserById.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService, times(1))
                .findById(Long.parseLong(id));
        verify(userMapper, times(1))
                .toDto(user);
    }
    @Test
    @DisplayName("not found [user]")
    void whenUserIsNull_thenReturnNotFound(){
        String id = "11";
        User user = null;

        when(userService.findById(Long.parseLong(id))).thenReturn(user);

        ResponseEntity<?> notFound = ResponseEntity.notFound().build();

        ResponseEntity<?> findUserId = userController.findById(id);

        assertThat(findUserId).isEqualTo(notFound);
        assertThat(findUserId.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userService, times(1)).findById(Long.parseLong(id));
    }

    @Test
    @DisplayName("Exception [user]")
    void whenLongParseGoesWrong_thenThrowNumberFormatException(){
        String id = "test123";

        assertThrows(NumberFormatException.class, ()->{
            Long.parseLong(id);
        });

       ResponseEntity<?> findUserId = userController.findById(id);

       assertThat(findUserId).isEqualTo(ResponseEntity.badRequest().build());
       assertThat(findUserId.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("save User")
    void whenUserIdentityIsPresent_thenDeleteIdentity(){
        String id = "12";

        userService.findById(Long.parseLong(id));

        userService.delete(Long.parseLong(id));

        ResponseEntity<?> responseEntity = ResponseEntity.ok().build();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService, times(1))
                .findById(Long.parseLong(id));
        verify(userService, times(1))
                .delete(Long.parseLong(id));

    }
    @Test
    @DisplayName("userId null")
    void whenUserIdentityIsNull_ThenReturnNotFound(){
        String id= "14";
        User user = null;

        when(userService.findById(Long.parseLong(id)))
                .thenReturn(user);

        ResponseEntity<?> notFound = ResponseEntity.notFound().build();
        ResponseEntity<?> findUserId = userController.save(id);

        assertThat(findUserId).isEqualTo(notFound);
        assertThat(findUserId.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userService, times(1))
                .findById(Long.parseLong(id));
    }
    @Test
    @DisplayName("UNAUTHORIZED")
    void whenObjectCredentialsIsDifferentOfUserCredentials_thenReturnUNAUTHORIZED(){
        String id = "15";

        User user = User.builder()
                .email("yoga@studio.com")
                .lastName("test")
                .firstName("Test")
                .password("test123")
                .admin(true)
                .build();

        when(userService.findById(Long.parseLong(id)))
                .thenReturn(user);

        UserDetails userDetails = UserDetailsImpl.builder()
                .username("test@test.com")
                .build();
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

       ResponseEntity<?> response = userController.save(id);
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
    @Test
    @DisplayName("Exception [user]")
    void whenLongParseGoesWrong_thenThrowException(){
        String id = "test16";

        assertThrows(NumberFormatException.class, ()->{
            Long.parseLong(id);
        });

        ResponseEntity<?> request = ResponseEntity.badRequest().build();
        ResponseEntity<?> exception = userController.save(id);

        assertThat(exception).isEqualTo(request);
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }
}
