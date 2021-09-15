package ru.vtb.stub.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StubData {

    @NotBlank
    private String route;
    @NotBlank
    private String method;
    @Valid
    private DataBlock response;
    @Valid
    private DataBlock error;
    @Valid
    private DataBlock validate;
}
