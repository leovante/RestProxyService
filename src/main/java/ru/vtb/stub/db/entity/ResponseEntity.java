package ru.vtb.stub.db.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.vtb.stub.domain.Header;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseEntity {

    @NotNull
    private Integer status;

    private List<Header> headers;

    private String body;

    private byte[] bodyAsByteArray;

    private String template;

    private Integer idx;

}
