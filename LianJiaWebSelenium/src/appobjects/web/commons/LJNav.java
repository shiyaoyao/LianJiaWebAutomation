package appobjects.web.commons;

import org.apache.log4j.Logger;

import appobjects.web.bases.LJLocators;
import appobjects.web.commons.LJContainer;
import appobjects.web.widgets.WebLink;

public class LJNav extends LJContainer{
	protected static final Logger log = Logger.getLogger(LJNav.class);
	
	public LJNav(String sLocator) {
		super(sLocator);
	}
	
	public WebLink getLJLogo(){
		return new WebLink(LJLocators.LJ_LOGO);
	}
	
	public WebLink getLJOldLogo(){
    	return new WebLink(LJLocators.LJ_OLD_LOGO);
	}
	
	public WebLink getNavErShouFang(){
		return new WebLink(LJLocators.NAV_ERSHOUFANG);
	}
	
	public WebLink getNavAll(){
		getNavErShouFang().hover(2);
		return new WebLink(LJLocators.NAV_ALL);
	}
	
	public WebLink getNavDiTieFang(){
		getNavErShouFang().hover(2);
		return new WebLink(LJLocators.NAV_DITIEFANG);
	}
	
	public WebLink getNavYouZhiXueQu(){
		getNavErShouFang().hover(2);
		return new WebLink(LJLocators.NAV_YOUZHIXUEQU);
	}
	
	public WebLink getNavChengJiao(){
		getNavErShouFang().hover(2);
		return new WebLink(LJLocators.NAV_CHENGJIAOFANGYUAN);
	}
	
	public WebLink getNavXinFang(){
		return new WebLink(LJLocators.NAV_XINFANG);
	}
	
	public WebLink getNavZuFang(){
		return new WebLink(LJLocators.NAV_ZUFANG);
	}
	
	public WebLink getNavShangPu(){
		return new WebLink(LJLocators.NAV_SHANGPU);
	}
	
	public WebLink getNavOversea(){		
		return new WebLink(LJLocators.NAV_OVERSEA);
	}
	
	public WebLink getNavXiaoQu(){
		return new WebLink(LJLocators.NAV_XIAOQU);
	}
	
	public WebLink getNavAgent(){
		return new WebLink(LJLocators.NAV_AGENT);
	}
	
	public WebLink getNavJinNang(){
		return new WebLink(LJLocators.NAV_JINNANG);
	}
	
	public WebLink getNavFangJia(){
		return new WebLink(LJLocators.NAV_FANGJIA);
	}
	
	public WebLink getNavAsk(){
		getNavJinNang().hover(2);
		return new WebLink(LJLocators.NAV_ASK);
	}
	
	public WebLink getNavBaiKe(){
		getNavJinNang().hover(2);
		return new WebLink(LJLocators.NAV_BAIKE);
	}
	
	public WebLink getNavLiCai(){
		return new WebLink(LJLocators.NAV_LICAI);
	}
	
	public WebLink getNavOnHand(){
		return new WebLink(LJLocators.NAV_ONHAND);
	}
	
	public WebLink getNavOnHandIMG(){
		getNavOnHand();
		return new WebLink(null);
	}
	
	public WebLink getNavMaiFang(){
		return new WebLink(LJLocators.NAV_MAIFANG);
	}
	
	public WebLink getRegister(){
		return new WebLink(LJLocators.NAV_REGISTER);
	}
	
	public WebLink getLogIn(){
		return new WebLink(LJLocators.NAV_LOGIN);
	}
	
	public WebLink getLogOut(){
		return new WebLink(LJLocators.NAV_LOGOUT);
	}
}
