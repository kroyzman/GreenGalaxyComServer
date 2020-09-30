package com.commissions.galaxy.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commissions.galaxy.data.DealerDao;
import com.commissions.galaxy.logic.Deal;
import com.commissions.galaxy.logic.Dealer;
import com.commissions.galaxy.logic.Dealer.PartnerSection;

@Service("JpaDealerService")
public class JpaDealerService implements DealerService {

	private DealerDao dealers;

	@Autowired
	public JpaDealerService(DealerDao dealers) {
		this.dealers = dealers;
	}

	@Override
	@Transactional
	public Dealer addNewDealer(Dealer dealer) throws Exception {
		return this.dealers.save(dealer);
	}

	@Override
	@Transactional
	public Dealer getDealerById(Long id) throws Exception {
		return this.dealers.findById(id).orElseThrow(() -> new Exception("No Such Dealer"));
	}

	@Override
	@Transactional
	public List<Dealer> getAllDealers() {
		return (List<Dealer>) this.dealers.findAll();
	}

	@Override
	@Transactional
	public List<Dealer> getDealersBySection(PartnerSection section) {
		return (List<Dealer>) this.dealers.findBySection(section);
	}

	@Override
	@Transactional
	public void deleteDealer(Dealer dealer) throws Exception {
		this.dealers.deleteById(dealer.getId());

	}

	@Override
	@Transactional
	public void updateDealer(Long dealerId, Dealer dealerNewValues) throws Exception {

		Dealer existingDealer = this.getDealerById(dealerId);

		if (dealerNewValues.getBaseCommissionRate() != existingDealer.getBaseCommissionRate()) {
			existingDealer.setBaseCommissionRate(dealerNewValues.getBaseCommissionRate());
		}

		if (dealerNewValues.getAboveCommissionRate() != existingDealer.getAboveCommissionRate()) {
			existingDealer.setAboveCommissionRate(dealerNewValues.getAboveCommissionRate());
		}

		if(dealerNewValues.getDealerAbove() == null) {
			existingDealer.setDealerAbove(null);
		} else {
			if (!dealerNewValues.getDealerAbove().equals(existingDealer.getDealerAbove())) {
				existingDealer.setDealerAbove(dealerNewValues.getDealerAbove());
			}
		}
		
		if(dealerNewValues.getSection() == null) {
			existingDealer.setSection(null);
		} else {
			if (dealerNewValues.getSection() != existingDealer.getSection()) {
				existingDealer.setSection(dealerNewValues.getSection());
			}
		}
		

		if ( dealerNewValues.getPartnerCommissionRate() != existingDealer.getPartnerCommissionRate()) {
			existingDealer.setPartnerCommissionRate(dealerNewValues.getPartnerCommissionRate());
		}

		System.out.println("Saving dealer....");
		this.dealers.save(existingDealer);

	}

}
