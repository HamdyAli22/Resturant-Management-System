package com.restaurant.spring.controller.vm;

import com.restaurant.spring.dto.ContactInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ContactInfoResponseVm implements Serializable {
    private List<ContactInfoDto> messages;

    private Long totalMessages;

}
