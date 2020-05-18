package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistent.repository.HeroKillRepository;
import gg.bayes.challenge.persistent.repository.HeroSpellRepository;
import gg.bayes.challenge.rest.model.HeroKills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeroKillsService {

    @Autowired
    private HeroKillRepository heroKillRepository;

    public List<HeroKills> getHeroKills(Long matchId){
        return heroKillRepository.getByMatchId(matchId);
    }
}
