package ru.vtb.stub.db.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.vtb.stub.db.entity.convert.RequestHistoryConverter;
import ru.vtb.stub.domain.Request;

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
    @Column(name = "id")
    private Long id;

    @Convert(converter = RequestHistoryConverter.class)
    @Column(name = "request")
    private Request request;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumns({
            @JoinColumn(name = "endpoint_path", referencedColumnName = "path"),
            @JoinColumn(name = "endpoint_method", referencedColumnName = "method"),
            @JoinColumn(name = "endpoint_team", referencedColumnName = "team")
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private EndpointEntity endpoint;

}
