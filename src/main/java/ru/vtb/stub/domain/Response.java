package ru.vtb.stub.domain;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vtb.stub.validate.Status;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    @Status
    @NotNull
    @Schema(description = "Код ответа", example = "200", required = true)
    private Integer status;

    @Valid
    @Schema(description = "Список заголовков ответа")
    private List<Header> headers;

    @Schema(description = "Тело ответа в формате JSON")
    private JsonNode body;

    @Schema(description = "Тело ответа в формате byte array")
    private byte[] bodyAsByteArray;

    @Schema(description = "Тело ответа в формате STRING")
    private String stringBody;
}
