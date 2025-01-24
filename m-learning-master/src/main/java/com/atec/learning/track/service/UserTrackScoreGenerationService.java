package com.atec.learning.track.service;

import com.atec.learning.track.domain.UserNote;
import com.atec.learning.track.domain.type.RdrItemType;
/**
 * responsible for calculating a score for each update of a track.
 * @author Hafedh
 *
 */
public interface UserTrackScoreGenerationService {
	
	public double generateUserTrackScore(
			Long viewNumer,
			Long likeNumber,
			Long rateNumber,
			Long achatNumber,
			Long itemValueId,
			RdrItemType itemType,
			UserNote action);
}
