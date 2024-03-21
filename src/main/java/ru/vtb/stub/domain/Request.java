package ru.vtb.stub.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @NotBlank
    @Schema(description = "Время, когда поступил запрос. Заполняется автоматически", required = true)
    private LocalDateTime date;

    @NotBlank
    @Schema(description = "End-point на который пришел запрос", example = "/path/example", required = true)
    private String path;

    @NotBlank
    @Schema(description = "HTTP метод запроса", example = "GET", required = true)
    private String method;

    @Schema(description = "Map<String, String> заголовков запроса")
    private Map<String, String> headers;

    @Schema(description = "Map<String, List<String>> параметров строки запроса")
    private Map<String, List<String>> params;

    @Schema(description = "Строка с телом запроса")
    private String body;
}
