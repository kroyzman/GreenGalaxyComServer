package com.commissions.galaxy.logic;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class Deal_DealerCommissionId implements Serializable {
	private Long dealerId;
	private Long dealId;
	
	@Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Deal_DealerCommissionId that = (Deal_DealerCommissionId) o;
        return Objects.equals(dealerId, that.dealerId) && Objects.equals(dealId, that.dealId);
    }
	
	 @Override
	    public int hashCode() {
	        return Objects.hash(dealerId, dealId);
	    }
}
