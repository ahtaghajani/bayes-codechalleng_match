package gg.bayes.challenge.service;

import gg.bayes.challenge.rest.model.HeroSpells;

import java.util.List;

public interface HeroSpellsService {
    List<HeroSpells> getHeroSpells(Long matchId, String hero);

    void extractAndInsertHeroSpells(String payload, Long matchId);
}
