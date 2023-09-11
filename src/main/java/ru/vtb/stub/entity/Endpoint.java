package ru.vtb.stub.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "endpoint", uniqueConstraints = @UniqueConstraint(name = "index3", columnNames = {"path", "method", "team_id"}))
public class Endpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "method", nullable = false)
    private String method;

    @Column(name = "wait")
    private Integer wait;

    @OneToMany(mappedBy = "endpoint", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Response> responses;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id")
    @ToString.Exclude
    private Team team;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Endpoint endpoint = (Endpoint) o;
        return id != null && Objects.equals(id, endpoint.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
