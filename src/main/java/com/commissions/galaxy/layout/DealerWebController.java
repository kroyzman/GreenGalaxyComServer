package com.commissions.galaxy.layout;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.commissions.galaxy.jpa.CommissionService;
import com.commissions.galaxy.jpa.DealerService;
import com.commissions.galaxy.logic.Deal;
import com.commissions.galaxy.logic.Dealer;

@CrossOrigin
@RestController
public class DealerWebController {

	private DealerService dealerService;
	private CommissionService commissionService;
	
	public CommissionService getCommissionService() {
		return commissionService;
	}

	@Autowired
	public void setCommissionService(CommissionService commissionService) {
		this.commissionService = commissionService;
	}
	
	@Autowired
	public void setDealerService(DealerService dealerService) {
		this.dealerService = dealerService;
	}

	@RequestMapping(method = RequestMethod.POST, path = "/dealers/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Dealer addNewDealer(@RequestBody Dealer dealer) throws Exception {
		return dealerService.addNewDealer(dealer);
	}
	
	
	@RequestMapping(method = RequestMethod.GET, path = "/dealers/dealers_all", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Dealer> getAllDealers() throws Exception {

		List<Dealer> dealersList = this.dealerService.getAllDealers();
		return dealersList;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/dealers/dealers_count")
	public int getDealersCount() throws Exception {

		List<Dealer> dealersList = this.dealerService.getAllDealers();
		return dealersList.size();
	}
	
	@RequestMapping(method = RequestMethod.PUT, path = "/dealers/update/{dealerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<Dealer> updateDealer(@PathVariable("dealerId") Long dealerId, @RequestBody Dealer dealer) throws Exception {
		dealerService.updateDealer(dealerId, dealer);
		return this.getAllDealers();
	}

	
	@RequestMapping(method = RequestMethod.DELETE, path = "/dealers/delete/{dealerId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Dealer deleteDealerById(@PathVariable("dealerId") Long dealerId) throws Exception {

		Dealer dealer = this.dealerService.getDealerById(dealerId);
		
		//Delete all commissions for dealer
		this.commissionService.deleteCommissionByDealer(dealer);
		
		
		//Delete Dealer
		try {
			this.dealerService.deleteDealer(dealer);
		} catch (DataIntegrityViolationException err) {
			throw new ResponseStatusException(
			           HttpStatus.CONFLICT, "Cant delete dealer with existing deals", err);
		} catch (Exception err) {
			System.out.println(err);
		}
		
		
		return dealer;
	}

}
