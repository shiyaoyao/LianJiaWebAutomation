package tasks.web.editors;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;

import tasks.web.util.NativeDialogUtils;
import tasks.web.util.SeleniumUtils;
import tasks.web.util.StringUtils;
import tasks.web.util.WindowManagement;
import appobjects.web.LJAppObject;
import appobjects.web.bases.IRTEToolbarStrings;
import appobjects.web.commons.LJDialog;
import appobjects.web.commons.LJMenu;
import appobjects.web.editors.IEditor.EditorType;
import appobjects.web.editors.RTEToolbar;
import appobjects.web.editors.RTEditor;

import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.web.SeleniumCore;
import com.lianjia.automation.core.web.WebBrowser;
import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public class RTEditorTasks extends AbstractRTEditorTasks implements IRTEToolbarStrings {
	private final static Logger log = Logger.getLogger(RTEditorTasks.class);

	private RTEditor rte = new RTEditor();
	
	public RTEditorTasks() {
		init();
	}
	
	public RTEditor getEditor() {
		return rte;
	}
	
	public EditorType getEditorType() {
		return EditorType.RICHTEXT;
	}
	
	public void init() {
		if(rte.exists())
			rte.init();
	}
	
	public void setEditorFocus() {
		String unid = LJAppObject.getDynamicId();
		if(!WebBrowser.isInternetExplorer()) {
			String js = "var oFrame = window.getElementById('e-" + unid
					+ "-bodyrich-editorframe');" +
					"oFrame.focus();";
			SeleniumUtils.jsEval(js);
		}
		SeleniumTestObject.getNewWebObject("").click();
	}
	
	/**
	 * Select text ranges in the Rich Text Editor based on <span> id. All text segments you want to 
	 * select have to be bounded by span tags. You can have as many span segments as you want.<br><br>
	 * Internet Explorer limitations: IE cannot select the outer levels of a nested span structure.. 
	 * 
	 * @param id the id parameter of the text segment
	 */
	public void selectText(String id){
		String js = null;
		JavascriptExecutor jsEval = (JavascriptExecutor)SeleniumCore.getWebDriverBrowser().getWebDriverAPI();
		try {
			WindowManagement.setBrowserFocus();
		} catch (IOException e) {
			//
		}
		rte.init();
		if (WebBrowser.isInternetExplorer()){
			String selText = SeleniumTestObject.getNewWebObject(id).getText();
			js = "var oText = window.getElementById('"+id+"');" +
				"var oRange = window.body.createTextRange();"+
				"oRange.findText('"+selText+"');"+
				"oRange.select();";			
		}
		else {
		js = "var oFrame = window.getElementById('e-"+LJAppObject.getDynamicId()+"-bodyrich-editorframe');"+
					"oFrame.focus();"+
					"var selection = oFrame.contentWindow.getSelection();"+
					"var oDoc = oFrame.contentDocument;"+
					"var oRange = oDoc.createRange();"+
					"var oText = oDoc.getElementById('"+id+"');" +
					"oRange.selectNode(oText);"+
					"selection.removeAllRanges();"+
					"selection.addRange(oRange);";
		}
		try {
			 jsEval.executeScript(js);
		} catch (Exception e) {
			log.debug("jsEval exception");
		}
	}
	
	public String selectText(SeleniumTestObject textSpan) {
		selectEditorFrame();
		String spanId = textSpan.getId();
		String sText = textSpan.getText();
		if(spanId.isEmpty())
			return "";
		resetEditorFrame();
		selectText(spanId);
		return sText;
	}
	
	public void clickText(SeleniumTestObject textSpan) {
		selectEditorFrame();
		String spanId = textSpan.getId();
		if(spanId.isEmpty())
			return;
		textSpan.click();
		resetEditorFrame();
	}
	
	public void boldText(String id) {
		selectText(id);
		clickBold();
	}
	
	public void underlineText(String id) {
		selectText(id);
		clickUnderline();
	}
	
	public void italicText(String id) {
		selectText(id);
		clickItalics();
	}
	
	public void strikethroughText(SeleniumTestObject span) {
		selectText(span);
		clickStrikethrough();
	}
	
	public void clickBold() {
		rte.oToolbar.selectBold();
	}
	
	public void clickItalics() {
		rte.oToolbar.selectItalics();
	}
	
	public void clickUnderline() {
		rte.oToolbar.selectUnderline();
	}
	
	public void clickStrikethrough () {
		rte.oToolbar.selectStrikethrough();
	}
	
	public void clickBulletedList() {
		rte.oToolbar.selectBulletedList();
	}
	
	public void clickNumberedList() {
		rte.oToolbar.selectNumberedList();
	}
	
	public void clickHorizontalRule() {
		rte.oToolbar.selectHorizontalRule();
	}
	
	public void clickPageBreak() {
		rte.oToolbar.selectPageBreak();
	}
	
	public void clickInsertImage() {
		rte.init();
		rte.oToolbar.selectInsertImage();
//		SeleniumTestObject wo = rte.oToolbar.insertImageIcon();
//		if(!wo.exists())
//			wo = rte.oToolbar.insertImageIcon_IE();
//		wo.click();
	}
	
	public void insertImage(Boolean bFromMenu, Boolean bPaste){
		rte.init();
		// Looks like no test for normal insert image yet because it needs interaction with  OS dialog.
		// This is to paste image by selecting the second menu item from the drop down menu appears by
		// clicking the insert image icon which is only available for IE.
		clickInsertImage();
		if (bFromMenu) {
			LJMenu menu = ((RTEToolbar)(rte.oToolbar)).getMenu_Image();
			if (!menu.isVisible())
				Platform.sleep(1);
			if (!menu.isVisible()) {
				log.debug("Drop down menu does not appear by clicking the insert image button.");
				
			}
			else
				menu.selectItem(bPaste ? 1 : 0);
		}
		if (!bFromMenu || !bPaste) {
			/** TODO: code to handle insert image dialog */
		}
	}
	
	public void insertImage(String sImageFilePath){
		rte.init();
		rte.setFocus();
		LJDialog dlg = ((RTEToolbar) rte.oToolbar).getDialog_InsertImage();
		SeleniumTestObject oInsert = new SeleniumTestObject(dlg.getObjectLocator()+"//input[@value='Insert']");
		if(!dlg.isVisible()){
			rte.oToolbar.selectInsertImage();
		}
		SeleniumTestObject wo = new SeleniumTestObject("//input[@id='HaikuUploadImage']");
		if(!wo.exists())
			assertTrue("Insert image dialog found", false);
		Platform.sleep(2);
		try {
			NativeDialogUtils.selectFileFromNativeBrowseDialog(sImageFilePath);
			WindowManagement.setBrowserFocus();
		} catch (Exception e) {
			// 
		}
		
		if(WebBrowser.isInternetExplorer()){
			Platform.sleep(1);
			oInsert = new SeleniumTestObject(dlg.getObjectLocator()+"//input[@value='Insert']");
			oInsert.clickElement();
		} else{
			oInsert.click();
		}
	}
	/**
	 * Copies the specified image file to the clipboard and pastes it
	 * into the editor content using ActiveX control.<br><br>
	 * The method attempts to paste the image at the end of current content.
	 * @param sFileName
	 * @param bFromMenu
	 * @throws IOException
	 */
	public void pasteImageActiveX(String sFileName, Boolean bFromMenu) throws IOException {
		rte.init();
		
		if (!bFromMenu) {
			pasteImage(sFileName);
			return;
		}
		loadImageToClipboard(sFileName);
		rte.setFocus();
		insertImage(true, true);
	}
	
	public void clickInsertLink() {
		rte.oToolbar.selectInsertLink();
	}	
		
	public void selectFontFamily(FontFamily family) {
		log.debug("FontFamily: "+family.family);
		rte.init();
		rte.oToolbar.selectFontFamily(family);
	}
	public String getSelectedFontFamily(){
		rte.init();
		return rte.oToolbar.getSelectedFontFamily();
	}
	
	public void selectFontSize(FontSize fontsize){
		log.debug("FontSize: (pixels) "+fontsize.pixels+", (css) "+fontsize.css);
		rte.init();
		rte.oToolbar.selectFontSize(fontsize);
	}
	
	
	public String getSelectedFontSize(){
		rte.init();
		return rte.oToolbar.getSelectedFontSize();
	}
	
	public void selectTextColor(String sHexColor) {
		log.debug("Text Color: "+sHexColor);
		try {
			WindowManagement.setBrowserFocus();
		} catch (IOException e) {
			//
		}
		rte.init();
		rte.oToolbar.selectTextColor(sHexColor);
	}
	
	public void selectTextColor(Color textColor){
		log.debug("Text Color: "+textColor.hex);
		try {
			WindowManagement.setBrowserFocus();
		} catch (IOException e) {
			//
		}
		rte.init();
		rte.oToolbar.selectTextColor(textColor);
	}
	
	public void selectBackgroundColor(Color color) {
		log.debug("Background Color: "+color.hex);
		try {
			WindowManagement.setBrowserFocus();
		} catch (IOException e) {
			//
		}
		rte.init();
		rte.oToolbar.selectBackgroundColor(color);
		
	}
	public void selectHeadingSize(Heading size) {
		rte.init();
		rte.oToolbar.selectHeadingSize(size);
	}
	
	public void selectHeadingSize(int size) {
		rte.init();
		rte.oToolbar.selectHeadingSize(size);
	}
	
	public void selectAlign(Align align) {
		log.debug("Align: "+align.name());
		rte.init();
		rte.oToolbar.selectAlign(align);
	}
	
	public String getTextColor(SeleniumTestObject object) {
		String cssRGB = object.getWebElement().getCssValue("color");
		return rgbToHex(cssRGB);
	}
	
	public String getBackgroundColor(SeleniumTestObject object) {
		String cssRGB = object.getWebElement().getCssValue("background-color");
		return rgbToHex(cssRGB);
	}
	
	public static String rgbToHex(String cssRGB){
		log.debug("CSS color value: "+cssRGB);
		String sR;
		String sG;
		String sB;
		Pattern rgb = Pattern.compile("\\((\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})[^\\d]");
		Pattern rgbHEX = Pattern.compile("\\((\\w{2})\\s*,\\s*(\\w{2})\\s*,\\s*(\\w{2})");
		
		Matcher matcher;
		String textColor = "UNSET";
		matcher = rgb.matcher(cssRGB);
		if(matcher.find()){
			try {
				sR = String.format("%02x",Integer.valueOf(matcher.group(1)));
				sG = String.format("%02x",Integer.valueOf(matcher.group(2)));
				sB = String.format("%02x",Integer.valueOf(matcher.group(3)));
				
				textColor = "#"+sR+sG+sB;
				textColor = textColor.toUpperCase();
			} catch (Exception e) {
				log.warn("Invalid RGB string: "+cssRGB);
				return textColor;
			}
			
		} else {
			matcher = rgbHEX.matcher(cssRGB);
			if(matcher.find()){
				textColor = (matcher.group(1)+matcher.group(2)+matcher.group(3)).toUpperCase();
			} else {
				log.warn("Invalid RGB string: "+cssRGB);
				return textColor;
			}
		}
		log.debug("HEX color value: "+textColor);
		return textColor;
	}
//	private String rgbToHex(String cssRGB) {
//		log.debug("CSS color value: "+cssRGB);
//		String tmpColor;
//		Pattern patern = Pattern.compile("\\(\\d+, \\d+, \\d+");
//		Matcher matcher;
//		String textColor = "";
//		String css = cssRGB;
//		String temp = "";
//		matcher = patern.matcher(css);
//		if(matcher.find()){
//			temp = matcher.group();
//			temp = temp.substring(1);
//			String[] color = StringUtils.csvStringToArray(temp);
//			tmpColor = Integer.toHexString(Integer.valueOf(color[0]));
//			tmpColor = tmpColor.length() == 1 ? "0"+tmpColor : tmpColor;
//			temp = tmpColor;
//			tmpColor = Integer.toHexString(Integer.valueOf(color[1]));
//			tmpColor = tmpColor.length() == 1 ? "0"+tmpColor : tmpColor;
//			temp += tmpColor;
//			tmpColor = Integer.toHexString(Integer.valueOf(color[2]));
//			tmpColor = tmpColor.length() == 1 ? "0"+tmpColor : tmpColor;
//			temp += tmpColor;
//			textColor = "#"+temp.toUpperCase();
//		} else if(css.contains("#")){
//			textColor = css;//css.split("#")[1];
//		}
//		log.debug("HEX color value: "+textColor);
//		return textColor;
//	}
	
	// VERIFICATION METHODS
	
	public boolean verifyBold(SeleniumTestObject selection) {
		String bold = "strong";
		boolean success = true;
		if(!WebBrowser.isInternetExplorer()) {
			selectEditorFrame();
			bold = "b";
		}
		SeleniumTestObject wo;
		String sExpected = selection.getText();
		if(WebBrowser.isFirefox()) {
			if(selection.getProperty("tagName").equalsIgnoreCase("div"))
				wo = selection.getChildByTag(bold);
			else
				wo = selection.getParentByTag(bold);
		}
		else
			wo = selection.getChildByTag(bold);
		if(!logCompare(true, wo.exists(), bold.toUpperCase()+" tag is found"))
			success = false;
		success &= logCompare(sExpected, wo.getText(), bold.toUpperCase()+ " selection has the right text");
		
		resetEditorFrame();
		
		if(!success){
			String content = getContent();
			content = StringUtils.escapeHtmlTags(content);
			log.debug(content);
		}
		
		return success;
	}
	
	public boolean verifyItalics(SeleniumTestObject selection) {
		String italic = "em";
		boolean success = true;
		if(!WebBrowser.isInternetExplorer()) {
			selectEditorFrame();
			italic = "i";
		}
		SeleniumTestObject wo;
		String sExpected = selection.getText();
		if(WebBrowser.isFirefox())
			wo = selection.getParentByTag(italic);
		else
			wo = selection.getChildByTag(italic);
		if(!logCompare(true, wo.exists(), italic.toUpperCase()+" tag is found"))
			success = false;
		success &= logCompare(sExpected, wo.getText(), italic.toUpperCase()+ " selection has the right text");
		
		resetEditorFrame();
		
		if(!success){
			String content = getContent();
			content = StringUtils.escapeHtmlTags(content);
			log.debug(content);
		}
		return success;
	}
	
	public boolean verifyUnderline(SeleniumTestObject selection) {
		String underline = "u";
		boolean success = true;
		selectEditorFrame();
		
		SeleniumTestObject wo;
		String sExpected = selection.getText();
		if(WebBrowser.isFirefox())
			wo = selection.getParentByTag(underline);
		else
			wo = selection.getChildByTag(underline);
		if(!logCompare(true, wo.exists(), underline.toUpperCase()+" tag is found"))
			success = false;
		success &= logCompare(sExpected, wo.getText(), underline.toUpperCase()+ " selection has the right text");
		
		resetEditorFrame();
		
		if(!success){
			String content = getContent();
			content = StringUtils.escapeHtmlTags(content);
			log.debug(content);
		}
		return success;
	}
	
	public boolean verifyStrikethrough(SeleniumTestObject selection) {
		String stike = "strike";
		boolean success = true;
		
		selectEditorFrame();
		
		SeleniumTestObject wo;
		String sExpected = selection.getText();
		if(WebBrowser.isFirefox()) {
			if(selection.getProperty("tagName").equalsIgnoreCase("div"))
				wo = selection.getChildByTag(stike);
			else
				wo = selection.getParentByTag(stike);
		}
		else
			wo = selection.getChildByTag(stike);
		
		if(!logCompare(true, wo.exists(), stike.toUpperCase()+" tag is found"))
			success = false;
		success &= logCompare(sExpected, wo.getText(), stike.toUpperCase()+ " selection has the right text");
		
		resetEditorFrame();
		
		if(!success){
			String content = getContent();
			content = StringUtils.escapeHtmlTags(content);
			log.debug(content);
		}
		
		return success;
	}
	public boolean verifyFontFamily(SeleniumTestObject selection, FontFamily family) {
		boolean success = true;
		selectEditorFrame();
		
		SeleniumTestObject wo;
		String sExpected = selection.getText();
		if(WebBrowser.isFirefox())
			wo = selection.getParentByTag("font");
		else
			wo = selection.getChildByTag("font");
		
		if(!logCompare(true, wo.exists(), "FONT tag is found"))
			success = false;
		else {
			String fam = family.family;
			log.info("Expected FontFamily: ("+fam+"), Actual FontFamily: ("+wo.getProperty("face")+")");
			if(!logCompare(true, wo.getProperty("face").toLowerCase().contains(fam.toLowerCase()), "FontFamily "+fam+" is found"))
				success = false;
			else
				success &= logCompare(sExpected, wo.getText(), "Font selection has the right text");
		}
		resetEditorFrame();
		
		if(!success){
			String content = getContent();
			content = StringUtils.escapeHtmlTags(content);
			log.debug(content);
		}
		return success;
	}
	
	public boolean verifyFontSize(SeleniumTestObject selection, FontSize size) {
		boolean success = true;
		selectEditorFrame();
		
		SeleniumTestObject wo;
		String sExpected = selection.getText();
		if(WebBrowser.isFirefox())
			wo = selection.getParentByTag("font");
		else
			wo = selection.getChildByTag("font");
		
		if(!logCompare(true, wo.exists(), "FONT tag is found"))
			success = false;
		else {
			String fontsize = size.css;
			log.info("Expected FontSize: ("+fontsize+"), Actual FontSize: ("+wo.getProperty("size")+")");
			if(!logCompare(true, wo.getProperty("size").contains(fontsize), "FontSize "+fontsize+" is found"))
				success = false;
			else
				success &= logCompare(sExpected, wo.getText(), "Font selection has the right text");
		}
		resetEditorFrame();
		
		if(!success){
			String content = getContent();
			content = StringUtils.escapeHtmlTags(content);
			log.debug(content);
		}
		return success;
	}
	
	public boolean verifyTextColor(SeleniumTestObject selection, String hexColor) {
		boolean success = true;
		String textColor = hexColor.startsWith("#") ? hexColor : "#" + hexColor;
		
		selectEditorFrame();
		
		SeleniumTestObject wo;
		String sExpected = selection.getText();
		if(WebBrowser.isFirefox())
			wo = selection.getParentByTag("font");
		else
			wo = selection.getChildByTag("font");
		
		if(!logCompare(true, wo.exists(), "FONT tag is found"))
			success = false;
		else {
			String color = getTextColor(wo);
			log.info("Expected Color: ("+textColor+"), Actual Color: ("+color+")");
			if(!logCompare(textColor.toUpperCase(), color.toUpperCase(), "Text color "+textColor+" is found"))
				success = false;
			else
				success &= logCompare(sExpected, wo.getText(), "Font selection has the right text");
		}
		
		resetEditorFrame();
		
		if(!success){
			String content = getContent();
			content = StringUtils.escapeHtmlTags(content);
			log.debug(content);
		}
		return success;
	}
	
	public boolean verifyTextColor(SeleniumTestObject selection, Color textColor) {
		String hex = textColor.hex;
		return verifyTextColor(selection, hex);
	}
	
	public boolean verifyBackgroundColor(SeleniumTestObject selection, String hexColor) {
		boolean success = true;
		
		selectEditorFrame();
		String expectedText = selection.getText();
		String expectedColor = hexColor.startsWith("#") ? hexColor : "#" + hexColor;

		SeleniumTestObject wo = selection;
		
		if(WebBrowser.isFirefox())
			wo = selection.getParentByTag("span");
		else if(WebBrowser.isInternetExplorer())
			wo = selection.getChildByTag("font");
		
		if(!wo.exists()){
			return logCompare(true, false, "Target selection found");
		}
		String bgcolor = getBackgroundColor(wo);
		if(!logCompare(expectedColor.toUpperCase(), bgcolor.toUpperCase(), "Background color "+expectedColor+" is found"))
			success = false;
		else
			success &= logCompare(expectedText, wo.getText(), "Background color selection has the right text");
		
		resetEditorFrame();
		
		if(!success){
			String content = getContent();
			content = StringUtils.escapeHtmlTags(content);
			log.debug(content);
		}
		return success;
	}
	
	public boolean verifyBackgroundColor(SeleniumTestObject selection, Color hexColor) {
		String hex = hexColor.hex;
		return verifyBackgroundColor(selection, hex);
	}
	
	public boolean verifyAlign(SeleniumTestObject selection, Align align) {
		boolean success = true;
		selectEditorFrame();
		
		String sAlign = selection.getWebElement().getCssValue("text-align").toLowerCase();
		
		logCompare(Pattern.compile(align.name().toLowerCase()), sAlign, "("+selection.getText()+") is aligned correctly" );
	
//		SeleniumTestObject wo;
//		wo = selection.getParentByTag("div");
//		
//		String sExpectedText = selection.getText();
//		String sActualAlign = wo.getWebElement().getCssValue("text-align").toLowerCase();
//		if(!logCompare(true, wo.exists(), "DIV tag is found"))
//			success = false;
//		else {
//			log.info("Expected Alignment: ("+_align+"), Actual Align: ("+sActualAlign+")");
//			if(!logCompare(true, sActualAlign.contains(_align), "Alignment value "+_align+" is found"))
//				success = false;
//			else
//				success &= logCompare(sExpectedText, wo.getText(), "Alignment DIV has the right text");
//		}
		resetEditorFrame();
		
		if(!success){
			String content = getContent();
			content = StringUtils.escapeHtmlTags(content);
			log.debug(content);
		}
			
		return success;
	}
	
	public boolean verifyHeading(SeleniumTestObject selection, Heading heading) {
		boolean success = true;
		String _heading = heading.name().toLowerCase();
		selectEditorFrame();
		
		SeleniumTestObject wo;
		wo = selection.getParentByTag(_heading);
		
		String sExpectedText = selection.getText();
		if(!logCompare(true, wo.exists(), _heading.toUpperCase() + " tag is found"))
			success = false;
		else {
			success &= logCompare(Pattern.compile(sExpectedText), wo.getText(), _heading.toUpperCase() + " has the right text");
		}
		
		resetEditorFrame();
		
		if(!success){
			String content = getContent();
			content = StringUtils.escapeHtmlTags(content);
			log.debug(content);
		}
		return success;
	}

	public boolean verifyBulletedList(SeleniumTestObject selection, String sText) {
		boolean success = true;
		selectEditorFrame();
		
		SeleniumTestObject wo;
		if(!WebBrowser.isChrome())
			wo = selection.getParentByTag("ul");
		else {
			wo = new SeleniumTestObject ("//ul[ ./li[contains(.,'"+sText+"')]]");
		}
		
		String sExpectedText = selection.getText();
		if(!logCompare(true, wo.exists(), "UL tag is found"))
			success = false;
		else {
			if(!WebBrowser.isChrome())
				success &= logCompare(true, wo.getText().contains(sExpectedText), "UL has the right text");
		}
		
		resetEditorFrame();
		
		if(!success){
			String content = getContent();
			content = StringUtils.escapeHtmlTags(content);
			log.debug(content);
		}
		
		return success;
	}
	
	public boolean verifyNumberedList(SeleniumTestObject selection, String sText) {
		boolean success = true;
		selectEditorFrame();
		
		SeleniumTestObject wo;
		if(!WebBrowser.isChrome())
			wo = selection.getParentByTag("ol");
		else
			wo = new SeleniumTestObject ("//ol[ ./li[contains(.,'"+sText+"')]]");
		
		String sExpectedText = selection.getText();
		if(!logCompare(true, wo.exists(), "OL tag is found"))
			success = false;
		else {
			if(!WebBrowser.isChrome())
				success &= logCompare(true, wo.getText().contains(sExpectedText), "OL has the right text");
		}
		
		resetEditorFrame();
		
		if(!success){
			String content = getContent();
			content = StringUtils.escapeHtmlTags(content);
			log.debug(content);
		}
		return success;
	}
	
	public boolean verifyHorizontalRule() {
		boolean success = true;
		selectEditorFrame();
		
		SeleniumTestObject wo = new SeleniumTestObject("//hr");
		int count = wo.getObjectCount();
		if(!logCompare(true, count > 0, "HR tag is found"))
			success = false;
		
		resetEditorFrame();
		
		if(!success){
			String content = getContent();
			content = StringUtils.escapeHtmlTags(content);
			log.debug(content);
		}
		return success;
	}
	
	private void selectEditorFrame() {
		rte.switchToFrame();
	}
	
	private void resetEditorFrame() {
		rte.resetFrame();
	}

	@Override
	public void clickUndoButton() {
		rte.oToolbar.selectUndo();
	}

	@Override
	public void setBackgroundColor(SeleniumTestObject oObject, Color color) {
		selectText(oObject);
		selectBackgroundColor(color);
	}

	@Override
	public void setFontFamily(SeleniumTestObject oObject, FontFamily fontFamily) {
		selectText(oObject);
		selectFontFamily(fontFamily);
	}

	@Override
	public void setFontSize(SeleniumTestObject oObject, FontSize fontSize) {
		selectText(oObject);
		selectFontSize(fontSize);
	}

	@Override
	public void setHeadingSize(SeleniumTestObject oObject, Heading heading) {
		selectText(oObject);
		selectHeadingSize(heading);
	}

	@Override
	public void setHeadingSize(SeleniumTestObject oObject, int iHeading) {
		selectText(oObject);
		selectHeadingSize(iHeading);
	}

	@Override
	public void setTextColor(SeleniumTestObject oObject, Color color) {
		selectText(oObject);
		selectTextColor(color);
	}

	@Override
	public void setTextColor(SeleniumTestObject oObject, String sHexColor) {
		selectText(oObject);
		selectTextColor(sHexColor);
	}

	@Override
	public void alignText(SeleniumTestObject oObject, Align align) {
		selectText(oObject);
		selectAlign(align);
	}

	@Override
	public void italicText(SeleniumTestObject oObject) {
		selectText(oObject);
		clickItalics();
	}

	@Override
	public void boldText(SeleniumTestObject oObject) {
		selectText(oObject);
		clickBold();
	}

	@Override
	public void underlineText(SeleniumTestObject oObject) {
		selectText(oObject);
		clickUnderline();
	}

	@Override
	public void clickBackgroundColorButton() {
		rte.oToolbar.selectBackgroundColor(null);
	}

	@Override
	public void clickTextColorButton() {
		rte.oToolbar.selectTextColor("");
	}

	@Override
	public void setBackgroundColor(SeleniumTestObject oObject, String sValue) {
		selectText(oObject);
		selectBackgroundColor(Color.valueOf(sValue));		
	}

	@Override
	public void setAlign(SeleniumTestObject oObject, Align align) {
		selectText(oObject);
		rte.oToolbar.selectAlign(align);
	}

	@Override
	public void setBulletedList(SeleniumTestObject oObject) {
		selectText(oObject);
		rte.oToolbar.selectBulletedList();
	}

	@Override
	public void setNumberedList(SeleniumTestObject oObject) {
		selectText(oObject);
		rte.oToolbar.selectNumberedList();
	}

	@Override
	public boolean supportsBackgroundColor() {
		return rte.oToolbar.supportsBackgroundColor();
	}

	@Override
	public boolean supportsStrikethrough() {
		return rte.oToolbar.supportsStrikethrough();
	}
	
	public String getObjectHTML(String sLocator) {
		rte.switchToFrame();
		String html = "";
		SeleniumTestObject wo = new SeleniumTestObject(sLocator);
		if(wo.exists())
			html = wo.getProperty("outerHTML");
		rte.resetFrame();
		return html;
	}
	
	public String getObjectText(String sLocator) {
		rte.switchToFrame();
		String text = "";
		SeleniumTestObject wo = new SeleniumTestObject(sLocator);
		if(wo.exists())
			text = wo.getText();
		rte.resetFrame();
		return text;
	}
	public int getObjectCountInEditor(String sLocator) {
		rte.switchToFrame();
		int count = SeleniumTestObject.getObjectCount(sLocator);
		rte.resetFrame();
		return count;
		
	}
}
