package com.commissions.galaxy.layout;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.commissions.galaxy.jpa.CommissionService;
import com.commissions.galaxy.jpa.DealerService;
import com.commissions.galaxy.logic.Deal;
import com.commissions.galaxy.logic.Deal_DealerCommission;
import com.commissions.galaxy.logic.Dealer;

@CrossOrigin
@RestController
public class CommissionsWebController {

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

	@RequestMapping(method = RequestMethod.GET, path = "/commissions/all/sum")
	public double getCommissionsSumByCurrency(
			@RequestParam(name = "currency", required = true, defaultValue = "USD") String currency) throws Exception {

		final String USD = "USD";
		final String ILS = "ILS";
		double sum = 0;

		List<Deal_DealerCommission> commissionsList = this.commissionService.getAllCommissions();

		if (currency.equals(USD)) {
			// Currency = USD
			sum = commissionsList.stream().mapToDouble(commission -> commission.getDealCommissionAmountForDealer())
					.sum();
			return sum;
		}

		// Currency = ILS
		sum = commissionsList.stream().mapToDouble(commission -> commission.getDealCommissionAmountForDealer()
				* commission.getDeal().getCurrentDollarValue()).sum();
		return sum;

	}

	@RequestMapping(method = RequestMethod.GET, path = "/commissions/byDate/{dealerId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Deal_DealerCommission> getCommissionForDealerByDate(@PathVariable("dealerId") Long dealerId,
			@RequestParam(name = "start_date", required = true, defaultValue = "2019-01-01") String startDate,
			@RequestParam(name = "end_date", required = true, defaultValue = "2021-01-01") String endDate)
			throws Exception {

		Date startDateObject = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
		Date endDateObject = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
		Dealer dealer = this.dealerService.getDealerById(dealerId);
		List<Deal_DealerCommission> commissionList = this.commissionService
				.getAllCommissionsForDealerByDate(startDateObject, endDateObject, dealer);
		System.out.println(commissionList.size());
		return commissionList;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/commissions/{dealerId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Deal_DealerCommission> getCommissionsByDealer(@PathVariable("dealerId") Long dealerId)
			throws Exception {
		Dealer dealer = this.dealerService.getDealerById(dealerId);
		return this.commissionService.getCommissionsByDealer(dealer);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/report/Commission Report/{dealerId}")
	public void getExcel(HttpServletResponse response, @PathVariable("dealerId") Long dealerId,
			@RequestParam(name = "currency", required = true, defaultValue = "USD") String currency) throws Exception {

		Dealer dealer = this.dealerService.getDealerById(dealerId);
		List<Deal_DealerCommission> commissions = this.commissionService.getCommissionsByDealer(dealer);
		List<Deal_DealerCommission> directCommissions = getDirectCommissions(commissions);
		List<Deal_DealerCommission> derivedCommissions = getDerivedCommissions(commissions);
		List<Deal_DealerCommission> partnerCommissions = getPartnerCommissions(commissions);

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition",
				"attachment; filename=Commission Report - " + dealer.getName() + ".xlsx");

		ByteArrayInputStream inputStream = ExcelReportView.customersToExcel(dealer, commissions, directCommissions,
				derivedCommissions, partnerCommissions, currency);

		IOUtils.copy(inputStream, response.getOutputStream());

	}

	private List<Deal_DealerCommission> getPartnerCommissions(List<Deal_DealerCommission> commissions) {
		List<Deal_DealerCommission> partnerCommissions = new ArrayList<Deal_DealerCommission>();
		for (Deal_DealerCommission commission : commissions) {
			if (commission.getDealer().getName() != commission.getDeal().getDealer().getName()
					&& commission.getDealer().getSection() == commission.getDeal().getDealProductsSection()) {
				partnerCommissions.add(commission);
			}
		}
		return partnerCommissions;
	}

	private List<Deal_DealerCommission> getDerivedCommissions(List<Deal_DealerCommission> commissions) {
		List<Deal_DealerCommission> derivedCommissions = new ArrayList<Deal_DealerCommission>();
		for (Deal_DealerCommission commission : commissions) {
			if (commission.getDealer().getName() != commission.getDeal().getDealer().getName()
					&& commission.getDealer().getSection() != commission.getDeal().getDealProductsSection()) {
				derivedCommissions.add(commission);
			}
		}
		return derivedCommissions;
	}

	private List<Deal_DealerCommission> getDirectCommissions(List<Deal_DealerCommission> commissions) {
		List<Deal_DealerCommission> directCommissions = new ArrayList<Deal_DealerCommission>();
		for (Deal_DealerCommission commission : commissions) {
			if (commission.getDealer().getName() == commission.getDeal().getDealer().getName()) {
				directCommissions.add(commission);
			}
		}
		return directCommissions;
	}
}
