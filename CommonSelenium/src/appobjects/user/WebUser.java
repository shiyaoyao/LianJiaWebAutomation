package appobjects.user;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

public class WebUser {
	private String username;
	private String password;	
	private String phone;	
	
	public String getUsername() {
		return getUserProp(this.username);
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return getUserProp(this.password);
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPhone() {
		return getUserProp(this.phone);
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}	
	   /**
     * Parses the contents of the string into WebUser objects
     * @param contents
     */
    public static WebUser getUserFromProperties(Properties props){
                
        WebUser user = new WebUser();
        
        user.setUsername(props.getProperty("username",""));
        user.setPassword(props.getProperty("password",""));        
        user.setPhone(props.getProperty("phone",""));
        
        return user;
    }
    
    public String getUserProp(String prop) {
       return prop == null ? "" : prop;
    }
    
	public ArrayList<String> getUserPropertiesList() {
		ArrayList<String> userFields = new ArrayList<String>();
		ArrayList<String> out = new ArrayList<String>();
		
		userFields.add("Password:" + getPassword());
		userFields.add("Phone:" + getPhone());
		userFields.add("Username:" + getUsername());

		for(String uf : userFields) {
			out.add(uf);
		}
		return out;
	}
		
	public Properties getUserProperties() {
		Properties props = new Properties();
		int index;
		for(String prop : getUserPropertiesList()) {
			index = prop.indexOf(':');
			props.put(prop.substring(0, index).toLowerCase(), prop.substring(index + 1));
		}
		return props;
	}

	public String getKey(Properties oProps, String sValue) {
		String foundKey = "";
		Set<Object> keys = oProps.keySet();
		if(oProps.containsValue(sValue)) {
			for(Object k : keys) {
				if(oProps.get(k).equals(sValue)) {
					foundKey = k.toString();
					break;
				}
			}
		}
		return foundKey;
	}

	public String getProperty(String sProp) {
		Object oVal = null;
		try {
			oVal = getUserProperties().get(sProp.toLowerCase());
		} catch (Exception e) {
		}
		return oVal == null ? "" : oVal.toString();
	}
}
