package com.atec.learning.track.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author mahbouba
 *
 */
public class UserNote implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static final Map<String, UserNote> TYPES = new HashMap<String, UserNote>();
	
	public static final UserNote VIEW = new UserNote("1", "view product");
	public static final UserNote LIKE = new UserNote("2", "like product");
	public static final UserNote RATE = new UserNote("3", "rate product");
	public static final UserNote ACHAT = new UserNote("4", "achat product");
	
	
	public static UserNote getInstance(final String type){
		return TYPES.get(type);
	}
	
	private String type;
    private String friendlyType;
    
    public UserNote(){
    	
    }
    
    public UserNote(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

	private void setType(String type) {
		this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
	}

	public String getFriendlyType() {
		return friendlyType;
	}

	public void setFriendlyType(String friendlyType) {
		this.friendlyType = friendlyType;
	}

	public String getType() {
		return type;
	}
	
	
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserNote other = (UserNote) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
