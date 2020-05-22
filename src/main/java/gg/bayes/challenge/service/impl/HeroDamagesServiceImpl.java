package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistent.repository.HeroDamagesRepository;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.service.HeroDamagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HeroDamagesServiceImpl implements HeroDamagesService {

    private static final String TIMESTAMP_REGEX = "\\[(?<timestamp>\\d{2}:\\d{2}:\\d{2}[.]\\d{3})\\]";
    private static final String HERO_NAME_PREFIX_REGEX = "(npc_dota_hero_)?(npc_dota_creep_)?(npc_dota_neutral_)?";
    private static final String DAMAGE_REGEX = TIMESTAMP_REGEX + "\\s" + HERO_NAME_PREFIX_REGEX + "(?<heroName>\\w+)\\shits\\s" + HERO_NAME_PREFIX_REGEX + "(?<target>\\w+)\\swith\\s\\w+\\sfor\\s(?<damages>\\d+)";

    @Autowired
    private HeroDamagesRepository heroDamagesRepository;

    @Override
    public List<HeroDamage> getHeroDamage(Long matchId, String heroName) {
        return heroDamagesRepository.getByMatchIdAndHero(matchId, heroName);
    }

    @Override
    public void extractAndInsertHeroDamages(String payload, Long matchId) {
        Pattern pattern = Pattern.compile(DAMAGE_REGEX);
        Matcher matcher = pattern.matcher(payload);

        while (matcher.find()) {
            String hero = matcher.group("heroName");
            String target = matcher.group("target");
            String damage = matcher.group("damages");

            gg.bayes.challenge.persistent.entity.HeroDamage heroDamage = new gg.bayes.challenge.persistent.entity.HeroDamage();

            heroDamage.setMatchId(matchId);
            heroDamage.setHero(hero);
            heroDamage.setTotalDamage(Integer.valueOf(damage));
            heroDamage.setTarget(target);

            heroDamagesRepository.save(heroDamage);
        }
    }

}
