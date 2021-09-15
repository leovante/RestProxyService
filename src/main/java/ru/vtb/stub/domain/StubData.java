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
    private String team;
    @NotBlank
    private String path;
    @NotBlank
    private String method;
    @Valid
    private Response response;
    @Valid
    private Error error;
}
