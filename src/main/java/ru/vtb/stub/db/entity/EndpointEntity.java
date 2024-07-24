package ru.vtb.stub.db.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import ru.vtb.stub.domain.Response;
import ru.vtb.stub.domain.StubData;

import java.time.Instant;
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
@EntityListeners(AuditingEntityListener.class)
public class EndpointEntity {

    @Column(name = "id", insertable = false, updatable = false, columnDefinition = "serial")
    private Long id;

    @EmbeddedId
    private EndpointPathMethodTeamPk primaryKey;

    @Column(name = "wait")
    private Integer wait;

    @Column(name = "is_regex")
    private Boolean isRegex;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "response_stub", columnDefinition = "jsonb")
    private List<ResponseEntity> responses;

    @Column(name = "idx")
    private Integer idx;

    @Column(name = "created_at")
    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant updatedAt;

    /*@OneToMany(mappedBy = "endpoint", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ResponseEntity> responses;*/

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
