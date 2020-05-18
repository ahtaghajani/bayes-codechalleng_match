package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistent.repository.HeroSpellRepository;
import gg.bayes.challenge.rest.model.HeroSpells;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeroSpellsService {

    @Autowired
    private HeroSpellRepository heroSpellRepository;

    public List<HeroSpells> getHeroSpells(Long matchId, String hero){
        return heroSpellRepository.getByMatchIdAndHero(matchId,hero);
    }
}
