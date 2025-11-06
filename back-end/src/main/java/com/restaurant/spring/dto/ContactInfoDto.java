package com.restaurant.spring.dto;

import com.restaurant.spring.dto.security.AccountDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactInfoDto implements Serializable {

    private Long id;

    @NotEmpty(message = "not_empty.name")
    @Size(min = 7, max = 100, message = "size.name")
    private String name;

    @NotEmpty(message = "not_empty.email")
    @Email(message = "not_valid.email")
    private String email;

    @NotEmpty(message = "not_empty.subject")
    @Size(min = 5, max = 60, message = "size.subject")
    private String subject;



    @NotEmpty(message = "not_empty.message")
    @Size(min = 10, max = 1000, message = "size.message")
    private String message;

    @Size(max = 1000, message = "size.reply")
    private String reply;

    private AccountDto account;
}
