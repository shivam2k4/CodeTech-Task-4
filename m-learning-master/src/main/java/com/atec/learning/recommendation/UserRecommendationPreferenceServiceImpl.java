package com.atec.learning.recommendation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.service.CustomerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.atec.learning.track.domain.UserTrack;
import com.atec.learning.track.service.UserTrackService;

@Service("userRecommendationPreferenceService")
public class UserRecommendationPreferenceServiceImpl implements UserRecommendationPreferenceService {
	
	@Resource(name = "rdrUserTrackService")
	protected UserTrackService userTrackService;
	
	@Resource(name = "blCustomerService")
	protected CustomerService customerService;
	
	@Value("${recommendation.user.preference.array}")
	protected int userPreferenceArray = 10;
	
	public FastByIDMap<PreferenceArray> programmaticallyInputDataGenericDataModel(Long userId) {
		if(Objects.nonNull(userId)){
			FastByIDMap<PreferenceArray> preferences = new FastByIDMap<PreferenceArray>();
				Customer customer = customerService.readCustomerById(userId);
				List<UserTrack> tracks = userTrackService.readUserTracksByCustomer(customer);
				tracks = tracks.stream()
							.limit(userPreferenceArray)
							.collect(Collectors.toList());

				int count = 0;
				PreferenceArray prefsForUser = new GenericUserPreferenceArray(userPreferenceArray);
				tracks.forEach((track) -> {
					prefsForUser.setUserID(count, track.getCustomer().getId());
					prefsForUser.setItemID(count, track.getItemValue());
					prefsForUser.setValue(count, (float) track.getUserItemScore());
					//count++;
				});
				preferences.put(userId, prefsForUser);
			return preferences;
		}
		return null;
	}

}
