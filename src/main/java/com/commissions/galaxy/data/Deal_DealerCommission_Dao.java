package com.commissions.galaxy.data;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.commissions.galaxy.logic.Deal;
import com.commissions.galaxy.logic.Deal_DealerCommission;
import com.commissions.galaxy.logic.Dealer;

public interface Deal_DealerCommission_Dao extends CrudRepository<Deal_DealerCommission, Long> {

//	@Query("SELECT c FROM Deal_DealerCommission c WHERE c.dealer = :dealerId")
	List<Deal_DealerCommission> findByDealer(Dealer dealer);

	@Transactional
	@Modifying
	@Query("delete from Commissions c where c.deal = :deal")
	void deleteByDeal(@Param("deal") Deal deal);

	@Transactional
	@Modifying
	@Query("delete from Commissions c where c.dealer = :dealer")
	void deleteByDealer(Dealer dealer);

	@Query("select c from Commissions c where c.dealer = :dealer and c.deal.date between :startDateObject and :endDateObject")
	List<Deal_DealerCommission> findByDealerAndDate(Date startDateObject, Date endDateObject, Dealer dealer);
}
