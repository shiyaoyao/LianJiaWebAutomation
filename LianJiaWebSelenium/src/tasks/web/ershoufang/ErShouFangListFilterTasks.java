package tasks.web.ershoufang;

import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import tasks.base.BaseTask;
import appobjects.web.ershoufang.ErShouFangListFilterObject;
import appobjects.web.widgets.WebLink;

import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.web.SeleniumCore;

public class ErShouFangListFilterTasks extends BaseTask {
	private static ErShouFangListFilterObject erShouFangListFilterObject = new ErShouFangListFilterObject("className=filter-box");
	
	//区域总数量
	public static int getAllQuYuNum(){
		return erShouFangListFilterObject.getAllQuYu().size();
	}
	
	//选取某一区域
	public static void clickQuYuByIndex(int index){
		List<WebElement> allQuYu = erShouFangListFilterObject.getAllQuYu();
		WebElement quYu = allQuYu.get(index);
		
		quYu.click();		
		Platform.sleep(3);
	}
	
	//获取某一区域名称
	public static String getQuYuNameByIndex(int index){
		List<WebElement> allQuYu = erShouFangListFilterObject.getAllQuYu();
		WebElement quYu = allQuYu.get(index);
		
		return quYu.getText();
	} 
	
	//某区域下商圈总数
	public static int getAllShangQuanNum(){
		return erShouFangListFilterObject.getAllShangQuan().size();
	}
	
	//选取某一商圈
	public static void clickShangQuanByIndex(int index){
		List<WebElement> allShangQuan = erShouFangListFilterObject.getAllShangQuan();
		WebElement shangQuan = allShangQuan.get(index);
		
		shangQuan.click();
		Platform.sleep(3);
	}
	
	//获取某一商圈名称
	public static String getShangQuanByIndex(int index){
		List<WebElement> allShangQuan = erShouFangListFilterObject.getAllShangQuan();
		WebElement shangQuan = allShangQuan.get(index);  
		
		return shangQuan.getText();
	}
	
	//售价段位总数量
	public static int getAllShouJiaNum(){
		return erShouFangListFilterObject.getAllShouJia().size();
	}
	
	//选取某一售价
	public static void clickShouJiaByIndex(int index){
		List<WebElement> allShouJia = erShouFangListFilterObject.getAllShouJia();
		WebElement shoujia = allShouJia.get(index);
		
		shoujia.click();
		Platform.sleep(3);
	}
	
	//获取某一售价
	public static String getShouJiaByIndex(int index){
		List<WebElement> allShouJia = erShouFangListFilterObject.getAllShouJia();
		WebElement shoujia = allShouJia.get(index);  
		
		return shoujia.getText();
	}	
	
	//面积段位总数量
	public static int getAllMianJiNum(){
		return erShouFangListFilterObject.getAllMianJi().size();
	}
	
	//选取某一面积
	public static void clickMianJiByIndex(int index){
		List<WebElement> allMianJi = erShouFangListFilterObject.getAllMianJi();
		WebElement mianji = allMianJi.get(index);
		
		mianji.click();
		Platform.sleep(3);
	}
	
	//获取某一面积
	public static String getMianJiByIndex(int index){
		List<WebElement> allMianJi = erShouFangListFilterObject.getAllMianJi();
		WebElement mianji = allMianJi.get(index);  
		
		return mianji.getText();
	} 
	
	//房型分类总数
	public static int getAllFangXingNum(){
		return erShouFangListFilterObject.getAllFangXing().size();
	}	
	
	//选取某一房型
	public static void clickFangXingByIndex(int index){
		List<WebElement> allFangXing = erShouFangListFilterObject.getAllFangXing();
		WebElement fangxing = allFangXing.get(index);
		
		fangxing.click();
		Platform.sleep(3);
	}
	
	//获取某一房型
	public static String getFangXingByIndex(int index){
		List<WebElement> allFangXing = erShouFangListFilterObject.getAllFangXing();
		WebElement fangxing = allFangXing.get(index);
		
		return fangxing.getText();
	}
	
	//朝向分类总数
	public static int getAllChaoXiangNum(){
		return erShouFangListFilterObject.getAllChaoXiang().size();
	}	
	
	//选取某一朝向
	public static void clickChaoXiangByIndex(int index){
		List<WebElement> allChaoXiang = erShouFangListFilterObject.getAllChaoXiang();
		WebElement chaoxiang = allChaoXiang.get(index);
		
		WebElement chaoxiangListBox = erShouFangListFilterObject.getChaoXiang();		
		hover(chaoxiangListBox);
		
		chaoxiang.click();
		Platform.sleep(3);
	}
	
	//获取某一朝向
	public static String getChaoXiangByIndex(int index){
		List<WebElement> allChaoXiang = erShouFangListFilterObject.getAllChaoXiang();
		
		WebElement chaoXiangListBox = erShouFangListFilterObject.getChaoXiang();
		hover(chaoXiangListBox);
		
		WebElement chaoxiang = allChaoXiang.get(index);
		return chaoxiang.getText();
	}
	
	//楼龄分类总数
	public static int getAllLouLingNum(){
		return erShouFangListFilterObject.getAllLouLing().size();
	}
	
