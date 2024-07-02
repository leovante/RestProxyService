package ru.vtb.stub.db.entity;

import io.micronaut.serde.annotation.Serdeable;
import lombok.*;

@Serdeable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class HeaderEntity {

    private String name;

    private String value;

}
