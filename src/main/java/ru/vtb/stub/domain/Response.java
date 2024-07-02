package ru.vtb.stub.domain;

import io.micronaut.json.tree.JsonNode;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vtb.stub.validate.Status;

import java.util.List;

@Serdeable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    @Status
    @NotNull
    private Integer status;

    @Valid
    private List<Header> headers;

    private JsonNode body;

    private byte[] bodyAsByteArray;

    private Boolean isUsed;

}
