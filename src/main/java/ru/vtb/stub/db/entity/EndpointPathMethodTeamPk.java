package ru.vtb.stub.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;

/**
 * Составной ид для роли уведомления.
 */
@Setter
@Getter
@Embeddable
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class EndpointPathMethodTeamPk implements Serializable {

    public static final int VARCHAR_FIELD_MAX_SIZE = 256;

    @Column(name = "path")
    @Length(max = VARCHAR_FIELD_MAX_SIZE)
    private String path;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "method")
    private RequestMethod method;

    @Column(name = "team")
    @Length(max = VARCHAR_FIELD_MAX_SIZE)
    private String team;

}
