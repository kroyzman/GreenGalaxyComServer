package com.commissions.galaxy.data;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.commissions.galaxy.logic.Dealer;
import com.commissions.galaxy.logic.Dealer.PartnerSection;




@Repository
public interface DealerDao extends CrudRepository<Dealer, Long>{
	
	List<Dealer> findByName(String name);
	List<Dealer> findBySection(PartnerSection section);
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "DELETE FROM Dealer d WHERE d.id = :dealerId")
	void deleteById(@Param("dealerId") Long deaerlId);
}
