package gg.bayes.challenge.service;

import gg.bayes.challenge.rest.model.HeroKills;

import java.util.List;

public interface HeroKillsService {
    List<HeroKills> getHeroKills(Long matchId);

    void extractAndInsertHeroKills(String payload, Long matchId);
}
