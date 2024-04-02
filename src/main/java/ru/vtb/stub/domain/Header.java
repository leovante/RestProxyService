package ru.vtb.stub.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Header {

    @NotBlank
    @Schema(description = "Имя заголовка", example = "Content-Type", required = true)
    private String name;

    @NotBlank
    @Schema(description = "Значение заголовка", example = "application/json", required = true)
    private String value;
}
