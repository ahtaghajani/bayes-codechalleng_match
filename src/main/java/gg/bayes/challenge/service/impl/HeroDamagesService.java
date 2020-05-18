package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistent.repository.HeroDamageRepository;
import gg.bayes.challenge.rest.model.HeroDamage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeroDamagesService {

    @Autowired
    private HeroDamageRepository heroDamageRepository;

    public List<HeroDamage> getHeroDamage(Long matchId, String heroName) {
        return heroDamageRepository.getByMatchIdAndHero(matchId, heroName);
    }
}
