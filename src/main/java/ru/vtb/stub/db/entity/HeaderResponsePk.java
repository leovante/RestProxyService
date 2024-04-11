package ru.vtb.stub.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.validator.constraints.Length;

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
public class HeaderResponsePk implements Serializable {

    public static final int VARCHAR_FIELD_MAX_SIZE = 256;

    @Column(name = "name")
    @Length(max = VARCHAR_FIELD_MAX_SIZE)
    private String name;

    @Column(name = "value")
    @Length(max = VARCHAR_FIELD_MAX_SIZE)
    private String value;

}
