package com.commissions.galaxy.jpa;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.commissions.galaxy.data.DealDao;
import com.commissions.galaxy.logic.Deal;


@Service("JpaDealService")
public class JpaDealService implements DealService {
	
	private DealDao deals;
	
	@Autowired
	public JpaDealService(DealDao deals) {
		this.deals = deals;
	}

	@Override
	@Transactional
	public Deal addNewDeal(Deal deal) throws Exception {
		return this.deals.save(deal);
	}

	@Override
	@Transactional
	public List<Deal> getAllDeals() {
		return (List<Deal>) this.deals.findAll();
	}

	@Override
	@Transactional
	public Deal getDealById(Long dealId) throws Exception {
		return this.deals.findById(dealId).orElseThrow(() -> new Exception("Deal Not Found"));
	}

	@Override
	@Transactional
	public void deleteDeal(Deal deal) {
		this.deals.deleteById(deal.getId());
	}

	@Override
	@Transactional
	public List<Deal> getAllDealsByDate(Date startDateObject, Date endDateObject) {
		return this.deals.findAllByDateBetween(startDateObject, endDateObject);
	}
	

}
