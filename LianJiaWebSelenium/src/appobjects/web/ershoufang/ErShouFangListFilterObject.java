package appobjects.web.ershoufang;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import appobjects.web.widgets.WebCheckBox;
import appobjects.web.widgets.WebLink;
import appobjects.web.widgets.WebPanel;

import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public class ErShouFangListFilterObject extends SeleniumTestObject {	
	public ErShouFangListFilterObject(String sLocator){
		super(sLocator);
	}	
	
	//区域
	public WebPanel getQuYuPanel(){
		return new WebPanel("css=dl[mod-id='filter-box-d']");
	}
	
	//所有区域
	public List<WebElement> getAllQuYu(){
		return getQuYuPanel().getWebElement().findElement(By.className("option-list")).findElements(By.tagName("a"));
	}	
	
	//某一区域下所有商圈
	public List<WebElement> getAllShangQuan(){
		return getQuYuPanel().getWebElement().findElement(By.className("sub-option-list")).findElements(By.tagName("a"));
	}		
	
	//售价
	public WebPanel getShouJiaPanel(){
		return new WebPanel("css=dl[mod-id='filter-box-p']");
	}
	
	//所有售价
	public List<WebElement> getAllShouJia(){
		return getShouJiaPanel().getWebElement().findElement(By.className("option-list")).findElements(By.tagName("a"));
	}	
	
	//面积
	public WebPanel getMianJiPanel(){
		return new WebPanel("css=dl[mod-id='filter-box-a']");
	}
	
	//所有面积
	public List<WebElement> getAllMianJi(){
		return getMianJiPanel().getWebElement().findElement(By.className("option-list")).findElements(By.tagName("a"));
	}	
	
	//房型
	public WebPanel getFangXingPanel(){
		return new WebPanel("css=dl[mod-id='filter-box-l']");
	}
	
	//所有房型
	public List<WebElement> getAllFangXing(){
		return getFangXingPanel().getWebElement().findElement(By.className("option-list")).findElements(By.tagName("a"));
	}
	
	//筛选
	public WebPanel getSaiXuanPanel(){
		return new WebPanel("css=div[mod-id='filter-box-f']");
	}
		
	//朝向
	public WebElement getChaoXiang(){
		return getSaiXuanPanel().getWebElement().findElement(By.className("d-1"));
	}
	
	//所有朝向
	public List<WebElement> getAllChaoXiang(){
		return getChaoXiang().findElement(By.className("fil-item")).findElements(By.tagName("a"));
	}	
	
	//楼龄
	public WebElement getLouLing(){
		return getSaiXuanPanel().getWebElement().findElement(By.className("d-2"));
	}
	
	//所有楼龄
	public List<WebElement> getAllLouLing(){
		return getLouLing().findElement(By.className("fil-item")).findElements(By.tagName("a"));
	}
	
	//楼层
	public WebElement getLouCeng(){
		return getSaiXuanPanel().getWebElement().findElement(By.className("d-3"));
	}
	
	//所有楼层
	public List<WebElement> getAllLouCeng(){
		return getLouCeng().findElement(By.className("fil-item")).findElements(By.tagName("a"));
	}
	
	//标签
	public WebElement getBiaoQian(){
		return getSaiXuanPanel().getWebElement().findElement(By.className("d-4"));
	}
	
	//所有标签
	public List<WebElement> getAllBiaoQian(){
		return getBiaoQian().findElement(By.className("fil-item")).findElements(By.tagName("a"));
	}
	
	//满五年唯一/满两年
	public WebCheckBox getManWuWeiYi(){
		return new WebCheckBox("linktext=满五年唯一/满两年");
	}
	
	//近地铁
	public WebCheckBox getJinDiTie(){
		return new WebCheckBox("linktext=近地铁");
	}
	
	//学区房
	public WebCheckBox getXueQuFang(){
		return new WebCheckBox("linktext=学区房");
	}
	
	//不限购
	public WebCheckBox getBuXianGou(){
		return new WebCheckBox("linktext=不限购");
	}
	
	//条件
	public WebPanel getTiaoJianPanel(){
		return new WebPanel("id=filter-bar");
	}	
	
	//筛选条件
	public List<WebElement> getAllFilterOptions(){
		return getTiaoJianPanel().getWebElement().findElement(By.id("filter-display-bar")).findElements(By.tagName("a"));
	}	
	
	//清空筛选选项
	public WebLink getFilterEmpty(){
		return new WebLink("id=filter-empty");
	}
	
	//排序
	public WebPanel getPaiXuPanel(){
		return new WebPanel("id=sort-bar");
	}
	
	//默认
	public WebElement getDefault(){
		return getPaiXuPanel().getWebElement().findElement(By.linkText("默认"));
	}
	
	//最新
	public WebElement getLatest(){
		return getPaiXuPanel().getWebElement().findElement(By.linkText("最新"));
	}
	
	//总价
	public WebElement getTotalPrice(){
		return getPaiXuPanel().getWebElement().findElement(By.xpath(".//span[text()='总价']"));
	}
	
	//总价从低到高
	public WebElement getTotalPriceAsc(){
		return getPaiXuPanel().getWebElement().findElement(By.xpath(".//span[text()='总价从低到高']"));
	}
	
	//总价从低到高
	public WebElement getTotalPriceItemAsc(){
		return getPaiXuPanel().getWebElement().findElement(By.linkText("总价从低到高"));
	}
	
	//总价从高到低
	public WebElement getTotalPriceDesc(){
		return getPaiXuPanel().getWebElement().findElement(By.xpath(".//span[text()='总价从高到低']"));
	}
	
	//总价从高到低
	public WebElement getTotalPriceItemDesc(){
		return getPaiXuPanel().getWebElement().findElement(By.linkText("总价从高到低"));
	}
	
	//单价
	public WebElement getUnitPrice(){
		return getPaiXuPanel().getWebElement().findElement(By.xpath(".//span[text()='单价']"));
	}
	
	//总价从低到高
	public WebElement getUnitPriceAsc(){
		return getPaiXuPanel().getWebElement().findElement(By.xpath(".//span[text()='单价从低到高']"));
	}
	
	//总价从低到高
	public WebElement getUnitPriceItemAsc(){
		return getPaiXuPanel().getWebElement().findElement(By.linkText("单价从低到高"));
	}
	
	//总价从高到低
	public WebElement getUnitPriceDesc(){
		return getPaiXuPanel().getWebElement().findElement(By.xpath(".//span[text()='单价从高到低']"));
	}
	
	//总价从高到低
	public WebElement getUnitPriceItemDesc(){
		return getPaiXuPanel().getWebElement().findElement(By.linkText("单价从高到低"));
	}
	
	//面积
	public WebElement getSquare(){
		return getPaiXuPanel().getWebElement().findElement(By.xpath(".//span[text()='面积']"));
	}
	
	//面积从低到高
	public WebElement getSquareAsc(){
		return getPaiXuPanel().getWebElement().findElement(By.xpath(".//span[text()='面积从低到高']"));
	}
	
	//面积从低到高
	public WebElement getSquareItemAsc(){
		return getPaiXuPanel().getWebElement().findElement(By.linkText("面积从低到高"));
	}
	
	//面积从高到低
	public WebElement getSquareDesc(){
		return getPaiXuPanel().getWebElement().findElement(By.xpath(".//span[text()='面积从高到低']"));
	}
	
	//面积从高到低
	public WebElement getSquareItemDesc(){
		return getPaiXuPanel().getWebElement().findElement(By.linkText("面积从高到低"));
	}
	
	
}
