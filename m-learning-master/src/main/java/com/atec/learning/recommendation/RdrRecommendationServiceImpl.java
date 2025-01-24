package com.atec.learning.recommendation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.atec.learning.dataSource.util.DatabaseConnexionUtil;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * 
 * @author mahbouba
 *
 */

@Service("rdrRecommendationService")
public class RdrRecommendationServiceImpl implements RdrRecommendationService {

	@Value("${recommendation.items}")
	protected static int nb_recommendationItems;

	@Value("${recommendation.neighboorhood}")
	protected static int nb_recommendationneighborhood;
	
	@Resource(name = "dataBaseConnexionUtil")
	protected DatabaseConnexionUtil dataBaseConnexionUtil;
	
	@Resource(name = "userRecommendationPreferenceService")
	protected UserRecommendationPreferenceService userRecommendationPreferenceService;

	private static final Log LOG = LogFactory.getLog(RdrRecommendationServiceImpl.class);

	public List<Long> recommendation() throws IOException, TasteException {
		// Create a data source from the Mysql
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setServerName("localhost");
		dataSource.setUser("root");
		dataSource.setPassword("");
		dataSource.setDatabaseName("Broadleaf");
		JDBCDataModel dataModel = new MySQLJDBCDataModel(dataSource,
				"user_track", "CUSTOMER", "ItemValue", "UserItemScore", null);

		// Creating UserSimilarity object.
		UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);

		// Creating UserNeighbourHood object .
		// construction des voisinage d'utilisateurs

		UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(nb_recommendationneighborhood, userSimilarity, dataModel);

		// Create a generic user based recommender with the dataModel, the
		// userNeighborhood and the userSimilarity
		Recommender genericRecommender = new GenericUserBasedRecommender(
				dataModel, userNeighborhood, userSimilarity);
		List<Long> listItems= new ArrayList<Long>();
		// Recommend the items for each user
		for (LongPrimitiveIterator iterator = dataModel.getUserIDs(); iterator
				.hasNext();) {
			Long userId = CustomerState.getCustomer().getId();

			// Generate a list of recommendations for the user
			List<RecommendedItem> itemRecommendations = genericRecommender.recommend(userId, nb_recommendationItems);

			LOG.trace("User Id: %n" + userId);

			if (itemRecommendations.isEmpty()) {
				LOG.trace("No recommendations for this user.");
			} else {
				// Display the list of recommendations
				
				
				for (RecommendedItem recommendedItem : itemRecommendations) {
					LOG.trace("Recommened Item Id ."
							+ recommendedItem.getItemID()
							+ " Strength of the preference: %n"
							+recommendedItem.getValue());
				
					listItems.add(recommendedItem.getItemID());

				}
			}

		}
		return listItems;
	}

	public List<Long> userItemsrecommendation(Long userId) {
		JDBCDataModel dataModel = dataBaseConnexionUtil.jdbcDataModelConnexion();
		return null;
	}

	public List<Long> userItemsRecommendationGenericDataModel(Long userId) throws TasteException {
		FastByIDMap<PreferenceArray> preferences = userRecommendationPreferenceService.programmaticallyInputDataGenericDataModel(userId);
		List<Long> recommendations = new ArrayList<Long>();
		if(Objects.nonNull(preferences)){
			DataModel model = new GenericDataModel(preferences);
			UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(model);
			UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(nb_recommendationneighborhood, userSimilarity, model);
			// Create a generic user based recommender with the dataModel, the
			// userNeighborhood and the userSimilarity
			Recommender genericRecommender = new GenericUserBasedRecommender(model, userNeighborhood, userSimilarity);
			List<RecommendedItem> itemRecommendations = genericRecommender.recommend(userId, nb_recommendationItems);
			itemRecommendations.forEach((item) -> {
				recommendations.add(item.getItemID());
			});
		}
		return recommendations;
	}
}
