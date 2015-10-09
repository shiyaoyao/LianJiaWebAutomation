package appobjects.cms.firstpagecontent;

import appobjects.web.commons.LJContainer;
import appobjects.web.widgets.WebComboBox;

public class QueryManageObject extends LJContainer{
	public QueryManageObject(String sLocator){
		super(sLocator);
	}
	
	public WebComboBox getCitySwitchComboBox(){
		return new WebComboBox("id=cityname");
	}
	
	public WebComboBox getChannelSwitchComboBox(){
		return new WebComboBox("id=channel");
	}
	
	public void switchToCity(String cityName){
		new WebComboBox("id=cityname").select(cityName);
		
		
	}
}