	//选取某一楼龄
	public static void clickLouLingByIndex(int index){
		List<WebElement> allLouLing = erShouFangListFilterObject.getAllLouLing();
		WebElement louling = allLouLing.get(index);
		
		WebElement louLingListBox = erShouFangListFilterObject.getLouLing();		
		hover(louLingListBox);
		
		louling.click();
		Platform.sleep(3);
	}
	
	//获取某一楼龄
	public static String getLouLingByIndex(int index){
		List<WebElement> allLouLing = erShouFangListFilterObject.getAllLouLing();
		WebElement louling = allLouLing.get(index);
		
		WebElement louLingListBox = erShouFangListFilterObject.getLouLing();		
		hover(louLingListBox);
		
		return louling.getText();
	}
	
	//楼层分类总数
	public static int getAllLouCengNum(){
		return erShouFangListFilterObject.getAllLouCeng().size();
	}
	
	//选取某一楼层
	public static void clickLouCengByIndex(int index){
		List<WebElement> allLouCeng = erShouFangListFilterObject.getAllLouCeng();
		WebElement louceng = allLouCeng.get(index);
		
		WebElement louCengListBox = erShouFangListFilterObject.getLouCeng();		
		hover(louCengListBox);
		
		louceng.click();
		Platform.sleep(3);
	}
	
	//获取某一楼层
	public static String getLouCengByIndex(int index){
		List<WebElement> allLouCeng = erShouFangListFilterObject.getAllLouCeng();
		WebElement louceng = allLouCeng.get(index);
		
		WebElement louCengListBox = erShouFangListFilterObject.getLouCeng();		
		hover(louCengListBox);
		
		return louceng.getText();
	}
	
	//标签分类总数
	public static int getAllBiaoQianNum(){
		return erShouFangListFilterObject.getAllBiaoQian().size();
	}
	
	//选取某一标签
	public static void clickBiaoQianByIndex(int index){
		List<WebElement> allBiaoQian = erShouFangListFilterObject.getAllBiaoQian();
		WebElement biaoqian = allBiaoQian.get(index);
		
		WebElement biaoQianListBox = erShouFangListFilterObject.getBiaoQian();		
		hover(biaoQianListBox);
		
		biaoqian.click();
		Platform.sleep(3);
	}
	
	//获取某一标签
	public static String getBiaoQianByIndex(int index){
		List<WebElement> allBiaoQian = erShouFangListFilterObject.getAllBiaoQian();
		WebElement biaoqian = allBiaoQian.get(index);
		
		WebElement biaoQianListBox = erShouFangListFilterObject.getBiaoQian();		
		hover(biaoQianListBox);
		
		return biaoqian.getText();
	}
	
	//勾选　满五年唯一/满两年
	public static boolean checkManWuWeiYi(){
		return erShouFangListFilterObject.getManWuWeiYi().check();
	}
	
	//勾选　近地铁
	public static boolean checkJinDiTie(){
		return erShouFangListFilterObject.getJinDiTie().check();
	}
	
	//勾选　学区房
	public static boolean checkXueQuFang(){
		return erShouFangListFilterObject.getXueQuFang().check();
	}
	
	//勾选　不限购
	public static boolean checkBuXianGou(){
		return erShouFangListFilterObject.getBuXianGou().check();
	} 
	
	//总价从低到高
	public static boolean chooseZongJiaAsc(){
		boolean total = true;
		try {
			total = erShouFangListFilterObject.getTotalPrice().isDisplayed();
		} catch (NoSuchElementException e){
			total = false;
		}
		
		if(total){
			hover(erShouFangListFilterObject.getTotalPrice());
			Platform.sleep(3);
			
			erShouFangListFilterObject.getTotalPriceItemAsc().click();	
			Platform.sleep(3);
			
			return true;
		} else {
			boolean desc = true;
			try {
				desc = erShouFangListFilterObject.getTotalPriceDesc().isDisplayed();
			} catch (NoSuchElementException e){
				desc = false;
			}
			
			if(desc){
				hover(erShouFangListFilterObject.getTotalPriceDesc());
				Platform.sleep(3);
				
				erShouFangListFilterObject.getTotalPriceItemAsc().click();		
				Platform.sleep(3);
				
				return true;
			} else {
				boolean asc = true;
				try {
					asc = erShouFangListFilterObject.getTotalPriceAsc().isDisplayed();
				} catch (NoSuchElementException e){
					asc = false;
				}
				
				return asc;
			}
		}						
	}
	
	//总价从高到低
	public static void chooseZongJiaDesc(){
		chooseZongJiaAsc();
	}	
	
	public static boolean isOptionSelected(String option){
		List<WebElement> allOptions = erShouFangListFilterObject.getAllFilterOptions();
		
		int i = 0;
		
		while(i < allOptions.size()){
			WebElement optionItem = allOptions.get(i);
			
			if((optionItem.getText()).equals(option))
				return true;
			
			i++;
		}
		
		return false;
	}
	
	public static void hover(WebElement element) {		
		Actions action = new Actions(SeleniumCore.getWebDriverBrowser().getWebDriverAPI());
		action.moveToElement(element).perform();
		Platform.sleep(3);
	}
	
	//清空筛选选项
	public static boolean clickFilterEmpty(){
		WebLink filterEmpty = erShouFangListFilterObject.getFilterEmpty();
		filterEmpty.click();
		
		Platform.sleep(3);
		
		if(erShouFangListFilterObject.getTiaoJianPanel().exists())
			return false;
		else
			return true; 		
	}
}
