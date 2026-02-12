package com.resturant.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthRequest(String username, @JsonProperty("password") String psw) {

}
