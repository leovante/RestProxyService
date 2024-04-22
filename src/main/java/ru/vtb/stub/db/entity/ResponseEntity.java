package ru.vtb.stub.db.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.vtb.stub.db.entity.convert.RawMessageConverter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "response", uniqueConstraints = @UniqueConstraint(
        name = "path_method_team", columnNames = {"endpoint_path", "endpoint_method", "endpoint_team"}))
@EntityListeners(AuditingEntityListener.class)
public class ResponseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Convert(converter = RawMessageConverter.class)
    @Column(name = "json_body")
    private JsonNode body;

    @Length(max = 8000)
    @Column(name = "string_body")
    private String stringBody;

    @Column(name = "index")
    private Integer index;

    @Column(name = "current_index")
    private Integer currentIndex;

    @Column(name = "created_at")
    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant updatedAt;

    @Column(name = "endpoint_path", insertable = false, updatable = false)
    private String path;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "endpoint_method", insertable = false, updatable = false)
    private RequestMethod method;

    @Column(name = "endpoint_team", insertable = false, updatable = false)
    private String team;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "response_header",
            joinColumns = @JoinColumn(name = "response_id", referencedColumnName = "id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "header_name", referencedColumnName = "name"),
                    @JoinColumn(name = "header_value", referencedColumnName = "value")})
    @ToString.Exclude
    private List<HeaderEntity> headers;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.MERGE)
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
