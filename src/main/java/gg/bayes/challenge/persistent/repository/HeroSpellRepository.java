package gg.bayes.challenge.persistent.repository;

import gg.bayes.challenge.persistent.entity.HeroSpells;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeroSpellRepository extends CrudRepository<HeroSpells,Integer> {
    @Query("SELECT " +
            "    new gg.bayes.challenge.rest.model.HeroSpells(h.spell, COUNT(h)) " +
            "FROM " +
            "    HeroSpells h " +
            "WHERE h.matchId =:matchId AND h.hero =:hero "+
            "GROUP BY " +
            "    h.spell")
    List<gg.bayes.challenge.rest.model.HeroSpells> getByMatchIdAndHero(@Param("matchId") Long matchId, @Param("hero")String hero);

}
