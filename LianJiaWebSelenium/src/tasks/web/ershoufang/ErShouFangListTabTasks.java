package tasks.web.ershoufang;

import tasks.base.BaseTask;
import appobjects.web.ershoufang.ErShouFangListTabObject;
import appobjects.web.widgets.WebLink;

import com.lianjia.StringFetch;
import com.lianjia.automation.core.Platform;

public class ErShouFangListTabTasks extends BaseTask {
	private static ErShouFangListTabObject erShouFangListTabObject = new ErShouFangListTabObject("className=tab-lst");
	
	 //切换到全部二手房
	 public static boolean gotoAllErShouFang() {
		WebLink allErShouFangTab = erShouFangListTabObject.getTab_AllErShouFang();			
		allErShouFangTab.click();
			
		Platform.sleep(3);
		
		String sTitle = erShouFangListTabObject.getTabTitle();
		assertTrue("切换到全部二手房", sTitle.equals(StringFetch.getString("ALL_ERSHOUFANG")));
		return true;
	 }
	 
	 //切换到地铁房
	 public static boolean gotoDiTieFang() {
		WebLink diTieFangTab = erShouFangListTabObject.getTab_DiTieFang();			
		diTieFangTab.click();
		
		Platform.sleep(3);
		
		String sTitle = erShouFangListTabObject.getTabTitle();
		assertTrue("切换到地铁房", sTitle.equals(StringFetch.getString("DITIEFANG")));
		return true;
	 }
	 	
	 //切换到优质学区
	 public static boolean gotoYouZhiXueQu() {
		WebLink youZhiXueQuTab = erShouFangListTabObject.getTab_YouZhiXueQu();			
		youZhiXueQuTab.click();
			
		Platform.sleep(3);
		
		String sTitle = erShouFangListTabObject.getTabTitle();
		assertTrue("切换到优质学区", sTitle.equals(StringFetch.getString("YouZhiXueQu")));
		return true;
 	}
	 
 	//切换到成交房源
 	public static boolean gotoChengJiaoFangYuan() {
 		WebLink chengJiaoTab = erShouFangListTabObject.getTab_ChengJiaoFangYuan();			
 		chengJiaoTab.click();
			
		Platform.sleep(3);
		
		String sTitle = erShouFangListTabObject.getTabTitle();
		assertTrue("切换到成交房源", sTitle.equals(StringFetch.getString("CHENGJIAOFANGYUAN")));
		return true;
	 }
}
