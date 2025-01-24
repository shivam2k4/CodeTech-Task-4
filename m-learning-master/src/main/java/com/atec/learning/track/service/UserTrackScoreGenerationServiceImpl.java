package com.atec.learning.track.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.springframework.stereotype.Service;

import com.atec.learning.track.domain.UserNote;
import com.atec.learning.track.domain.type.RdrItemType;

/**
 * calculate Score based number of total action product.
 * @author Hafedh
 * @version 1.0
 */
@Service("userTrackScoreGenerationService")
public class UserTrackScoreGenerationServiceImpl implements UserTrackScoreGenerationService{
	
	@Resource(name = "blCatalogService")
	protected CatalogService catalogService;
	
	@Override
	public double generateUserTrackScore(Long viewNumber, Long likeNumber,
			Long rateNumber, Long achatNumber, Long itemValueId,
			RdrItemType itemType, UserNote action) {
		double userScore = 0.00;// lass than or equal to 1.
		/*
		 * Create List ActionPercent {action, number, Percentage}
		 * 	___________________________________________________
		 * | action_1   | action_2   | action_3   | action_4   |
		 * |___________________________________________________|
		 * | number	    | number     | number     |  number    |
		 * |___________________________________________________|
		 * | percentage | percentage | percentage | percentage |
		 * |___________________________________________________|
		 */
		if(itemType.equals(RdrItemType.PRODUCT)){
			// sort action numbers
			//Long[] numbers = new Long[]{viewNumber, likeNumber, rateNumber, achatNumber};
			//Map<String, ActionPercent> actionNumbers = calculatePercentageForEveryActionNumber(viewNumber, likeNumber, rateNumber, achatNumber);
			Map<String, ActionPercent> percents = calculatePercentageForEveryActionNumber(viewNumber, likeNumber, rateNumber, achatNumber);
			userScore = percents.get(action.getType()).getPercentage();
		}
		return userScore;
	}
	
	protected Map<String, ActionPercent> calculatePercentageForEveryActionNumber(Long viewNumber, Long likeNumber, Long rateNumber, Long achatNumber){
		Map<String, ActionPercent> actionNumbers = new HashMap<String, ActionPercent>();
		Long total = 	(viewNumber == null 	? 	0 		: 	viewNumber) 
					+ 	(likeNumber == null 	? 	0		:	likeNumber) 
					+ 	(rateNumber == null 	? 	0		:	rateNumber) 
					+ 	(achatNumber == null 	? 	0		:	achatNumber); 
		actionNumbers.put(UserNote.VIEW.getType(),  new ActionPercent((viewNumber == null ? 0 : viewNumber)   , (viewNumber == null  ? 0 : viewNumber  / total)));
		actionNumbers.put(UserNote.LIKE.getType(), 	new ActionPercent((likeNumber == null ? 0 : likeNumber)   , (likeNumber == null  ? 0 : likeNumber  / total)));
		actionNumbers.put(UserNote.RATE.getType(), 	new ActionPercent((rateNumber == null ? 0 : rateNumber)   , (rateNumber == null  ? 0 : rateNumber  / total)));
		actionNumbers.put(UserNote.ACHAT.getType(), new ActionPercent((achatNumber == null ? 0 : achatNumber) , (achatNumber == null ? 0 : achatNumber / total)));
		return actionNumbers;
	}
	
	public class ActionPercent{

		protected Long 	 number;
		protected float  percentage;

		public ActionPercent(Long number, float percentage) {
			super();
			this.number = number;
			this.percentage = percentage;
		}
		public Long getNumber() {
			return number;
		}
		public void setNumber(Long number) {
			this.number = number;
		}
		public float getPercentage() {
			return percentage;
		}
		public void setPercentage(float percentage) {
			this.percentage = percentage;
		}
	}
}
