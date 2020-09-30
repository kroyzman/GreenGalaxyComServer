package com.commissions.galaxy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.commissions.galaxy.data.DealDao;
import com.commissions.galaxy.data.Deal_DealerCommission_Dao;
import com.commissions.galaxy.data.DealerDao;
import com.commissions.galaxy.layout.DealWebController;
import com.commissions.galaxy.layout.DealerWebController;
import com.commissions.galaxy.logic.Deal;
import com.commissions.galaxy.logic.Deal_DealerCommission;
import com.commissions.galaxy.logic.Dealer;
import com.commissions.galaxy.logic.Dealer.PartnerSection;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@SpringBootApplication
public class GalaxyCommissionSystemApplication {
	private static final Logger log = LoggerFactory.getLogger(GalaxyCommissionSystemApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GalaxyCommissionSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(DealerDao dealersDao, DealDao dealDao, Deal_DealerCommission_Dao commissionDao,
			DealWebController dealWebController) {
		return (args) -> {
			final int DEALERS_COUNT = 3;
			final int DEALS_COUNT = 10;
//			Dealer nadi = new Dealer("Nadi", 10, null, 15D, null, null);
//			Dealer benny = new Dealer("Benny", 15, null, null, null, null);
//			Dealer moshe = new Dealer("Moshe", 12, nadi, 20D, null, null);
//			Dealer eran = new Dealer("Eran Ben Zeev", 10, null, 15D, 33D, PartnerSection.COSMETICS);
//			dealersDao.save(eran);
//
//			Dealer doron = new Dealer("Doron Kedem", 10, eran, 15D, null, null);
//			Dealer yehiel = new Dealer("Yehiel", 10, eran, 15D, null, null);
//
//			dealersDao.save(doron);
//			dealersDao.save(yehiel);
			
//			Dealer[] dealers = new Dealer[3];
//			dealers[0] = eran;
//			dealers[1] = doron;
//			dealers[2] = yehiel;
//			
//			
//			Deal deal1 = new Deal(new Date(), 1000L, "USA", 1000, 10000, 3.454, "Benny", PartnerSection.COSMETICS,
//					eran);
//			Deal deal2 = new Deal(new Date(), 2000L, "Mexico", 1000, 9000, 3.454, "Adi", PartnerSection.INDUSTRY, eran);
//			Deal deal3 = new Deal(new Date(), 3000L, "England", 1000, 8000, 3.454, "Dani", PartnerSection.COSMETICS,
//					doron);
//			Deal deal4 = new Deal(new Date(), 4000L, "Japan", 1000, 4000, 3.454, "Philip", PartnerSection.RAIL, doron);
//			Deal deal5 = new Deal(new Date(), 5000L, "USA", 1000, 5000, 3.454, "Rami", PartnerSection.RAIL, doron);
//			Deal deal6 = new Deal(new Date(), 6000L, "England", 1000, 6000, 3.454, "Dana", PartnerSection.SPECIAL,
//					yehiel);
//			Deal deal7 = new Deal(new Date(), 7000L, "Japan", 1000, 7000, 3.454, "Kobi", PartnerSection.COSMETICS,
//					yehiel);

//			dealWebController.addNewDeal(deal1);
//			dealWebController.addNewDeal(deal2);
//			dealWebController.addNewDeal(deal3);
//			dealWebController.addNewDeal(deal4);
//			dealWebController.addNewDeal(deal5);
//			dealWebController.addNewDeal(deal6);
//			dealWebController.addNewDeal(deal7);

			RestTemplate restTamplate = new RestTemplate();
			ObjectMapper mapper = new ObjectMapper();
			
			ResponseEntity<String> responseDeals = restTamplate
					.getForEntity("https://api.mockaroo.com/api/83488a10?count=" + DEALS_COUNT + "&key=1728e220", String.class);

			ResponseEntity<String> response = restTamplate
					.getForEntity("https://api.mockaroo.com/api/1a597440?count=" + DEALERS_COUNT + "&key=1728e220", String.class);
			
			
			
			//DEALERS
			JsonNode rootDealers = mapper.readTree(response.getBody());
			
			ArrayList<Dealer> dealersList = new ArrayList<>();
			int i = 0;
			if (rootDealers.isArray()) {
				ArrayNode arrayNode = (ArrayNode) rootDealers;
				Iterator<JsonNode> node = arrayNode.elements();

				while (node.hasNext()) {
					
					JsonNode current = node.next();
//				          System.out.println(node.next().get("invoiceNumber").asLong());
					String name = current.get("name").asText();
					double baseCommissionRate = current.get("baseCommissionRate").asDouble();
					Double aboveCommissionRate = current.get("aboveCommissionRate").asDouble();
					Double partnerCommissionRate = current.get("partnerCommissionRate").asDouble();
					String dealProductsSection = current.get("section").asText();
					
					if(aboveCommissionRate != 0 && i != 0) {
						
						Random rand = new Random(); 
						int rand_int1 = rand.nextInt(i);
						if(!dealProductsSection.equalsIgnoreCase("null")) {
							
							dealersList.add(new Dealer(name, baseCommissionRate, dealersList.get(rand_int1), aboveCommissionRate, partnerCommissionRate, PartnerSection.valueOf(dealProductsSection)));
						} else {
							dealersList.add(new Dealer(name, baseCommissionRate, dealersList.get(rand_int1), aboveCommissionRate, partnerCommissionRate, null));
						}
						
					} else {
						if(!dealProductsSection.equalsIgnoreCase("null")) {
							dealersList.add(new Dealer(name, baseCommissionRate, null, aboveCommissionRate, partnerCommissionRate, PartnerSection.valueOf(dealProductsSection)));
						} else {
							dealersList.add(new Dealer(name, baseCommissionRate, null, aboveCommissionRate, partnerCommissionRate, null));
						}
						
					}
					
					i++;
				}
			}
			
			dealersList.forEach((dealer) -> {
				dealersDao.save(dealer);
			});
			
			//DEALS
			JsonNode rootDeals = mapper.readTree(responseDeals.getBody());

			if (rootDeals.isArray()) {
				ArrayNode arrayNode = (ArrayNode) rootDeals;
				Iterator<JsonNode> node = arrayNode.elements();

				while (node.hasNext()) {
					JsonNode current = node.next();
//				          System.out.println(node.next().get("invoiceNumber").asLong());
					long invoiceNumber = current.get("invoiceNumber").asLong();
					String state = current.get("state").asText();
					double dealValue = current.get("dealValue").asDouble();
					double dealCost = current.get("dealCost").asDouble();
					double dollar = current.get("currentDollarValue").asDouble();
					String customer = current.get("customer").asText();
					String date = current.get("date").asText();
					String dealProductsSection = current.get("dealProductsSection").asText();
					
					System.out.println("fetched date: " +date);
					 Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(date);
					 System.out.println("parsed date: "+date1);
					
					 
					Random rand = new Random(); 
					int rand_int1 = rand.nextInt(DEALERS_COUNT);
					
					dealWebController.addNewDeal(new Deal(date1, invoiceNumber, state, dealCost, dealValue, dollar, customer,
							PartnerSection.valueOf(dealProductsSection), dealersList.get(rand_int1)));
				}
			}
//				List<Deal> asList = mapper.readValue(root, Deal.class);
//				JsonNode invoiceNumber = root.path("invoiceNumber");
//				System.out.println(invoiceNumber.asLong());

//				for (final JsonNode objNode : recipesArr) {
//					
//				}

//
//			Deal_DealerCommission commission1 = new Deal_DealerCommission(deal1, nadi, nadi.getBaseCommissionRate(),
//					deal1.getDealValue() * nadi.getBaseCommissionRate() / 100);
//			Deal_DealerCommission commission2 = new Deal_DealerCommission(deal1, benny, benny.getBaseCommissionRate(),
//					deal1.getDealValue() * benny.getBaseCommissionRate() / 100);
//			Deal_DealerCommission commission3 = new Deal_DealerCommission(deal2, nadi, nadi.getBaseCommissionRate(),
//					deal1.getDealValue() * nadi.getBaseCommissionRate() / 100);

			// First Implementation

//			dealersDao.save(miki);
//			dealersDao.save(roni);
//			dealersDao.save(haiim);
//			
//			dealDao.save(deal1);
//			dealDao.save(deal2);
//			dealDao.save(deal3);

//			commissionDao.save(commission1);
//			commissionDao.save(commission2);
//			commissionDao.save(commission3);

			// Commission above for nadi
//			Dealer aboveDealer = dealersDao.findById(deal3.getDealer().getDealerAbove().getId())
//					.orElseThrow(() -> new Exception());
//			Deal_DealerCommission commission4 = new Deal_DealerCommission(deal3, aboveDealer,
//					aboveDealer.getAboveCommissionRate(),
//					(deal3.getDealValue() - (deal3.getDealValue() * deal3.getDealer().getBaseCommissionRate() / 100))
//							* aboveDealer.getAboveCommissionRate() / 100);
//
//			commissionDao.save(commission4);
//
//			List<Dealer> partners = dealersDao.findBySection(PartnerSection.COSMETICS);
//			System.out.println("COSMETICS : " + partners.size());
//			
//			List<Dealer> partnersInd = dealersDao.findBySection(PartnerSection.INDUSTRY);
//			System.out.println("INDUSTRY : " + partnersInd.size());
//			
//			dealersDao.findByName("Nadi").forEach(dealer -> {
//				log.info(dealer.getName());
//			});
			// Second Implementation

//	    	commissionDao.save(new Deal_DealerCommission(deal1, nadi, 5, 200));
//	    	nadi.getDeals().add(commission1);
//	    	deal1.getDealers().add(commission1);
//	    	dealDao.save(deal1);
//	    	dealersDao.save(nadi);

//	    	
//	    	
//	    	// save a few deals
//	    	

		};
	}

}
