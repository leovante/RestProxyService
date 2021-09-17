package ru.vtb.stub.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Header {

    @NotBlank
    private String name;
    @NotBlank
    private String value;
}
