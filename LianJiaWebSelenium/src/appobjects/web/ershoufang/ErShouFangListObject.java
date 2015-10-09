package appobjects.web.ershoufang;

import org.apache.log4j.Logger;

import appobjects.web.Browser;
import appobjects.web.bases.LJLocators;
import appobjects.web.commons.LJListView;
import appobjects.web.widgets.WebButton;

public class ErShouFangListObject extends LJListView {
	protected static final Logger log = Logger.getLogger(ErShouFangListObject.class);
	
	public ErShouFangListObject(String listId) {
		super(listId);
	}	
	
	public WebButton getButton_ShiJingTuMode(){
		return new WebButton(LJLocators.LISTMODE_ID_ERSHOUFANG_SHIJINGTU);
	}
	
	public WebButton getButton_HuXingTuMode(){
		return new WebButton(LJLocators.LISTMODE_ID_ERSHOUFANG_HUXINGTU);
	}	
	
	public static String getTDK(){
		String pageTitle = Browser.getTitle();
		return pageTitle;
	}
}
