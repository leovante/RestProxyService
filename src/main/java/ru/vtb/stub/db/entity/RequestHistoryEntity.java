package ru.vtb.stub.db.entity;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.annotation.event.PrePersist;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.vtb.stub.domain.Request;

import java.time.Instant;

@Serdeable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@MappedEntity("request_history")
public class RequestHistoryEntity {

    @Id
    @GeneratedValue(GeneratedValue.Type.AUTO)
    private String id;

    private Request request;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String endpoint;

    @MappedProperty("endpoint_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private EndpointPathMethodTeamPk endpointId;

    @NotNull
    private Instant createdAt;

    @PrePersist
    void setCreatedAt() {
        if(createdAt == null){
            createdAt = Instant.now();
        }
    }

}
