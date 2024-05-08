package ru.vtb.stub.db.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "endpoint", uniqueConstraints = @UniqueConstraint(name = "path_method_team", columnNames = {"path", "method", "team"}))
public class EndpointEntity {

    @Column(name = "id", insertable = false, updatable = false, columnDefinition = "serial")
    private Long id;

    @EmbeddedId
    private EndpointPathMethodTeamPk primaryKey;

    @Column(name = "wait")
    private Integer wait;

    @Column(name = "is_regex")
    private Boolean isRegex;

    @OneToMany(mappedBy = "endpoint", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ResponseEntity> responses;

    @OneToMany(mappedBy = "endpoint", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<RequestHistoryEntity> requestHistory;

    public void setPrimaryKey(EndpointPathMethodTeamPk primaryKey) {
        this.primaryKey = primaryKey;
        if (this.getPrimaryKey() == null) {
            this.setPrimaryKey(new EndpointPathMethodTeamPk());
        }
        this.getPrimaryKey().setPath(primaryKey.getPath());
        this.getPrimaryKey().setMethod(primaryKey.getMethod());
        this.getPrimaryKey().setTeam(primaryKey.getTeam());
    }

}
