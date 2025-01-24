package com.atec.learning.track.service;


/**
 * 
 * @author takwa
 *
 */
public class TrackUtils {
	
	/**
	 * Méthode basic de calcul du Score
	 * @param totalScore
	 * @param action
	 * @author takwa
	 * @return
	 */
		
    
    
	public static double generateNewScore(double totalScore, double action){
		if(totalScore != 0 && action != 0){
			return action / totalScore; 
		}
		return 0;
	}
	


}
