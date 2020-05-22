package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistent.entity.*;
import gg.bayes.challenge.persistent.repository.*;
import gg.bayes.challenge.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MatchServiceImpl implements MatchService {
    private final HeroItemsService heroItemsService;
    private final HeroSpellsService heroSpellsService;
    private final HeroDamagesService heroDamagesService;
    private final HeroKillsService heroKillsService;

    @Autowired
    public MatchServiceImpl(HeroItemsService heroItemsService, HeroSpellsRepository heroSpellsRepository, HeroSpellsService heroSpellsService, HeroDamagesService heroDamagesService, HeroKillsService heroKillsService) {
        this.heroItemsService = heroItemsService;
        this.heroSpellsService = heroSpellsService;
        this.heroDamagesService = heroDamagesService;
        this.heroKillsService = heroKillsService;
    }

    @Autowired
    private MatchRepository matchRepository;

    @Override
    public Long ingestMatch(String payload) {

        Long matchId = createAndInsertNewMatch();
        heroItemsService.extractAndInsertHeroItems(payload, matchId);
        heroSpellsService.extractAndInsertHeroSpells(payload, matchId);
        heroDamagesService.extractAndInsertHeroDamages(payload, matchId);
        heroKillsService.extractAndInsertHeroKills(payload, matchId);

        return matchId;
    }

    private Long createAndInsertNewMatch() {
        MatchEntity matchEntity = new MatchEntity();
        matchRepository.save(matchEntity);
        return matchEntity.getId();
    }




}
