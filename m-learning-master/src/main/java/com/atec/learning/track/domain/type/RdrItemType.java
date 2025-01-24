package com.atec.learning.track.domain.type;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RdrItemType implements Serializable{
	private static final long serialVersionUID = 1L;

    private static final Map<String, RdrItemType> TYPES = new HashMap<String, RdrItemType>();

    public static final RdrItemType PRODUCT = new RdrItemType("PRODUCT");

    public static RdrItemType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;

    public RdrItemType() {
    }

    public RdrItemType(final String type) {
        setType(type);
    }

    public String getType() {
        return type;
    }

    private void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
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
        if (!getClass().isAssignableFrom(obj.getClass()))
            return false;
        RdrItemType other = (RdrItemType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
