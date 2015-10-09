package appobjects.web.homepage;

import appobjects.web.bases.LJLocators;
import appobjects.web.widgets.WebLink;
import appobjects.web.widgets.WebStaticText;

public class LJHomeNav {
	public static WebStaticText getChaChengJiaoJia(){
		return new WebStaticText(LJLocators.HOME_NAV_CHACHENGJIAOJIA);
	}
	
	public static WebLink getChaChengJiaoJiaImg(){
		return new WebLink(LJLocators.HOME_NAV_CHACHENGJIAOJIA_IMG);
	}
	
	public static WebLink getChaChengJiaoJiaText(){
		return new WebLink(LJLocators.HOME_NAV_CHACHENGJIAOJIA_TEXT);
	}	
	
	public static WebStaticText getSearchXueQuFang(){
		return new WebStaticText(LJLocators.HOME_NAV_SEARCHXUEQUFANG);
	}
	
	public static WebLink getXueQuFangImg(){
		return new WebLink(LJLocators.HOME_NAV_SEARCHXUEQUFANG_IMG);
	}
	
	public static WebLink getXueQuFangText(){
		return new WebLink(LJLocators.HOME_NAV_SEARCHXUEQUFANG_TEXT);
	}
	
	public static WebStaticText getLianJiaLiCai(){
		return new WebStaticText(LJLocators.HOME_NAV_LIANJIALICAI);
	}
	
	public static WebLink getLianJiaLiCaiImg(){
		return new WebLink(LJLocators.HOME_NAV_LIANJIALICAI_IMG);
	}
	
	public static WebLink getLianJiaLiCaiText(){
		return new WebLink(LJLocators.HOME_NAV_LIANJIALICAI_TEXT);
	}
	
	public static WebStaticText getChuZuFang(){
		return new WebStaticText(LJLocators.HOME_NAV_CHUZUFANG);
	}
	
	public static WebLink getChuZuFangImg(){
		return new WebLink(LJLocators.HOME_NAV_CHUZUFANG_IMG);
	}
	
	public static WebStaticText getSearchDiTieFang(){
		return new WebStaticText(LJLocators.HOME_NAV_SEARCHDITIEFANG);
	}
	
	public static WebStaticText getSearchDiTieFangImg(){
		return new WebStaticText(LJLocators.HOME_NAV_SEARCHDITIEFANG_IMG);
	}
	
	public static WebStaticText getSearchErShouFang(){
		return new WebStaticText(LJLocators.HOME_NAV_SEARCHERSHOUFANG);
	}
	
	public static WebLink getSearchErShouFangImg(){
		return new WebLink(LJLocators.HOME_NAV_SEARCHERSHOUFANG_IMG);
	}
}
