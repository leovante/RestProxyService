package ru.vtb.stub.db.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "header", uniqueConstraints = @UniqueConstraint(name = "team_code", columnNames = {"name", "value"}))
public class HeaderEntity {

    @Column(name = "id", insertable = false, updatable = false, columnDefinition = "serial")
    private Long id;

    @EmbeddedId
    private HeaderResponsePk primaryKey;

    @ManyToMany(mappedBy = "headers")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<ResponseEntity> response;

}
