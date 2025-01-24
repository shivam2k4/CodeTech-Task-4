package com.atec.learning.track.domain.type;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RdrUserAction implements Serializable{
private static final long serialVersionUID = 1L;
	
	private static final Map<String, RdrUserAction> TYPES = new HashMap<String, RdrUserAction>();
	
	public static final RdrUserAction VIEW = new RdrUserAction("VIEW", "view");
	public static final RdrUserAction LIKE = new RdrUserAction("LIKE", "like");
	public static final RdrUserAction RATE = new RdrUserAction("RATE", "rate");
	public static final RdrUserAction ACHAT = new RdrUserAction("ACHAT", "achat");
	
	
	public static RdrUserAction getInstance(final String type){
		return TYPES.get(type);
	}
	
	private String type;
    private String friendlyType;
    
    public RdrUserAction(){
    	
    }
    
    public RdrUserAction(final String type, final String friendlyType) {
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
        RdrUserAction other = (RdrUserAction) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
