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
@Table(name = "response")
public class Response {

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

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Header> headers;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "endpoint_id")
    @ToString.Exclude
    private Endpoint endpoint;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Response response = (Response) o;
        return id != null && Objects.equals(id, response.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
