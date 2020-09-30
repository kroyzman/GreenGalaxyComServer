package com.commissions.galaxy.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.commissions.galaxy.logic.Deal;
import com.commissions.galaxy.logic.Deal_DealerCommission;
import com.commissions.galaxy.logic.Dealer;

public interface CommissionService {
	public Deal_DealerCommission addNewDealDealerCommission(Deal_DealerCommission commission) throws Exception;

	public List<Deal_DealerCommission> getCommissionsByDealer(Dealer dealer);

	public void deleteCommissionByDeal(Deal deal);

	public List<Deal_DealerCommission> getAllCommissions();

	public void deleteCommissionByDealer(Dealer dealer);

	public List<Deal_DealerCommission> getAllCommissionsForDealerByDate(Date startDateObject, Date endDateObject,
			Dealer dealer);
}
