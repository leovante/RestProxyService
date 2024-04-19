package ru.vtb.stub.db.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "request_history", uniqueConstraints = @UniqueConstraint(
        name = "path_method_team", columnNames = {"endpoint_path", "endpoint_method", "endpoint_team"}))
@EntityListeners(AuditingEntityListener.class)
public class RequestHistoryEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "request", nullable = true, length = 8000)
    private String request;

    @Basic
    @Column(name = "endpoint_path", nullable = false, length = 255)
    private String endpointPath;

    @Basic
    @Column(name = "endpoint_method", nullable = false, length = 255)
    private String endpointMethod;

    @Basic
    @Column(name = "endpoint_team", nullable = false, length = 255)
    private String endpointTeam;

}
