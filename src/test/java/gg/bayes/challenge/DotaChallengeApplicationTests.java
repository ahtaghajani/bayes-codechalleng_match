package gg.bayes.challenge;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gg.bayes.challenge.rest.model.HeroKills;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.hamcrest.Matchers;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DotaChallengeApplicationTests {
    public static final String BASE_PATH = "/api/match";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testHeroKills() throws Exception {
        String input = "[00:11:17.489] npc_dota_hero_snapfire is killed by npc_dota_hero_mars\n" +
                "[00:11:20.322] npc_dota_hero_rubick is killed by npc_dota_hero_pangolier\n" +
                "[00:12:15.108] npc_dota_neutral_harpy_scout is killed by npc_dota_hero_pangolier\n" +
                "[00:12:21.207] npc_dota_neutral_harpy_storm is killed by npc_dota_creep_goodguys_ranged\n" +
                "[00:12:22.873] npc_dota_neutral_harpy_scout is killed by npc_dota_creep_goodguys_melee\n" +
                "[00:13:06.296] npc_dota_hero_pangolier is killed by npc_dota_hero_mars\n" +
                "[00:13:32.556] npc_dota_hero_bane is killed by npc_dota_hero_abyssal_underlord\n" +
                "[00:14:13.813] npc_dota_hero_pangolier is killed by npc_dota_hero_rubick\n" +
                "[00:14:26.543] npc_dota_neutral_polar_furbolg_champion is killed by npc_dota_hero_dragon_knight\n" +
                "[00:14:35.607] npc_dota_neutral_polar_furbolg_ursa_warrior is killed by npc_dota_hero_dragon_knight\n";

        String matchId = ingestMatch(BASE_PATH, input);

        String content = mockMvc.perform(
                get(BASE_PATH + "/" + matchId)
                        .content(input)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(7)))
                .andReturn().getResponse().getContentAsString();

        List<HeroKills> heroKillsList = objectMapper.readValue(content, new TypeReference<List<HeroKills>>() {});
        assertHeroKills(heroKillsList, "mars", 2L);
        assertHeroKills(heroKillsList, "pangolier", 2L);
        assertHeroKills(heroKillsList, "goodguys_ranged", 1L);
        assertHeroKills(heroKillsList, "goodguys_melee", 1L);
        assertHeroKills(heroKillsList, "abyssal_underlord", 1L);
        assertHeroKills(heroKillsList, "rubick", 1L);
        assertHeroKills(heroKillsList, "dragon_knight", 2L);
    }

    private void assertHeroKills(List<HeroKills> heroKillsList, String heroName, Long kills) {
        Optional<HeroKills> hiroKilsOptional = heroKillsList.stream().filter(heroKills -> heroName.equals(heroKills.getHero())).findAny();
        assertEquals(true, hiroKilsOptional.isPresent(), "hero not found: " + heroName);
        assertEquals(kills, hiroKilsOptional.get().getKills(), heroName + " kills");
    }

    @Test
    public void testHeroItems() throws Exception {
        String input = "[00:08:46.693] npc_dota_hero_snapfire buys item item_clarity\n" +
                "[00:08:46.759] npc_dota_hero_dragon_knight buys item item_quelling_blade\n" +
                "[00:08:46.992] npc_dota_hero_dragon_knight buys item item_gauntlets\n" +
                "[00:08:47.426] npc_dota_hero_dragon_knight buys item item_gauntlets\n" +
                "[00:08:48.059] npc_dota_hero_dragon_knight buys item item_circlet\n";

        String matchId = ingestMatch(BASE_PATH, input);

        String heroName = "snapfire";
        mockMvc.perform(
                get(BASE_PATH + "/" + matchId + "/" + heroName + "/items")
                        .content(input)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].item").value("clarity"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].timestamp").value("846693"));

        heroName = "dragon_knight";
        mockMvc.perform(
                get(BASE_PATH + "/" + matchId + "/" + heroName + "/items")
                        .content(input)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].item").value("quelling_blade"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].timestamp").value("846759"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].item").value("gauntlets"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].timestamp").value("846992"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].item").value("gauntlets"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].timestamp").value("847426"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[3].item").value("circlet"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[3].timestamp").value("848059"));

    }

    @Test
    public void testHeroSpells() throws Exception {
        String input = "[00:08:43.460] npc_dota_hero_pangolier casts ability pangolier_swashbuckle (lvl 1) on dota_unknown\n" +
                "[00:08:43.460] npc_dota_hero_pangolier casts ability pangolier_swashbuckle (lvl 1) on dota_unknown\n" +
                "[00:10:52.129] npc_dota_hero_rubick casts ability rubick_fade_bolt (lvl 1) on npc_dota_creep_goodguys_ranged\n" +
                "[00:15:44.224] npc_dota_hero_pangolier casts ability pangolier_shield_crash (lvl 1) on dota_unknown";
        String matchId = ingestMatch(BASE_PATH, input);

        String heroName = "pangolier";
        mockMvc.perform(
                get(BASE_PATH + "/" + matchId + "/" + heroName + "/spells")
                        .content(input)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].spell").value("pangolier_shield_crash"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].casts").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].spell").value("pangolier_swashbuckle"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].casts").value("2"));

        heroName = "rubick";
        mockMvc.perform(
                get(BASE_PATH + "/" + matchId + "/" + heroName + "/spells")
                        .content(input)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].spell").value("rubick_fade_bolt"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].casts").value("1"));
    }


    @Test
    public void testHeroDamages() throws Exception {
        String input = "[00:10:42.031] npc_dota_hero_abyssal_underlord hits npc_dota_hero_bloodseeker with abyssal_underlord_firestorm for 18 damage (720->702)\n" +
                "[00:10:42.031] npc_dota_hero_abyssal_underlord hits npc_dota_hero_bloodseeker with abyssal_underlord_firestorm for 5 damage (702->697)\n" +
                "[00:10:43.064] npc_dota_hero_abyssal_underlord hits npc_dota_hero_bloodseeker with abyssal_underlord_firestorm for 5 damage (698->693)\n" +
                "[00:10:43.065] npc_dota_hero_abyssal_underlord hits npc_dota_hero_bloodseeker1 with abyssal_underlord_firestorm for 5 damage (698->693)\n" +
                "[00:10:43.066] npc_dota_hero_abyssal_underlord hits npc_dota_hero_bloodseeker with abyssal_underlord_firestorm for 18 damage (693->675)\n";

        String matchId = ingestMatch(BASE_PATH, input);

        String heroName = "abyssal_underlord";
        mockMvc.perform(
                get(BASE_PATH + "/" + matchId + "/" + heroName + "/damage")
                        .content(input)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].target").value("bloodseeker"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].damage_instances").value(4l))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].total_damage").value(46l))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].target").value("bloodseeker1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].damage_instances").value(1l))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].total_damage").value(5l));
    }

    private String ingestMatch(String basePath, String input) throws Exception {
        return mockMvc.perform(
                post(basePath)
                        .content(input)
                        .contentType(MediaType.TEXT_PLAIN)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNumber())
                .andReturn().getResponse().getContentAsString();
    }

}
