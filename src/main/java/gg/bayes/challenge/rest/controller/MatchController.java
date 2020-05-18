package gg.bayes.challenge.rest.controller;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;

import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import gg.bayes.challenge.service.impl.HeroDamagesService;
import gg.bayes.challenge.service.impl.HeroItemsService;
import gg.bayes.challenge.service.impl.HeroKillsService;
import gg.bayes.challenge.service.impl.HeroSpellsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final MatchService matchService;
    private final HeroKillsService heroKillService;
    private final HeroItemsService heroItemsService;
    private final HeroDamagesService heroDamagesService;
    private final HeroSpellsService heroSpellsService;

    @Autowired
    public MatchController(MatchService matchService, HeroKillsService heroKillService, HeroItemsService heroItemsService, HeroDamagesService heroDamagesService, HeroSpellsService heroSpellsService) {
        this.matchService = matchService;
        this.heroKillService = heroKillService;
        this.heroItemsService = heroItemsService;
        this.heroDamagesService = heroDamagesService;
        this.heroSpellsService = heroSpellsService;
    }


    @PostMapping(consumes = "text/plain")
    public ResponseEntity<Long> ingestMatch(@RequestBody @NotNull @NotBlank String payload) {
        final Long matchId = matchService.ingestMatch(payload);
        return ResponseEntity.ok(matchId);
    }

    @GetMapping("{matchId}")
    public ResponseEntity<List<HeroKills>> getMatch(@PathVariable("matchId") Long matchId) {
        List<HeroKills> heroKillsList = heroKillService.getHeroKills(matchId);
        return new ResponseEntity<>(heroKillsList, HttpStatus.OK);
    }

    @GetMapping("{matchId}/{heroName}/items")
    public ResponseEntity<List<HeroItems>> getItems(@PathVariable("matchId") Long matchId,
                                                    @PathVariable("heroName") String heroName) {

        List<gg.bayes.challenge.persistent.entity.HeroItems> heroItems = heroItemsService.getItems(matchId,heroName);
        List<HeroItems> response=new ArrayList<>();
        for (gg.bayes.challenge.persistent.entity.HeroItems h:heroItems) {
            HeroItems heroItemsList=new HeroItems();
            heroItemsList.setItem(h.getItem());
            heroItemsList.setTimestamp(h.getTimestamp());
            response.add(heroItemsList);
        }

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("{matchId}/{heroName}/spells")
    public ResponseEntity<List<HeroSpells>> getSpells(@PathVariable("matchId") Long matchId,
                                                      @PathVariable("heroName") String heroName) {
        List<HeroSpells> heroSpellsList = heroSpellsService.getHeroSpells(matchId,heroName);
        return new ResponseEntity<>(heroSpellsList, HttpStatus.OK);
    }

    @GetMapping("{matchId}/{heroName}/damage")
    public ResponseEntity<List<HeroDamage>> getDamage(@PathVariable("matchId") Long matchId,
                                                      @PathVariable("heroName") String heroName) {
        List<HeroDamage> heroDamageList = heroDamagesService.getHeroDamage(matchId,heroName);
        return new ResponseEntity<>(heroDamageList, HttpStatus.OK);
    }
}
