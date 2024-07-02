package ru.vtb.stub.db.entity;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.annotation.event.PrePersist;
import io.micronaut.serde.annotation.Serdeable;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Serdeable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@MappedEntity("endpoint")
public class EndpointEntity {

    public static final int VARCHAR_FIELD_MAX_SIZE = 256;

    @Id
    @GeneratedValue(GeneratedValue.Type.AUTO)
    private String id;

    @MappedProperty("second_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private EndpointPathMethodTeamPk secondId;

    private Integer wait;

    @MappedProperty("is_regex")
    private Boolean isRegex;

    @MappedProperty("responses")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ResponseEntity> responses;

    @PrePersist
    void setCreatedAt() {
        responses.forEach(it -> {
            if (it.getCreatedAt() == null)
                it.setCreatedAt(Instant.now());
        });
    }

}
