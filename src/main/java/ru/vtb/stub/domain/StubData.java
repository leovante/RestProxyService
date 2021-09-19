package ru.vtb.stub.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vtb.stub.validate.Method;
import ru.vtb.stub.validate.Path;
import ru.vtb.stub.validate.Team;

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

    @Team
    @NotBlank
    @Schema(description = "Уникальный prefix для одновременной работы разных команд", example = "team1", required = true)
    private String team;

    @Path
    @NotBlank
    @Schema(description = "End-point для которого устанавливается ответ", example = "/path/example", required = true)
    private String path;

    @Method
    @NotBlank
    @Schema(description = "HTTP метод ответа", example = "GET", required = true)
    private String method;

    @Valid
    @Schema(description = "Параметры ответа")
    private Response response;
}
