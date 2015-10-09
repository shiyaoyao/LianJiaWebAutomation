package appobjects.web.commons;

import org.apache.log4j.Logger;

import tasks.web.util.DateUtils;

import com.lianjia.StringFetch;
import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public class LJDialog extends LJContainer {
	/**
	 * The common Dialog class: This class creates an LianJia dialog object which has the characteristics
	 * of dealing with LianJia Dialog. 
	 *
	 */
	protected static final Logger log = Logger.getLogger(LJDialog.class);
	
	public static final String BUTTONNAME_OK = StringFetch.getString("D_OK");
	public static final String BUTTONNAME_CANCEL = StringFetch.getString("D_CANCEL");
	public static final String BUTTONNAME_YES = StringFetch.getString("D_YES");
	public static final String BUTTONNAME_NO = StringFetch.getString("D_NO");
	
	public static String OKBUTTON = "//input[@type='button'][contains(@id,'-ok')]";
	public static String CANCELBUTTON = "//input[@type='button'][contains(@id,'-cancel')]";
	public static String YESBUTTON = "//input[@type='button'][contains(@id,'-yes')]";
	public static String NOBUTTON = "//input[@type='button'][contains(@id,'-no')]";
	
	public LJContainer dialog = null;
	public String MYDIALOG_ID = "";
		
	/**
	 * Constructor to find Div dialogs by ID
	 * @param myLocator either XPath or id value
	 */
	public LJDialog(String dialogLocator) {
		super(dialogLocator);
	}
	
	public static LJDialog getCurrentDialog() {
		SeleniumTestObject oDialog = new SeleniumTestObject("");
		String sId = oDialog.getId();
		if(!sId.equals("")){
			return new LJDialog(sId);
		}
		return new LJDialog("null");
	}

	/**
	 * Returns the Unique portion of an ID of a Dialog.
	 * @param sID of a Dialog
	 **/
	public static String getUNIDOfDialog(String sID){
		if(sID.startsWith(null))
			return sID.replace(null,"");
		return sID;
	}
	
	public boolean waitForExistence(){	
		double timeOut = 30.0;
		double begin = System.nanoTime();
		while(DateUtils.elapsedSeconds(begin) < timeOut){
			if(exists()){
				waitForVisibleElement(10*1000);
				break;
			}
		}
		return exists();
	}
		
	public boolean waitForDialogToOpen(long lMilliseconds) {
		boolean success = this.waitForVisibleElement(lMilliseconds);
		if(success){
			SeleniumTestObject tb = getDialogTitleBar(false);
			if(tb != null && tb.waitForMatchingElement(5000))
				log.info("Dialog Title:<code> '"+ tb.getText()+"' </code>");
			else
				log.info("No title found");
		}
		return success;
	}
	
	/**
	 * Waits for dialog to close.
	 * @param dialog
	 * @return
	 */
	public boolean waitForDialogToClose(){	
		return waitForNotVisible(60);
	}

	public LJDialog getDialogContainer(){
		if(!waitForExistence()){
			log.debug(MYDIALOG_ID +" doesn't exist");
			return null;
		}
		String sID = MYDIALOG_ID;			
		return new LJDialog(sID);
	}
	
	
	
	/**
	 * Returns the title bar of a specific dialog
	 * @param bWait TODO
	 * @param dialog
	 * @return
	 */
	public SeleniumTestObject getDialogTitleBar(boolean bWait){
		if(dialog.isVisible()) {
			if(MYDIALOG_ID.isEmpty())
				MYDIALOG_ID = dialog.getId();
		} else {
			return null;
		}
		SeleniumTestObject tb = new SeleniumTestObject( MYDIALOG_ID);	
		if(bWait){
			if(!tb.waitForExistence()){
				log.debug(MYDIALOG_ID +" doesn't exist");
				return null;
			}
		}
		return tb;
	}
	
	/**
	 * Returns the Close button of a specific dialog
	 * @param dialog
	 * @return
	 */
	public SeleniumTestObject getCloseButton(){
		if(!exists()){
			log.debug(MYDIALOG_ID +" doesn't exist");
			return new SeleniumTestObject("e-null");
		}
		String sID = MYDIALOG_ID;
		return new SeleniumTestObject(null);//"//td[@id='"+sID+"']/img[1]");	
	}

	
	
	/**
	 * Returns button of a specific dialog
	 * @param dialog
	 * @return
	 */
	public LJContainer getButton(String buttonName){
		if(!exists()){
			log.debug(MYDIALOG_ID +" doesn't exist");
			return null;
		}
		LJContainer button = new LJContainer(null);
		if( button.exists())
			return button;
		return null;
		}
	
	/**
	 * Returns the Cancel button of a specific dialog
	 * @param dialog
	 * @return
	 */
	public LJContainer getOkButton(){
		LJContainer btn = new LJContainer(null);
		if(btn.exists()){
			log.debug("Found OK button: '"+btn.getObjectLocator()+"'");
			return btn;
		}
		
		btn = new LJContainer(_getOKButton().getObjectLocator());
		return btn;
	}
	
	/**
	 * Returns the Cancel button of a specific dialog
	 * @param dialog
	 * @return
	 */
	public LJContainer getCancelButton(){
		LJContainer Cancel = getButton(BUTTONNAME_CANCEL);
		if(Cancel==null)
			Cancel = getButton(BUTTONNAME_CANCEL.toLowerCase());
		return Cancel;
	}
	public SeleniumTestObject _getCancelButton() {
		return dlgButton(StringFetch.getString("D_CANCEL"));//new SeleniumTestObject(this.getXpathLocator()+CANCELBUTTON);
	}
	
	public SeleniumTestObject getYesButton() {
		return new SeleniumTestObject(this.getXpathLocator()+YESBUTTON);
	}
	
	public SeleniumTestObject getNoButton() {
		return dlgButton(StringFetch.getString("D_NO"));//new SeleniumTestObject(this.getXpathLocator()+NOBUTTON);
	}
	
	public SeleniumTestObject _getOKButton() {
		return dlgButton(StringFetch.getString("D_OK"));//new SeleniumTestObject(this.getXpathLocator()+OKBUTTON);
	}
	
	public SeleniumTestObject getAcceptButton() {
		return acceptButton();//SeleniumTestObject.waitForAnyToExist(new SeleniumTestObject[] {getYesButton(), _getOKButton()});
	}
	
	public SeleniumTestObject getDeclineButton() {
		return cancelButton();//SeleniumTestObject.waitForAnyToExist(new SeleniumTestObject[] {_getCancelButton(), getNoButton()});
	}

	private SeleniumTestObject dlgButton(String sBtn){
		String sId = this.getId();
		String loc = sId.isEmpty() ? "css=input[type=button][value*=${element_text}]" : "css=div[id="+sId+"] input[type=button][value*=${element_text}]";
		
		SeleniumTestObject btn =  new SeleniumTestObject(null);
		return btn;
	}
	
	public SeleniumTestObject cancelButton() {
		SeleniumTestObject close = dlgButton(StringFetch.getString("D_NO"));
		SeleniumTestObject cancel = dlgButton(StringFetch.getString("D_NO"));
		SeleniumTestObject no = dlgButton(StringFetch.getString("D_NO"));
		
		return SeleniumTestObject.waitForAnyToExist(new SeleniumTestObject[] {close, cancel, no}, 1000);
	}
	
	public SeleniumTestObject acceptButton() {
		SeleniumTestObject ok = dlgButton(StringFetch.getString("D_NO"));
		SeleniumTestObject yes = dlgButton(StringFetch.getString("D_NO"));
		
		return SeleniumTestObject.waitForAnyToExist(new SeleniumTestObject[] {ok, yes}, 1000);
	}	
	
	public boolean close() {
		log.info("Closing dialog '"+getTitle()+"'");
		if(!this.exists())
			return true;
		clickCloseButton();
		return waitForDialogToClose();
	}
	/************************************* SETTERS & GETTERS **********************************************/
	
	/**
	 * Returns the Title of a dialog.
	 * @return
	 */
	public String getDialogTitle(){
		String sTitle = getTitle();
		if(!sTitle.isEmpty())
			return sTitle;
		
		SeleniumTestObject titlebar =  getDialogTitleBar(true);
		if(titlebar.exists())
			return titlebar.getTextContent();
		return "";
	}


	public String getTitle() {
		String stitle = this.getProperty("");
		if(stitle.isEmpty()){
			waitForMatchingElement(2000);
			stitle = this.getProperty("");
		}
		return stitle;
	}
	/**
	 * Returns the body message of a dialog.
	 * @return
	 */
	public String getDialogMessage(){
		LJContainer container =  getDialogContainer();
		if(container.waitForExistence())
			return container.getTextContent();
		return "";
	}
	
	public void clickOKButton(){
		getOkButton().click();
	}
	
	public void clickCloseButton(){
		getCloseButton().click();
	}
	
	public void clickButtonWithName(String sText) {
		String sName = sText;
		SeleniumTestObject btn = getButton(sName);
		if(btn != null)
			btn.click();
	}
	
}
