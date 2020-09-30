package com.commissions.galaxy.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.commissions.galaxy.logic.Dealer.PartnerSection;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Deal {
	private Long id;
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date date;
	private long invoiceNumber;
	private String state;
	private double dealCost;
	private double dealValue;
	private double currentDollarValue;
	private String customer;
	private PartnerSection dealProductsSection;
	private Dealer dealer;
	private List<Deal_DealerCommission> dealers = new ArrayList<Deal_DealerCommission>();

	public Deal() {
//		this.date = new Date();
	}
	
	
	
	
	public Deal(Date date, long invoiceNumber, String state, double dealCost, double dealValue,
			double currentDollarValue, String customer, PartnerSection dealProductsSection, Dealer dealer) {
		super();
		this.date = date;
		this.invoiceNumber = invoiceNumber;
		this.state = state;
		this.dealCost = dealCost;
		this.dealValue = dealValue;
		this.currentDollarValue = currentDollarValue;
		this.customer = customer;
		this.dealProductsSection = dealProductsSection;
		this.dealer = dealer;
	}




	public Deal(Date date, long invoiceNumber, String state, double dealCost, double dealValue, String customer,
			PartnerSection dealProductsSection, Dealer dealer, List<Deal_DealerCommission> dealers) {
		super();
		this.date = date;
		this.invoiceNumber = invoiceNumber;
		this.state = state;
		this.dealCost = dealCost;
		this.dealValue = dealValue;
		this.customer = customer;
		this.dealProductsSection = dealProductsSection;
		this.dealer = dealer;
		this.dealers = dealers;
	}
	
	


	public Deal(Date date, long invoiceNumber, String state, double dealCost, double dealValue, String customer,
			PartnerSection dealProductsSection, Dealer dealer) {
		super();
		this.date = date;
		this.invoiceNumber = invoiceNumber;
		this.state = state;
		this.dealCost = dealCost;
		this.dealValue = dealValue;
		this.customer = customer;
		this.dealProductsSection = dealProductsSection;
		this.dealer = dealer;
	}


	public Deal(Date date, long invoiceNumber, double dealValue, String customer, PartnerSection dealProductsSection,
			Dealer dealer) {
		super();
		this.date = date;
		this.invoiceNumber = invoiceNumber;
		this.dealValue = dealValue;
		this.customer = customer;
		this.dealProductsSection = dealProductsSection;
		this.dealer = dealer;
	}


	public Deal(Date date, long invoiceNumber, double dealValue, String customer, PartnerSection dealProductsSection,
			Dealer dealer, List<Deal_DealerCommission> dealers) {
		super();
		this.date = date;
		this.invoiceNumber = invoiceNumber;
		this.dealValue = dealValue;
		this.customer = customer;
		this.dealProductsSection = dealProductsSection;
		this.dealer = dealer;
		this.dealers = dealers;
	}


	@OneToOne
	public Dealer getDealer() {
		return dealer;
	}



	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}



	public double getCurrentDollarValue() {
		return currentDollarValue;
	}




	public void setCurrentDollarValue(double currentDollarValue) {
		this.currentDollarValue = currentDollarValue;
	}




	public Deal(long invoiceNumber, double dealValue, String customer, PartnerSection dealProductsSection,
			Dealer dealer) {
		super();
		this.invoiceNumber = invoiceNumber;
		this.dealValue = dealValue;
		this.customer = customer;
		this.dealProductsSection = dealProductsSection;
		this.dealer = dealer;
		this.date = new Date();
	}


	public Deal(Date date, long invoiceNumber, double dealValue, String customer,
			PartnerSection dealProductsSection) {
		super();
		this.date = date;
		this.invoiceNumber = invoiceNumber;
		this.dealValue = dealValue;
		this.customer = customer;
		this.dealProductsSection = dealProductsSection;
	}

	public Deal(Date date, long invoiceNumber, double dealValue, String customer, PartnerSection dealProductsSection,
			List<Deal_DealerCommission> dealers) {
		super();
		this.date = date;
		this.invoiceNumber = invoiceNumber;
		this.dealValue = dealValue;
		this.customer = customer;
		this.dealProductsSection = dealProductsSection;
		this.dealers = dealers;
	}

	public Deal(Long id, Date date, long invoiceNumber, double dealValue, String customer,
			PartnerSection dealProductsSection, List<Deal_DealerCommission> dealers) {
		super();
		this.id = id;
		this.date = date;
		this.invoiceNumber = invoiceNumber;
		this.dealValue = dealValue;
		this.customer = customer;
		this.dealProductsSection = dealProductsSection;
		this.dealers = dealers;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public double getDealCost() {
		return dealCost;
	}


	public void setDealCost(double dealCost) {
		this.dealCost = dealCost;
	}


	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public double getDealValue() {
		return dealValue;
	}

	public void setDealValue(double dealValue) {
		this.dealValue = dealValue;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public PartnerSection getDealProductsSection() {
		return dealProductsSection;
	}

	public void setDealProductsSection(PartnerSection dealProductsSection) {
		this.dealProductsSection = dealProductsSection;
	}
	@JsonIgnore
	@OneToMany(mappedBy = "deal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	public List<Deal_DealerCommission> getDealers() {
		return dealers;
	}

	public void setDealers(List<Deal_DealerCommission> dealers) {
		this.dealers = dealers;
	}




	@Override
	public String toString() {
		return "Deal [id=" + id + ", date=" + date + ", invoiceNumber=" + invoiceNumber + ", state=" + state
				+ ", dealCost=" + dealCost + ", dealValue=" + dealValue + ", currentDollarValue=" + currentDollarValue
				+ ", customer=" + customer + ", dealProductsSection=" + dealProductsSection + ", dealer=" + dealer
				+ ", dealers=" + dealers + "]";
	}


	
	
	

}
