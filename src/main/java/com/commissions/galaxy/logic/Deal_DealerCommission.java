package com.commissions.galaxy.logic;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity(name = "Commissions")
@Table(name = "Commissions")
public class Deal_DealerCommission implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private Deal_DealerCommissionId id = new Deal_DealerCommissionId();
	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("dealId")
	private Deal deal;
	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("dealerId")
	private Dealer dealer;
	private double dealerCommissionRate;
	private double dealCommissionAmountForDealer;

	public Deal_DealerCommission() {

	}

	public Deal_DealerCommission(Deal deal, Dealer dealer) {
		super();
		this.deal = deal;
		this.dealer = dealer;
	}

	public Deal_DealerCommission(Deal_DealerCommissionId id, Deal deal, Dealer dealer, double dealerCommissionRate,
			double dealCommissionAmountForDealer) {
		super();
		this.id = id;
		this.deal = deal;
		this.dealer = dealer;
		this.dealerCommissionRate = dealerCommissionRate;
		this.dealCommissionAmountForDealer = dealCommissionAmountForDealer;
	}

	public Deal_DealerCommission(Deal deal, Dealer dealer, double dealerCommissionRate,
			double dealCommissionAmountForDealer) {
		super();
		this.deal = deal;
		this.dealer = dealer;
		this.dealerCommissionRate = dealerCommissionRate;
		this.dealCommissionAmountForDealer = dealCommissionAmountForDealer;
	}

	
	public Deal_DealerCommissionId getId() {
		return id;
	}

	public void setId(Deal_DealerCommissionId id) {
		this.id = id;
	}

	
	public Deal getDeal() {
		return deal;
	}

	public void setDeal(Deal deal) {
		this.deal = deal;
	}

	
	public Dealer getDealer() {
		return dealer;
	}

	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}

	public double getDealerCommissionRate() {
		return dealerCommissionRate;
	}

	public void setDealerCommissionRate(double dealerCommissionRate) {
		this.dealerCommissionRate = dealerCommissionRate;
	}

	public double getDealCommissionAmountForDealer() {
		return dealCommissionAmountForDealer;
	}

	public void setDealCommissionAmountForDealer(double dealCommissionAmountForDealer) {
		this.dealCommissionAmountForDealer = dealCommissionAmountForDealer;
	}

}
