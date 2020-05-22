package gg.bayes.challenge.service;

import gg.bayes.challenge.persistent.entity.HeroItems;

import java.util.List;

public interface HeroItemsService {
    List<HeroItems> getItems(Long matchId, String heroName);

    void extractAndInsertHeroItems(String payload, Long matchId);
}
