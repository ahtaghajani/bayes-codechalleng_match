package gg.bayes.challenge.persistent.repository;

import gg.bayes.challenge.persistent.entity.HeroDamage;
import gg.bayes.challenge.rest.model.HeroSpells;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeroDamagesRepository extends CrudRepository<HeroDamage,Integer> {
    @Query("SELECT " +
            "    new gg.bayes.challenge.rest.model.HeroDamage(h.target,count (h), SUM(h.totalDamage)) " +
            "FROM " +
            "    HeroDamage h " +
            "WHERE h.matchId =:matchId AND h.hero =:hero "+
            "GROUP BY " +
            "    h.target")
    List<gg.bayes.challenge.rest.model.HeroDamage> getByMatchIdAndHero(@Param("matchId") Long matchId, @Param("hero")String hero);
}
