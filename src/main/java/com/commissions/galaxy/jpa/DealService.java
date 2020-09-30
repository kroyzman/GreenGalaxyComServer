package com.commissions.galaxy.jpa;

import java.util.Date;
import java.util.List;

import com.commissions.galaxy.logic.Deal;

public interface DealService {

	public Deal addNewDeal(Deal transaction) throws Exception;

	List<Deal> getAllDeals();
	public Deal getDealById(Long dealId) throws Exception;

	public void deleteDeal(Deal deal);

	public List<Deal> getAllDealsByDate(Date startDateObject, Date endDateObject);
}
