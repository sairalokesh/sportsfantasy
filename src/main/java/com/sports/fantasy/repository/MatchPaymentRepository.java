package com.sports.fantasy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.sports.fantasy.model.MatchPayments;

@Repository
public interface MatchPaymentRepository extends CrudRepository<MatchPayments, Long>{

  @Query(value = "select t FROM MatchPayments as t where t.user.id = ?1 order by t.transactionDate desc")
  List<MatchPayments> getAllUserMatchPayments(Long userId);

}
