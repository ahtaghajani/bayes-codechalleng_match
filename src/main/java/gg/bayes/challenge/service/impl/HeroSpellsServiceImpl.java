package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistent.repository.HeroSpellsRepository;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.HeroSpellsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HeroSpellsServiceImpl implements HeroSpellsService {

    private static final String TIMESTAMP_REGEX = "\\[(?<timestamp>\\d{2}:\\d{2}:\\d{2}[.]\\d{3})\\]";
    private static final String HERO_NAME_PREFIX_REGEX = "(npc_dota_hero_)?(npc_dota_creep_)?(npc_dota_neutral_)?";
    private static final String SPELL_REGEX = TIMESTAMP_REGEX + "\\s" + HERO_NAME_PREFIX_REGEX + "(?<heroName>\\w+)\\scasts\\sability\\s(?<spell>\\w+)\\s\\(lvl\\s\\d+\\)\\son\\s" + HERO_NAME_PREFIX_REGEX + "(?<target>\\w+)";

    @Autowired
    private HeroSpellsRepository heroSpellRepository;

    @Override
    public List<HeroSpells> getHeroSpells(Long matchId, String hero){
        return heroSpellRepository.getByMatchIdAndHero(matchId,hero);
    }

    @Override
    public void extractAndInsertHeroSpells(String payload, Long matchId) {

        Pattern pattern = Pattern.compile(SPELL_REGEX);
        Matcher matcher = pattern.matcher(payload);

        while (matcher.find()) {
            String hero = matcher.group("heroName");
            String spell = matcher.group("spell");
            String target = matcher.group("target");

            gg.bayes.challenge.persistent.entity.HeroSpells heroSpells = new gg.bayes.challenge.persistent.entity.HeroSpells();

            heroSpells.setMatchId(matchId);
            heroSpells.setHero(hero);
            heroSpells.setSpell(spell);
            heroSpells.setTarget(target);

            heroSpellRepository.save(heroSpells);
        }
    }

}
