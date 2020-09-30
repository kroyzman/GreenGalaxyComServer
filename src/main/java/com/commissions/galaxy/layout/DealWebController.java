package com.commissions.galaxy.layout;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.commissions.galaxy.jpa.CommissionService;
import com.commissions.galaxy.jpa.DealService;
import com.commissions.galaxy.jpa.DealerService;
import com.commissions.galaxy.logic.Deal;
import com.commissions.galaxy.logic.Deal_DealerCommission;
import com.commissions.galaxy.logic.Dealer;

@CrossOrigin
@RestController
public class DealWebController {

	private DealService dealService;
	private CommissionService commissionService;
	private DealerService dealerService;

	public DealerService getDealerService() {
		return dealerService;
	}

	@Autowired
	public void setDealerService(DealerService dealerService) {
		this.dealerService = dealerService;
	}

	public CommissionService getCommissionService() {
		return commissionService;
	}

	@Autowired
	public void setCommissionService(CommissionService commissionService) {
		this.commissionService = commissionService;
	}

	public DealService getDealService() {
		return dealService;
	}

	@Autowired
	public void setDealService(DealService dealService) {
		this.dealService = dealService;
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/deals/delete/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Deal deleteDealById(@PathVariable("dealId") Long dealId) throws Exception {

		System.out.println("HERE");

		Deal deal = this.dealService.getDealById(dealId);
		System.out.println(deal);

		// Delete all commissions for deal
		this.commissionService.deleteCommissionByDeal(deal);

		// Delete Deal
		this.dealService.deleteDeal(deal);

		return deal;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/deals/deals_all", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Deal> getAllDeals() throws Exception {

		List<Deal> dealsList = this.dealService.getAllDeals();
		System.out.println(dealsList.get(0));
		return dealsList;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/deals/deals_all/date", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Deal> getDealsByDate(
			@RequestParam(name = "start_date", required = true, defaultValue = "2019-01-01") String startDate,
			@RequestParam(name = "end_date", required = true, defaultValue = "2021-01-01") String endDate)
			throws Exception {
		Date startDateObject = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
		Date endDateObject = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

		List<Deal> dealsList = this.dealService.getAllDealsByDate(startDateObject, endDateObject);
		System.out.println(dealsList.size());
		return dealsList;
	}

//	@RequestMapping(method = RequestMethod.GET, path = "/deals/dealsAmount")
//	public double getDealsAmount() throws Exception {
//
//		List<Deal> dealsList = this.dealService.getAllDeals();
//		
//		double sum = dealsList.stream().mapToDouble(deal -> deal.getDealValue()).sum();
//	
//		return sum;
//	}

	@RequestMapping(method = RequestMethod.POST, path = "/deals/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Deal addNewDeal(@RequestBody Deal deal) throws Exception {

		Deal returnedDeal = this.dealService.addNewDeal(deal);
		System.out.println(returnedDeal);
		handleCommissionsForDealers(returnedDeal, returnedDeal.getDealer());

		return returnedDeal;
	}

	private void handleCommissionsForDealers(Deal deal, Dealer dealer) throws Exception {

		Deal_DealerCommission commission;

		// Check if im a partner
		commission = getPartnerCommission(deal, dealer);

		// if commission partner not null --> save commission and finish
		if (commission != null) {
			System.out.println("IM A PARTNER !!!!!!");
			this.commissionService.addNewDealDealerCommission(commission);
			return;
		} else {
			System.out.println("IM NOT A PARTNER !!!!!!");
			// Check if there's dealer above
			commission = getDealerAboveCommission(deal, dealer);
			if (commission != null) {
				System.out.println("DEALER ABOVE GOT COMMISSION!!!!!!");
				this.commissionService.addNewDealDealerCommission(commission);
			}
			// Check if there's partners who need to receive commissions and handle this...
			handleAllPartnersCommissions(dealer, deal);
		}

		// Handle my commission if im not a partner
		handleDirectDealerCommission(deal, dealer);

	}

	private void handleDirectDealerCommission(Deal deal, Dealer dealer) throws Exception {
		double commissionRateForDealer, commissionAmountForDealer;
		Deal_DealerCommission commission;
		commissionRateForDealer = dealer.getBaseCommissionRate();
		commissionAmountForDealer = deal.getDealValue() * commissionRateForDealer / 100;
		commission = new Deal_DealerCommission(deal, dealer, commissionRateForDealer, commissionAmountForDealer);
		this.commissionService.addNewDealDealerCommission(commission);
		System.out.println("I GOT MY COMMISSION!!!");
	}

	private void handleAllPartnersCommissions(Dealer dealer, Deal deal) {
		List<Dealer> partners = this.dealerService.getDealersBySection(deal.getDealProductsSection());
		if (!partners.isEmpty()) {
			System.out.println("PARTNERS GET COMMISSIONS!!!");
			partners.forEach(partner -> {
				double commissionRateForPartner, commissionAmountForPartner;
				commissionRateForPartner = partner.getPartnerCommissionRate();
				commissionAmountForPartner = commissionRateForPartner / 100 * (deal.getDealValue() - deal.getDealCost());
				if(commissionAmountForPartner < 0) {
					commissionAmountForPartner = 0;
				}
				Deal_DealerCommission partnerCommission = new Deal_DealerCommission(deal, partner,
						commissionRateForPartner, commissionAmountForPartner);
				try {
					this.commissionService.addNewDealDealerCommission(partnerCommission);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

	private Deal_DealerCommission getDealerAboveCommission(Deal deal, Dealer dealer) {
		double commissionRateForDealerAbove, commissionAmountForDealerAbove;
		Deal_DealerCommission commission;
		Dealer dealerAbove = dealer.getDealerAbove();

		// If there's dealer above and he's not a partner with the deal's section
		if (dealerAbove != null && dealerAbove.getSection() != deal.getDealProductsSection()) {
			commissionRateForDealerAbove = dealerAbove.getAboveCommissionRate();
			commissionAmountForDealerAbove = (deal.getDealValue()
					- (deal.getDealValue() * dealer.getBaseCommissionRate() / 100)) * commissionRateForDealerAbove
					/ 100;
			commission = new Deal_DealerCommission(deal, dealerAbove, commissionRateForDealerAbove,
					commissionAmountForDealerAbove);
			return commission;
		}

		System.out.println("I DONT HAVE A DEALER ABOVE ME!!!!!!");
		return null;

	}

	private Deal_DealerCommission getPartnerCommission(Deal deal, Dealer dealer) {
		double commissionRateForPartner, commissionAmountForPartner;
		Deal_DealerCommission commission;
		if (dealer.getPartnerCommissionRate() != null && dealer.getSection() == deal.getDealProductsSection()) {
			// Return the commission obj for the partner
			commissionRateForPartner = dealer.getPartnerCommissionRate();
			commissionAmountForPartner = commissionRateForPartner / 100 * (deal.getDealValue() - deal.getDealCost());
			if(commissionAmountForPartner < 0) {
				commissionAmountForPartner = 0;
			}
			commission = new Deal_DealerCommission(deal, dealer, commissionRateForPartner, commissionAmountForPartner);
			return commission;
		}
		return null;

	}

}
