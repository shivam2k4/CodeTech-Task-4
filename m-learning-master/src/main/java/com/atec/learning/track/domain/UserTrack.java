package com.atec.learning.track.domain;

import java.io.Serializable;

import org.broadleafcommerce.profile.core.domain.Customer;

import com.atec.learning.track.domain.type.RdrItemType;

public interface UserTrack extends Serializable{
	
	/**
	 * get the id
	 * @return
	 */
	public Long getId();
	
	/**
	 * set the id
	 * @param id
	 */
	public void setId(Long id);
	
	/**
	 * get {@link Customer}.
	 * @return
	 */
	public Customer getCustomer();
	
	/**
	 * set the customer{@link Customer}.
	 * @param customer
	 */
	public void setCustomer(Customer customer);
	
	/**
	 * get the item type, PRODUCT ...
	 * @return
	 */
	public RdrItemType getItemType();
	
	/**
	 * set the Item Type.
	 * @param itemType
	 */
	public void setItemType(RdrItemType itemType);
	
	/**
	 * get the item Value --> the Id of the Item type.
	 * @return
	 */
	public Long getItemValue();
	
	/**
	 * set the item Value
	 * @param ratingValue
	 */
	public void setItemValue(Long itemValue);
	
	/**
	 * get a boolean result if the Item is Viewed or not.
	 * @return
	 */
	public boolean getTrackView();
	
	/**
	 * set the value of trackView.
	 * @param string
	 */
	public void setTrackView(Boolean string);
	
	/**
	 * get a boolean result if the Item is buyed or not.
	 * @return
	 */
	public boolean getTrackAchat();
	
	/**
	 * set the value of trackAchat.
	 * @param trackAchat
	 */
	public void setTrackAchat(boolean trackAchat);
	
	/**
	 * get a boolean result if the Item is Like or not (added to whishlist).
	 * @return
	 */
	public boolean getTrackLike();
	
	/**
	 * set the value of trackLike.
	 * @param string
	 */
	public void setTrackLike(Boolean string);
	
	/**
	 * get a boolean result if the Item is Rated or not.
	 * @return
	 */
	public boolean getTrackRated();
	
	/**
	 * set the value of trackRated.
	 * @param trackRated
	 */
	public void setTrackRated(Boolean trackRated);
	
	/**
	 * get the score of User-Item.
	 * @return
	 */
	public double getUserItemScore();
	
	/**
	 * get the score of User-Item.
	 * @param userItemScore
	 */
	public void setUserItemScore(double userItemScore);
}
