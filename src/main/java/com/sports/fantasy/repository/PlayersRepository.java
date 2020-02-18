package com.sports.fantasy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.sports.fantasy.model.Players;

@Repository
public interface PlayersRepository extends CrudRepository<Players, Long>{

  @Query("Select p from Players as p where p.fullName = ?1")
  List<Players> getPlayerInfoByName(String name);

}
