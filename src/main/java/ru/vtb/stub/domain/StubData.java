package ru.vtb.stub.domain;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Тайм-аут ответа от заглушки")
    private Integer wait;

    @NotBlank
    @Schema(description = "Имя (prefix) команды", example = "onb2", required = true)
    private String team;

    @NotBlank
    @Schema(description = "End-point для которого устанавливается ответ", example = "/path/example", required = true)
    private String path;

    @NotBlank
    @Schema(description = "HTTP метод ответа", example = "GET", required = true)
    private String method;

    @Valid
    @Schema(description = "Тело ответа. Статус < 400")
    private Response response;

    @Valid
    @Schema(description = "Тело ответа. Статус >= 400")
    private Error error;
}
