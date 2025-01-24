package com.atec.learning.recommendation;

import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.model.PreferenceArray;

public interface UserRecommendationPreferenceService {
	
	public FastByIDMap<PreferenceArray> programmaticallyInputDataGenericDataModel(Long userId);
}
