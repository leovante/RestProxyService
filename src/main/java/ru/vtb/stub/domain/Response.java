package ru.vtb.stub.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vtb.stub.validate.Status;

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
    @JsonDeserialize(using = BodyToStringDeserializer.class)
    private String body;

    @Schema(description = "Тело ответа в формате byte array")
    private byte[] bodyAsByteArray;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idx;

}
