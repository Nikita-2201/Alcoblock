package ru.gknsv.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String id;
    private String login;
    private String password;
    private String sex;
    private String height;
    private String weight;
    private String experience;
}
