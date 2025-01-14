package ru.vtb.stub.domain;

import io.micronaut.http.HttpMethod;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vtb.stub.validate.Path;
import ru.vtb.stub.validate.Team;

import java.util.List;

@Serdeable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StubData {

    @Team
    @NotBlank
    @Schema(description = "Уникальный префикс для одновременной работы разных команд",
            example = "team1", required = true)
    private String team;

    @Path
    @NotBlank
    @Schema(description = "End-point для которого устанавливается ответ." +
            "С помощью \"--\" можно указать, что подходит любой path param",
            example = "/path/example, /path/--/example/--", required = true)
    private String path;

    @Schema(description = "HTTP метод для которого устанавливается ответ", example = "GET", required = true)
    private HttpMethod method;

    @Min(100)
    @Max(60_000)
    @Schema(description = "Тайм-аут ответа")
    private Integer wait;

    @Deprecated
    @Valid
    @Schema(description = "Параметры ответа")
    private Response response;

    @Valid
    @Schema(description = "Список параметров ответа")
    private List<Response> responses;

}
