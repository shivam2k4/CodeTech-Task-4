package com.atec.learning.track.exceptions;


/**
 * 
 * @author mahbouba
 *
 */
public class UserTrackExceptions extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    protected UserTrackExceptions() {
        super();
    }
    
    public UserTrackExceptions(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UserTrackExceptions(String message) {
        super(message);
    }
    
    public UserTrackExceptions(Throwable cause) {
        super(cause);
    }
	
	
}
