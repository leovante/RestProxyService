package ru.vtb.stub.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "response")
public class ResponseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "body_json")
    private String bodyJson;

    @Column(name = "body_string")
    private String bodyString;

    @Column(name = "index_number")
    private Integer indexNumber;

    @Column(name = "current_number")
    private Integer currentNumber;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<HeaderEntity> headers;

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
