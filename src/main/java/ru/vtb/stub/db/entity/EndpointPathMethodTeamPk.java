package ru.vtb.stub.db.entity;

import io.micronaut.serde.annotation.Serdeable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Составной ид для эндпоинта.
 */
@Serdeable
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class EndpointPathMethodTeamPk {

    private String path;

    private String team;

    private String method;

}
