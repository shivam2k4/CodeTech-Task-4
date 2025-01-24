package com.atec.learning.track.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.web.BroadleafRequestContext;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.broadleafcommerce.core.rating.domain.RatingSummary;
import org.broadleafcommerce.core.rating.service.type.RatingType;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Service;

import com.atec.learning.track.dao.UserTrackDao;
import com.atec.learning.track.domain.UserNote;
import com.atec.learning.track.domain.UserTrack;
import com.atec.learning.track.domain.type.RdrItemType;
import com.atec.learning.track.domain.type.RdrUserAction;
import com.atec.learning.track.exceptions.UserTrackExceptions;
import com.rayondart.core.rating.service.RdrRatingService;


/**
 * @author mahbouba
 *
 */
@Service("rdrUserTrackService")
public class UserTrackServiceImpl implements UserTrackService {

	public static final String BL_MAP_ACTION = "_bl_Map_action";

	private static final Log LOG = LogFactory
			.getLog(UserTrackServiceImpl.class);

	@Resource(name = "blRatingService")
	protected RdrRatingService ratingService;

	@Resource(name = "rdrUserTrackDao")
	protected UserTrackDao userTrackDao;

	@Resource(name = "blCatalogService")
	private CatalogService catalogService;

	public UserTrack create() {
		return userTrackDao.create();
	}

	
	public UserTrack readTrackById(Long userTrackId) {
		return userTrackDao.readTrackById(userTrackId);
	}

	
	public UserTrack add(UserTrack userTrack) {
		return userTrackDao.add(userTrack);
	}

	
	public void remove(UserTrack userTrack) {
		userTrackDao.remove(userTrack);
	}

	
	public UserTrack readTrackByCustomerAndItem(Customer customer,
			RdrItemType itemType, Long itemValue) {
		return userTrackDao.readTrackByCustomerAndItem(customer.getId(),
				itemType.getType(), itemValue);
	}

	
	public List<UserTrack> readUserTracksByCustomer(Customer customer) {
		return readUserTracksByCustomer(customer);
	}

	// method score calculating by user and item
	
	public double calculateScoreUserItem(UserTrack userTrack)
			throws UserTrackExceptions {
		double userScore = 0L; // final Score.
		double totalScore = 10L; // temporary this value
		LOG.debug("calculate Score for " + userTrack.getItemType().getType()
				+ " with Id = " + userTrack.getItemValue());

		if (Objects.nonNull(userTrack)) {
			if (userTrack.getTrackLike()) { // if item is viewed
				userScore = userScore
						+ TrackUtils.generateNewScore(totalScore,
								Double.parseDouble(UserNote.LIKE.getType()));
			}

			if (userTrack.getTrackView()) { // if item is liked
				userScore = userScore
						+ TrackUtils.generateNewScore(totalScore,
								Double.parseDouble(UserNote.VIEW.getType()));
			}

			if (userTrack.getTrackRated()) { // if item is rated
				RatingSummary ratingSummary = ratingService
						.readRatingSummary(userTrack.getItemValue().toString(),
								RatingType.PRODUCT);
				if (Objects.nonNull(ratingSummary)) {
					/*
					 * il ya du traitement ici : - si la valeur du rate > 2.5 =
					 * User 3ajbou el produit - si inférieur --> user ma3ejbouch
					 */
					userScore = userScore
							+ TrackUtils
									.generateNewScore(totalScore, Double
											.parseDouble(UserNote.RATE
													.getType()));
				}
			}

			if (userTrack.getTrackAchat()) { // if item is buyed.
				userScore = userScore
						+ TrackUtils.generateNewScore(totalScore,
								Double.parseDouble(UserNote.ACHAT.getType()));
			}
		}
		return userScore;// the final value of userTrack
	}

	public void updateUserProductsTracks(Map<String, String> actionsUsers)
			throws UserTrackExceptions {

		if (actionsUsers.get(RdrUserAction.VIEW.getType()) != null) {
			String[] productIds = ((String) actionsUsers.get(RdrUserAction.VIEW
					.getType())).split(",");
			trackSave(productIds);

		}
		if (actionsUsers.get(RdrUserAction.LIKE.getType()) != null) {
			String[] productIds = ((String) actionsUsers.get(RdrUserAction.LIKE
					.getType())).split(",");
			trackSave(productIds);
		}

		if (actionsUsers.get(RdrUserAction.RATE.getType()) != null) {
			String[] productIds = ((String) actionsUsers.get(RdrUserAction.RATE
					.getType())).split(",");
			trackSave(productIds);

		}

	}

	public UserTrack trackSave(String[] productIds) throws UserTrackExceptions {
		BroadleafRequestContext context = BroadleafRequestContext
				.getBroadleafRequestContext();
		// tester si la map existe ou non
		@SuppressWarnings("unchecked")
		Map<String, Object> ruleMap = (Map<String, Object>) context
				.getWebRequest().getAttribute("blRuleMap",
						context.getWebRequest().SCOPE_REQUEST);

		if (ruleMap == null) {
			LOG.trace("Creating ruleMap");
			ruleMap = new HashMap<String, Object>();

		}
		Map<String, Object> userActions = (Map<String, Object>) ruleMap
				.get(BL_MAP_ACTION);

		if (userActions != null) {
			if (!ArrayUtils.isEmpty(productIds)) {
				for (String string : productIds) {

					Long productId = Long.parseLong(string);

					Set<Entry<String, Object>> entry = userActions.entrySet();

					for (Map.Entry<String, Object> mapAction : entry) {

						LOG.trace(productId + ": ");

						LOG.trace(mapAction.getValue());
						UserTrack track = readTrackByCustomerAndItem(
								(Customer) CustomerState.getCustomer(),
								RdrItemType.PRODUCT, productId);

						// sil n'existe pas , nous allons créer une nouvelle
						// instance
						if (Objects.isNull(track)) {
							track = create();

						}
						// sinon update de la liste
						else {
							track = add(track);

						}
						track.setTrackView((Boolean) mapAction.getValue());
						double userScore = calculateScoreUserItem(track);
						track.setUserItemScore(userScore);
						return track;
					}
				}
			}
		}
		return null;

	}

}