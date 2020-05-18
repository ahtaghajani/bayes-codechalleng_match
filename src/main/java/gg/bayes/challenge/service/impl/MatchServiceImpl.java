package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistent.entity.*;
import gg.bayes.challenge.persistent.repository.*;
import gg.bayes.challenge.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MatchServiceImpl implements MatchService {

    private static final String TIMESTAMP_REGEX = "\\[(?<timestamp>\\d{2}:\\d{2}:\\d{2}[.]\\d{3})\\]";
    private static final String HERO_NAME_PREFIX_REGEX = "(npc_dota_hero_)?(npc_dota_creep_)?(npc_dota_neutral_)?";
    private static final String ITEM_REGEX = TIMESTAMP_REGEX + "\\s" + HERO_NAME_PREFIX_REGEX + "(?<heroName>\\w+)\\sbuys\\sitem\\sitem_(?<item>\\w+)";
    private static final String SPELL_REGEX = TIMESTAMP_REGEX + "\\s" + HERO_NAME_PREFIX_REGEX + "(?<heroName>\\w+)\\scasts\\sability\\s(?<spell>\\w+)\\s\\(lvl\\s\\d+\\)\\son\\s" + HERO_NAME_PREFIX_REGEX + "(?<target>\\w+)";
    private static final String DAMAGE_REGEX = TIMESTAMP_REGEX + "\\s" + HERO_NAME_PREFIX_REGEX + "(?<heroName>\\w+)\\shits\\s" + HERO_NAME_PREFIX_REGEX + "(?<target>\\w+)\\swith\\s\\w+\\sfor\\s(?<damages>\\d+)";
    private static final String KILL_REGEX = TIMESTAMP_REGEX + "\\s" + HERO_NAME_PREFIX_REGEX + "(?<killedHero>\\w+)\\sis\\skilled\\sby\\s" + HERO_NAME_PREFIX_REGEX + "(?<heroName>\\w+)";

    @Autowired
    public MatchServiceImpl() {
    }

    @Autowired
    private HeroItemRepository heroItemRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private HeroDamageRepository heroDamageRepository;

    @Autowired
    private HeroKillRepository heroKillsRepository;

    @Autowired
    private HeroSpellRepository heroSpellRepository;

    @Override
    public Long ingestMatch(String payload) {

        Long matchId = createAndInsertNewMatch();
        extractAndInsertHeroItems(payload, matchId);
        extractAndInsertHeroSpells(payload, matchId);
        extractAndInsertHeroDamages(payload, matchId);
        extractAndInsertHeroKills(payload, matchId);

        return matchId;
    }

    private Long createAndInsertNewMatch() {
        MatchEntity matchEntity = new MatchEntity();
        matchRepository.save(matchEntity);
        return matchEntity.getId();
    }

    public void extractAndInsertHeroItems(String payload, Long matchId) {

        Pattern pattern = Pattern.compile(ITEM_REGEX);
        Matcher matcher = pattern.matcher(payload);

        while (matcher.find()) {
            String timestamp = matcher.group("timestamp");
            String hero = matcher.group("heroName");
            String item = matcher.group("item");

            HeroItems heroItems = new HeroItems();

            heroItems.setMatchId(matchId);
            heroItems.setItem(item);
            heroItems.setHero(hero);
            String t1 = timestamp.replace(":", "");
            heroItems.setTimestamp(Long.valueOf(t1.replace(".", "")));

            heroItemRepository.save(heroItems);
        }
    }

    private void extractAndInsertHeroSpells(String payload, Long matchId) {

        Pattern pattern = Pattern.compile(SPELL_REGEX);
        Matcher matcher = pattern.matcher(payload);

        while (matcher.find()) {
            String hero = matcher.group("heroName");
            String spell = matcher.group("spell");
            String target = matcher.group("target");

            HeroSpells heroSpells = new HeroSpells();

            heroSpells.setMatchId(matchId);
            heroSpells.setHero(hero);
            heroSpells.setSpell(spell);
            heroSpells.setTarget(target);

            heroSpellRepository.save(heroSpells);
        }
    }

    private void extractAndInsertHeroDamages(String payload, Long matchId) {
        Pattern pattern = Pattern.compile(DAMAGE_REGEX);
        Matcher matcher = pattern.matcher(payload);

        while (matcher.find()) {
            String hero = matcher.group("heroName");
            String target = matcher.group("target");
            String damage = matcher.group("damages");

            HeroDamage heroDamage = new HeroDamage();

            heroDamage.setMatchId(matchId);
            heroDamage.setHero(hero);
            heroDamage.setTotalDamage(Integer.valueOf(damage));
            heroDamage.setTarget(target);

            heroDamageRepository.save(heroDamage);
        }
    }

    private void extractAndInsertHeroKills(String payload, Long matchId) {
        Pattern pattern = Pattern.compile(KILL_REGEX);
        Matcher matcher = pattern.matcher(payload);

        while (matcher.find()) {
            String hero = matcher.group("heroName");
            String killed = matcher.group("killedHero");

            HeroKills heroKills = new HeroKills();

            heroKills.setMatchId(matchId);
            heroKills.setKilled(killed);
            heroKills.setHero(hero);

            heroKillsRepository.save(heroKills);
        }
    }
}
