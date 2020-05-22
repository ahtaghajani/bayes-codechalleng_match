package gg.bayes.challenge.service;

import gg.bayes.challenge.rest.model.HeroDamage;

import java.util.List;

public interface HeroDamagesService {
    List<HeroDamage> getHeroDamage(Long matchId, String heroName);

    void extractAndInsertHeroDamages(String payload, Long matchId);
}
