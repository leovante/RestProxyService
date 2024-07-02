package ru.vtb.stub.db.entity;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@MappedEntity("team")
public class TeamEntity {

    @Id
    @GeneratedValue(GeneratedValue.Type.AUTO)
    private String id;

    private String code;

}
