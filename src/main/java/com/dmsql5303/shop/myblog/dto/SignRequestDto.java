package com.dmsql5303.shop.myblog.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class SignRequestDto {
    private String username;
    private String password;
    private String email;

}
