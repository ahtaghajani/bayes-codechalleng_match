package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistent.repository.HeroKillsRepository;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.service.HeroKillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HeroKillsServiceImpl implements HeroKillsService {

    private static final String TIMESTAMP_REGEX = "\\[(?<timestamp>\\d{2}:\\d{2}:\\d{2}[.]\\d{3})\\]";
    private static final String HERO_NAME_PREFIX_REGEX = "(npc_dota_hero_)?(npc_dota_creep_)?(npc_dota_neutral_)?";
    private static final String KILL_REGEX = TIMESTAMP_REGEX + "\\s" + HERO_NAME_PREFIX_REGEX + "(?<killedHero>\\w+)\\sis\\skilled\\sby\\s" + HERO_NAME_PREFIX_REGEX + "(?<heroName>\\w+)";

    @Autowired
    private HeroKillsRepository heroKillsRepository;

    @Override
    public List<HeroKills> getHeroKills(Long matchId){
        return heroKillsRepository.getByMatchId(matchId);
    }

    @Override
    public void extractAndInsertHeroKills(String payload, Long matchId) {
        Pattern pattern = Pattern.compile(KILL_REGEX);
        Matcher matcher = pattern.matcher(payload);

        while (matcher.find()) {
            String hero = matcher.group("heroName");
            String killed = matcher.group("killedHero");

            gg.bayes.challenge.persistent.entity.HeroKills heroKills = new gg.bayes.challenge.persistent.entity.HeroKills();

            heroKills.setMatchId(matchId);
            heroKills.setKilled(killed);
            heroKills.setHero(hero);

            heroKillsRepository.save(heroKills);
        }
    }
}
