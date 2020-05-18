package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistent.entity.HeroItems;
import gg.bayes.challenge.persistent.repository.HeroItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeroItemsService {

    @Autowired
    private HeroItemRepository heroItemRepository;

    public List<HeroItems> getItems(Long matchId, String heroName){
        return heroItemRepository.findByMatchIdAndHero(matchId,heroName);
    }
}
