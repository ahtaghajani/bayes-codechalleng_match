package gg.bayes.challenge.persistent.repository;

import gg.bayes.challenge.persistent.entity.HeroItems;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeroItemRepository extends CrudRepository<HeroItems,Integer> {

    List<HeroItems> findByMatchIdAndHero(Long matchId, String heroName);
}
