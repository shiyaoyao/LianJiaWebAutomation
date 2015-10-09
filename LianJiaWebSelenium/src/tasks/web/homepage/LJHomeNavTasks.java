package tasks.web.homepage;

import tasks.base.BaseTask;
import tasks.web.util.WindowManagement;
import appobjects.web.Browser;
import appobjects.web.homepage.LJHomeNav;
import appobjects.web.widgets.WebLink;

import com.lianjia.StringFetch;

public class LJHomeNavTasks extends BaseTask {	 
	
	public static boolean isChaChengJiaoJiaExist(){
		return LJHomeNav.getChaChengJiaoJia().exists();
	}
	
	public static boolean gotoFangJiaByImg(){
		WebLink img = LJHomeNav.getChaChengJiaoJiaImg();
		img.click();
		
		WindowManagement.switchToNewWindow();
		
		String sTitle = Browser.getTitle();
		
		if(sTitle.contains(StringFetch.getString("TEXT_FANGJIA")))
			return true;
		else 
			return false;
	}
	
	public static boolean gotoFangJiaByText(){
		WebLink text = LJHomeNav.getChaChengJiaoJiaText();
		text.click();
		
		WindowManagement.switchToNewWindow();
		
		String sTitle = Browser.getTitle();
		
		if(sTitle.contains(StringFetch.getString("TEXT_FANGJIA")))
			return true;
		else 
			return false;
	}
	
	public static boolean isSearchXueQuFangExist(){
		return LJHomeNav.getSearchXueQuFang().exists();
	}
	
	public static boolean gotoXueQuFangByImg(){
		WebLink img = LJHomeNav.getXueQuFangImg();
		img.click();
		
		WindowManagement.switchToNewWindow();
		
		String sTitle = Browser.getTitle();
		
		if(sTitle.contains(StringFetch.getString("TEXT_XUEQUFANG")))
			return true;
		else 
			return false;
	}
	
	public static boolean gotoXueQuFangByText(){
		WebLink text = LJHomeNav.getXueQuFangText();
		text.click();
		
		WindowManagement.switchToNewWindow();
		
		String sTitle = Browser.getTitle();
		
		if(sTitle.contains(StringFetch.getString("TEXT_XUEQUFANG")))
			return true;
		else 
			return false;
	}
	
	public static boolean isLianJiaLiCaiExist(){
		return LJHomeNav.getLianJiaLiCai().exists();
	}
	
	public static boolean gotoLiCaiByImg(){
		WebLink img = LJHomeNav.getLianJiaLiCaiImg();
		img.click();
		
		WindowManagement.switchToNewWindow();
		
		String sTitle = Browser.getTitle();
		
		if(sTitle.contains(StringFetch.getString("TEXT_LIANJIALICAI")))
			return true;
		else 
			return false;
	}
	
	public static boolean gotoLiCaiByText(){
		WebLink text = LJHomeNav.getLianJiaLiCaiText();
		text.click();
		
		WindowManagement.switchToNewWindow();
		
		String sTitle = Browser.getTitle();
		
		if(sTitle.contains(StringFetch.getString("TEXT_LIANJIALICAI")))
			return true;
		else 
			return false;
	}
	
	public static boolean isChuZuFangExist(){
		return LJHomeNav.getChuZuFang().exists();
	}
	
	public static boolean gotoChuZuFangByImg(){
		WebLink img = LJHomeNav.getChuZuFangImg();
		img.click();
		
		WindowManagement.switchToNewWindow();
		
		String sTitle = Browser.getTitle();
		
		if(sTitle.contains(StringFetch.getString("TEXT_ZUFANG")))
			return true;
		else 
			return false;
	}
	
	public static boolean isSearchDiTieFangExist(){
		return LJHomeNav.getSearchDiTieFang().exists();
	}
	
	public static boolean gotoDiTieFangByImg(){
		WebLink img = LJHomeNav.getSearchErShouFangImg();
		img.click();
		
		WindowManagement.switchToNewWindow();
		
		String sTitle = Browser.getTitle();
		
		if(sTitle.contains(StringFetch.getString("TEXT_DITIEERSHOUFANG")))
			return true;
		else 
			return false;
	}
	
	public static boolean isSearchErShouFangExist(){
		return LJHomeNav.getSearchErShouFang().exists();
	}

	public static boolean gotoErShouFangByImg(){
		WebLink img = LJHomeNav.getSearchErShouFangImg();
		img.click();
		
		WindowManagement.switchToNewWindow();
		
		String sTitle = Browser.getTitle();
		
		if(sTitle.contains(StringFetch.getString("TEXT_ERSHOUFANG")))
			return true;
		else 
			return false;
	}
}
