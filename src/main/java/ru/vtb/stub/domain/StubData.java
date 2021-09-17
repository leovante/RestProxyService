package ru.vtb.stub.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StubData {

    @Min(100)
    @Max(60_000)
    private Integer wait;
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
