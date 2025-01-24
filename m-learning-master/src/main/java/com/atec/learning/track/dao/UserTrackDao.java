package com.atec.learning.track.dao;

import java.util.List;

import com.atec.learning.track.domain.UserTrack;

public interface UserTrackDao {
	
	public UserTrack readTrackById(Long userTrackId);
	
	public UserTrack create();
	
	public UserTrack add(UserTrack userTrack);
	
	public void remove(UserTrack userTrack);
	
	public UserTrack readTrackByCustomerAndItem(Long customerId, String itemType, Long itemValue);
	
	public List<UserTrack> readUserTracksByCustomerId(Long customerId);
}
