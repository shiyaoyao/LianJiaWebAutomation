package appobjects.web.editors;

import tasks.web.util.NativeDialogUtils;
import appobjects.web.LJAppObject;
import appobjects.web.bases.IRTEToolbarStrings;
import appobjects.web.commons.LJDialog;
import appobjects.web.commons.LJMenu;

import com.lianjia.BaseAssert;
import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public class RTEToolbar extends AbstractToolbar implements  IRTEToolbarStrings {

	private static String idPattern = "e-${unid}-bodyrich-commands";
	private static String locatorPattern = "//div[@id='${element_id}']"; 
	private SeleniumTestObject toolBar;
	public String toolbarId;
	public String locator;
	
	public RTEToolbar(String unid) {
		toolbarId = LJAppObject.parseLocator(idPattern, unid);
		locator = LJAppObject.parseLocator(locatorPattern, toolbarId);
		toolBar = new SeleniumTestObject(locator);
	}
	
	private void init() {
		if(toolBar == null || !toolBar.exists()) {
			toolbarId = LJAppObject.parseLocator(idPattern);
			locator = LJAppObject.parseLocator(locatorPattern, toolbarId);
			toolBar = new SeleniumTestObject(locator);
			return;
		}
	}
	
	@Override
	public boolean exists() {
		init();
		return toolBar.exists();
	}
	protected SeleniumTestObject getIcon(String icon) {
		init();
		return new SeleniumTestObject(locator+"//img[@command='"+icon+"']");
	}
	
	protected SeleniumTestObject boldIcon() {
		return getIcon(BOLD_ICON);
	}
	
	protected SeleniumTestObject italicIcon() {
		return getIcon(ITALIC_ICON);
	}
	
	protected SeleniumTestObject underlineIcon() {
		return getIcon(UNDERLINE_ICON);
	}
	
	public SeleniumTestObject strikethroughIcon() {
		return getIcon(STRIKETHROUGH_ICON);
	}	
	
	public SeleniumTestObject backgroundColorIcon() {
		return getIcon(BACKGROUNDCOLOR_ICON);
	}
	
	protected SeleniumTestObject tableIcon() {
		return getIcon(INSERTTABLE_ICON);
	}
	
	protected SeleniumTestObject bulletedListIcon() {
		return getIcon(UNORDERDLIST_ICON);
	}
	
	protected SeleniumTestObject numberedListIcon() {
		return getIcon(ORDEREDLIST_ICON);
	}
	
	protected SeleniumTestObject indentIcon() {
		return getIcon(INDENT_ICON);
	}
	
	protected SeleniumTestObject outdentIcon() {
		return getIcon(OUTDENT_ICON);
	}
	
	protected SeleniumTestObject horizontalRuleIcon() {
		return getIcon(HORIZONTALRULE_ICON);
	}
		
	public SeleniumTestObject insertImageIcon() {
		return getIcon(INSERTIMAGE_ICON);
	}
	
	public SeleniumTestObject insertImageIcon_IE() {
		return getIcon(SHOW_INSERTIMAGE_MENU);
	}
	
	// FONT MENU STUFF
	protected SeleniumTestObject getButton_FontFamily(){
		SeleniumTestObject btn = new SeleniumTestObject("//td[contains(@id,'"+ toolbarId + "-fontname-') and not(contains(@class,'s-nodisplay'))]");
		if(btn.exists())
			return new SeleniumTestObject(btn.getId());
		return new SeleniumTestObject("");
	}
	
	protected LJMenu getMenu_FontFamily(){
		return new LJMenu("//div[@id='"+ toolbarId + "-fontnamepicker']");
	}
	
	protected SeleniumTestObject getButton_FontSize(){
		SeleniumTestObject btn = new SeleniumTestObject("//td[contains(@id,'"+ toolbarId + "-fontsize-') and not(contains(@class,'s-nodisplay'))]");
		if(btn.exists())
			return new SeleniumTestObject(btn.getId());
		return new SeleniumTestObject("");
	}
	
	protected LJMenu getMenu_FontSize(){
		return new LJMenu("//div[@id='"+ toolbarId + "-fontsizepicker']");
	}
	
	protected LJMenu getMenu_Align() {
		return new LJMenu("//div[@id='"+ toolbarId + "-alignmentpicker']");
	}
	
	protected LJMenu getMenu_Heading() {
		return new LJMenu("//div[@id='" + toolbarId + "-headingpicker']");
	}
	
	protected SeleniumTestObject getMenu_TextColor() {
		return new SeleniumTestObject("//div[@id='" + toolbarId + "-colorpicker']");
	}
	
	public LJMenu getMenu_Image() {
		return new LJMenu("//div[@id='" + toolbarId + "-imagemenus']");
	}
	
	public LJDialog getDialog_InsertImage() {
		return new LJDialog("//div[@id='e-dialog-insertimageprompt']");
	}
	
	protected SeleniumTestObject getDialogButton_BrowseImage() {
		return new SeleniumTestObject("//div[@id='e-dialog-insertimageprompt-container']//input[@id='HaikuUploadImage']");
	}
	
	protected SeleniumTestObject getDialogButton_InsertImage() {
		return new SeleniumTestObject("//div[@id='e-dialog-insertimageprompt-container']//input[@type='button'][1]");
	}
	protected SeleniumTestObject getTextColorMenuItem(String sHexTitle) {
		String loc = getMenu_TextColor().getObjectLocator()+"//td[@title='${color}']";
		SeleniumTestObject wo = new SeleniumTestObject(loc.replace("${color}", sHexTitle.toLowerCase()));
		if(!wo.exists())
			wo = new SeleniumTestObject(loc.replace("${color}", sHexTitle.toUpperCase()));
		return wo;
	}
	
	protected SeleniumTestObject getMenu_BackgroundColor() {
		return new SeleniumTestObject("//div[@id='" + toolbarId + "-bkcolorpicker']");
	}
	
	protected SeleniumTestObject getBackgroundColorMenuItem(String sHexTitle) {
		String loc = getMenu_BackgroundColor().getObjectLocator()+"//td[@title='${color}']";
		SeleniumTestObject wo = new SeleniumTestObject(loc.replace("${color}", sHexTitle.toLowerCase()));
		if(!wo.exists())
			wo = new SeleniumTestObject(loc.replace("${color}", sHexTitle.toUpperCase()));
		return wo;
	}

	@Override
	public String getSelectedFontFamily() {
		return getButton_FontFamily().getText();
	}

	@Override
	public String getSelectedFontSize() {
		return getButton_FontSize().getText();
	}

	@Override
	public void insertImage(String imageFilePath) {
		selectInsertImage();
		getDialog_InsertImage().waitForVisibleElement(1000);
		getDialogButton_BrowseImage().click();
		Platform.sleep(1);
		try {
			BaseAssert.assertTrue("File added through Native File Browser",NativeDialogUtils.selectFileFromNativeBrowseDialog(imageFilePath));
		} catch (Exception e) {
			BaseAssert.assertTrue("File added through Native File Browser", false);
		}
		Platform.sleep(1);
		getDialogButton_InsertImage().click();
		
	}

	@Override
	public void selectAlign(Align align) {
		getMenu_Align().waitForVisibleElement(1000);
		getMenu_Align().selectItem(align.ordinal());
		
	}

	@Override
	public void selectFontFamily(FontFamily family) {
		getButton_FontFamily().click();
		getMenu_FontFamily().selectItem(family.family);
		
	}

	@Override
	public void selectFontSize(FontSize size) {
		fontSize().click();
		getMenu_FontSize().selectItem(size.pixels);
	}

	@Override
	public void selectHeadingSize(Heading h) {
		getMenu_Heading().waitForVisibleElement(1000);
		getMenu_Heading().selectItem(h.ordinal());
	}

	@Override
	public void selectHeadingSize(int iH) {
		getMenu_Heading().waitForVisibleElement(1000);
		getMenu_Heading().selectItem(iH);
	}

	@Override
	public void selectTextColor(Color color) {
		selectTextColor(color.hex);
	}

	@Override
	public void selectTextColor(String sHexColor) {
		if(sHexColor.isEmpty()){
			return;
		}
		String colorTitle = sHexColor.startsWith("#") ? sHexColor : "#" + sHexColor;
		getMenu_TextColor().waitForVisibleElement(1000);
		SeleniumTestObject wo = getTextColorMenuItem(colorTitle);
		if(!wo.exists()) {
			return;
		}
		wo.click();
	}

	@Override
	public void selectBackgroundColor(Color color) {
		if(color == null) {
			backgroundColorIcon().click();
			return;
		}
		if(!backgroundColorIcon().exists()){
			BaseAssert.logCompare(false, false, "BACKGROUND COLOR SELECTION IS NOT SUPPORTED BY THIS EDITOR!");
			return;
		}
		String sHexColor = color.hex;
		String colorTitle = sHexColor.startsWith("#") ? sHexColor : "#" + sHexColor;
		backgroundColorIcon().click();
		getMenu_BackgroundColor().waitForVisibleElement(1000);
		SeleniumTestObject wo = getBackgroundColorMenuItem(colorTitle);
		if(!wo.exists()) {
			return;
		}
		wo.click();
	}
	
	protected SeleniumTestObject fontFamily() {
		return getButton_FontFamily();
	}

	
	protected SeleniumTestObject fontSize() {
		return getButton_FontSize();
	}

	
	protected SeleniumTestObject undoButton() {
		return getIcon(UNDO_ICON);
	}

	@Override
	public void selectBold() {
		boldIcon().click();
	}

	@Override
	public void selectUnderline() {
		underlineIcon().click();
	}

	@Override
	public void selectStrikethrough() {
		if(!strikethroughIcon().exists()){
			BaseAssert.logCompare(false, false, "THE STRIKETHROUGH ATTRIBUTE IS NOT SUPPORTED BY THIS EDITOR!");
			return;
		}
		strikethroughIcon().click();
	}

	@Override
	public void selectBulletedList() {
		bulletedListIcon().click();
	}

	@Override
	public void selectHorizontalRule() {
		horizontalRuleIcon().click();
	}

	@Override
	public void selectInsertImage() {
		SeleniumTestObject wo = insertImageIcon();
		if(!wo.exists())
			wo = insertImageIcon_IE();
		wo.click();
	}

	@Override
	public void selectInsertLink() {
	}

	@Override
	public void selectItalics() {
		italicIcon().click();
	}

	@Override
	public void selectNumberedList() {
		numberedListIcon().click();
	}

	@Override
	public void selectPageBreak() {
	}

	@Override
	public void selectIndent() {
		indentIcon().click();
	}

	@Override
	public void selectOutdent() {
		outdentIcon().click();
	}	
	
	@Override
	public void selectUndo() {
		underlineIcon().click();
	}

	@Override
	public boolean supportsStrikethrough() {
		return strikethroughIcon().exists();
	}

	@Override
	public boolean supportsBackgroundColor() {
		return backgroundColorIcon().exists();
	}
	
	
}
