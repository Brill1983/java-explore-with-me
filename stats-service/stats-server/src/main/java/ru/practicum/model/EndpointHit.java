package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "endpoint_hits")
@Getter
@Setter
public class EndpointHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "app", length = 100)
    private String app;

    @Column(name = "uri", length = 100)
    private String uri;

    @Column(name = "ip", length = 100)
    private String ip;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
