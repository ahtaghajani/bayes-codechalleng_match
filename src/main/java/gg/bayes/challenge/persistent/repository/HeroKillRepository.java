package gg.bayes.challenge.persistent.repository;

import gg.bayes.challenge.persistent.entity.HeroKills;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeroKillRepository extends CrudRepository<HeroKills,Integer> {

        @Query("SELECT " +
                "    new gg.bayes.challenge.rest.model.HeroKills(h.hero, COUNT(h)) " +
                "FROM " +
                "    HeroKills h " +
                "WHERE h.matchId =:matchId "+
                "GROUP BY " +
                "    h.hero")
        List<gg.bayes.challenge.rest.model.HeroKills> getByMatchId(@Param("matchId") Long matchId);

}
