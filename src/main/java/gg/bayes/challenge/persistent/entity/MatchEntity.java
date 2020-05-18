package gg.bayes.challenge.persistent.entity;

import javax.persistence.*;

@Entity
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public MatchEntity() {
    }
}
