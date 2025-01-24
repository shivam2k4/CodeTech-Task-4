package com.atec.learning.recommendation;

import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;

/**
 * 
 * @author mahbouba
 * 
 * 
 * @author Hafedh
 * {@link #userItemsrecommendation(Long)}
 */


public interface RdrRecommendationService {
	
	public List<Long> recommendation() throws IOException, TasteException;
	
	public List<Long> userItemsrecommendation(Long userId);
	
	public List<Long> userItemsRecommendationGenericDataModel(Long userId) throws TasteException;
	
}
