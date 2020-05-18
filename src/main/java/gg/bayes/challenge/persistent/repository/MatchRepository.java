package gg.bayes.challenge.persistent.repository;

import gg.bayes.challenge.persistent.entity.MatchEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends CrudRepository<MatchEntity, Integer> {

}
