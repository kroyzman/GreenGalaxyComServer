package com.commissions.galaxy.data;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.commissions.galaxy.logic.Deal;

public interface DealDao extends CrudRepository<Deal, Long> {
	
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "DELETE FROM Deal d WHERE d.id = :dealId")
	void deleteById(@Param("dealId") Long dealId);
	List<Deal> findAllByDateBetween(Date dateStart, Date dateEnd);
}
