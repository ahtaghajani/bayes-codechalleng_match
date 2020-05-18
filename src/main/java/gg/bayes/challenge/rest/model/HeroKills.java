package gg.bayes.challenge.rest.model;

import lombok.Data;
import lombok.Getter;

@Data
public class HeroKills {

    private String hero;

    private Long kills;

    public HeroKills() {
    }

    public HeroKills(String hero, Long kills) {
        this.hero = hero;
        this.kills = kills;
    }

    public String getHero() {
        return hero;
    }

    public Long getKills() {
        return kills;
    }
}
