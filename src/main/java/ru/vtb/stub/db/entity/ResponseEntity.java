package ru.vtb.stub.db.entity;

import io.micronaut.json.tree.JsonNode;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Serdeable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEntity {

    private Integer status;

    private JsonNode body;

    private Boolean isUsed;

    @NotNull
    private Instant createdAt;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<HeaderEntity> headers;

}
