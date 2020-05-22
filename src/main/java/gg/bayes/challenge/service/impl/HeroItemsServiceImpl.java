package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.persistent.entity.HeroItems;
import gg.bayes.challenge.persistent.repository.HeroItemsRepository;

import gg.bayes.challenge.service.HeroItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HeroItemsServiceImpl implements HeroItemsService {
    private static final String TIMESTAMP_REGEX = "\\[(?<timestamp>\\d{2}:\\d{2}:\\d{2}[.]\\d{3})\\]";
    private static final String HERO_NAME_PREFIX_REGEX = "(npc_dota_hero_)?(npc_dota_creep_)?(npc_dota_neutral_)?";
    static final String ITEM_REGEX = TIMESTAMP_REGEX + "\\s" + HERO_NAME_PREFIX_REGEX + "(?<heroName>\\w+)\\sbuys\\sitem\\sitem_(?<item>\\w+)";

    @Autowired
    private HeroItemsRepository heroItemRepository;

    @Override
    public List<HeroItems> getItems(Long matchId, String heroName){
        return heroItemRepository.findByMatchIdAndHero(matchId,heroName);
    }

    @Override
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
}
