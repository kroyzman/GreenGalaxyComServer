package com.commissions.galaxy.jpa;


import java.util.List;

import com.commissions.galaxy.logic.Deal;
import com.commissions.galaxy.logic.Dealer;
import com.commissions.galaxy.logic.Dealer.PartnerSection;



public interface DealerService {
	public Dealer addNewDealer(Dealer dealer) throws Exception;
	public Dealer getDealerById(Long id) throws Exception;
	public List<Dealer> getDealersBySection(PartnerSection section);
	List<Dealer> getAllDealers();
	public void deleteDealer(Dealer dealer) throws Exception;
	public void updateDealer(Long dealerId, Dealer dealer) throws Exception;

}
