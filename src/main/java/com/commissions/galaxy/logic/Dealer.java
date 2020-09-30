package com.commissions.galaxy.logic;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Dealer {
	private Long id;
	private String name;
	private List<Deal_DealerCommission> deals = new ArrayList<Deal_DealerCommission>();
	private double baseCommissionRate;
	private Dealer dealerAbove;
	private Double aboveCommissionRate;
	private Double partnerCommissionRate;

	public enum PartnerSection {
		COSMETICS, INDUSTRY, SPECIAL, RAIL, AVIATION;
	}

	private PartnerSection section;

	public Dealer() {
	}

	public Dealer(String name, double baseCommissionRate, Dealer dealerAbove, Double aboveCommissionRate,
			Double partnerCommissionRate, PartnerSection section) {
		super();
		this.name = name;
		this.baseCommissionRate = baseCommissionRate;
		this.dealerAbove = dealerAbove;
		this.aboveCommissionRate = aboveCommissionRate;
		this.partnerCommissionRate = partnerCommissionRate;
		this.section = section;
	}

	public Dealer(String name, List<Deal_DealerCommission> deals, double baseCommissionRate, Dealer dealerAbove,
			Double aboveCommissionRate, Double partnerCommissionRate, PartnerSection section) {
		super();
		this.name = name;
		this.deals = deals;
		this.baseCommissionRate = baseCommissionRate;
		this.dealerAbove = dealerAbove;
		this.aboveCommissionRate = aboveCommissionRate;
		this.partnerCommissionRate = partnerCommissionRate;
		this.section = section;
	}

	public Dealer(Long id, String name, List<Deal_DealerCommission> deals, double baseCommissionRate,
			Dealer dealerAbove, Double aboveCommissionRate, Double partnerCommissionRate, PartnerSection section) {
		super();
		this.id = id;
		this.name = name;
		this.deals = deals;
		this.baseCommissionRate = baseCommissionRate;
		this.dealerAbove = dealerAbove;
		this.aboveCommissionRate = aboveCommissionRate;
		this.partnerCommissionRate = partnerCommissionRate;
		this.section = section;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonIgnore
	@OneToMany(mappedBy = "dealer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	public List<Deal_DealerCommission> getDeals() {
		return deals;
	}

	public void setDeals(List<Deal_DealerCommission> deals) {
		this.deals = deals;
	}

	public double getBaseCommissionRate() {
		return baseCommissionRate;
	}

	public void setBaseCommissionRate(double baseCommissionRate) {
		this.baseCommissionRate = baseCommissionRate;
	}
	
	@OneToOne
	public Dealer getDealerAbove() {
		return dealerAbove;
	}

	public void setDealerAbove(Dealer dealerAbove) {
		this.dealerAbove = dealerAbove;
	}

	public Double getAboveCommissionRate() {
		return aboveCommissionRate;
	}

	public void setAboveCommissionRate(Double aboveCommissionRate) {
		this.aboveCommissionRate = aboveCommissionRate;
	}

	public Double getPartnerCommissionRate() {
		return partnerCommissionRate;
	}

	public void setPartnerCommissionRate(Double partnerCommissionRate) {
		this.partnerCommissionRate = partnerCommissionRate;
	}

	public PartnerSection getSection() {
		return section;
	}

	public void setSection(PartnerSection section) {
		this.section = section;
	}

	@Override
	public String toString() {
		return "Dealer [id=" + id + ", name=" + name + ", deals=" + deals + ", baseCommissionRate=" + baseCommissionRate
				+ ", dealerAbove=" + dealerAbove + ", aboveCommissionRate=" + aboveCommissionRate
				+ ", partnerCommissionRate=" + partnerCommissionRate + ", section=" + section + "]";
	}
	
	

}