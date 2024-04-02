package ru.vtb.stub.db.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import ru.vtb.stub.db.entity.convert.RawMessageConverter;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "response")
@EntityListeners(AuditingEntityListener.class)
public class ResponseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "json_body")
    @Convert(converter = RawMessageConverter.class)
    private JsonNode body;

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

    @Column(name = "expired_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant expiredAt;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<HeaderEntity> headers;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "endpoint_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private EndpointEntity endpoint;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ResponseEntity responseEntity = (ResponseEntity) o;
        return id != null && Objects.equals(id, responseEntity.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
