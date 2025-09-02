package com.openclassrooms.starterjwt.payload.request;

import javax.validation.constraints.*;

import lombok.Data;

@Data
public class SignupRequest {
  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(min = 3, max = 20)
  private String firstName;

  @NotBlank
  @Size(min = 3, max = 20)
  private String lastName;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;

  public String getEmail() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String getPassword() {
    return password;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() { // Add getter for lastName
    return lastName;
  }
}
