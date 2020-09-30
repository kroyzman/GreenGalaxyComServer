package com.commissions.galaxy.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commissions.galaxy.data.Deal_DealerCommission_Dao;
import com.commissions.galaxy.logic.Deal;
import com.commissions.galaxy.logic.Deal_DealerCommission;
import com.commissions.galaxy.logic.Dealer;

@Service("JpaCommissionService")
public class JpaCommissionService implements CommissionService{
	
	private Deal_DealerCommission_Dao commissionDao;
	
	
	@Autowired
	public JpaCommissionService(Deal_DealerCommission_Dao commissionDao) {
		this.commissionDao = commissionDao;
	}


	@Override
	@Transactional
	public Deal_DealerCommission addNewDealDealerCommission(Deal_DealerCommission commission) throws Exception {
		// TODO Auto-generated method stub
		return this.commissionDao.save(commission);
	}


	@Override
	@Transactional
	public List<Deal_DealerCommission> getCommissionsByDealer(Dealer dealer) {
		return (List<Deal_DealerCommission>) this.commissionDao.findByDealer(dealer);
	}


	@Override
	@Transactional
	public void deleteCommissionByDeal(Deal deal) {
		this.commissionDao.deleteByDeal(deal);
		
	}


	@Override
	@Transactional
	public List<Deal_DealerCommission> getAllCommissions() {
		return (List<Deal_DealerCommission>) this.commissionDao.findAll();
	}


	@Override
	@Transactional
	public void deleteCommissionByDealer(Dealer dealer) {
		this.commissionDao.deleteByDealer(dealer);
	}


	@Override
	@Transactional
	public List<Deal_DealerCommission> getAllCommissionsForDealerByDate(Date startDateObject, Date endDateObject,
			Dealer dealer) {
		return (List<Deal_DealerCommission>) this.commissionDao.findByDealerAndDate( startDateObject,  endDateObject,
				dealer);
	}

}
