package com.openclassrooms.starterjwt.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String username;
  private String firstName;
  private String lastName;

  private Boolean admin;

  public JwtResponse(String accessToken, Long id, String username,String firstName, String lastName, Boolean admin) {
    this.token = accessToken;
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.admin = admin;
  }
}
