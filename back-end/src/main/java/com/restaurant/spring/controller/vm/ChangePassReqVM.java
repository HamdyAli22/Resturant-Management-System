package com.restaurant.spring.controller.vm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassReqVM {

    //@NotBlank(message = "old.pass.required")
    private String oldPassword;

   // @NotBlank(message = "new.pass.required")
//    @Pattern(
//            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{7,}$",
//            message = "new.pass.regexp"
//    )
    private String newPassword;

   // @NotBlank(message = "confirm.pass.required")
    private String confirmPassword;

    private String username;
}
