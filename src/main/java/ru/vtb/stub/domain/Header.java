package ru.vtb.stub.domain;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Serdeable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Header {

    @NotBlank
    private String name;

    @NotBlank
    private String value;

}
