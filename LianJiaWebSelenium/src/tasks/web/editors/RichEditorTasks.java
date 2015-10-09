package tasks.web.editors;
import org.apache.log4j.Logger;

import appobjects.web.editors.Editor;
import appobjects.web.editors.IEditor.EditorType;
import appobjects.web.editors.RTEditor;
import appobjects.web.bases.IRTEToolbarStrings;

import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public class RichEditorTasks extends EditorTasks implements IRTEToolbarStrings{
	private static final Logger log = Logger.getLogger(RichEditorTasks.class);
	
	private static AbstractRTEditorTasks _tasks;
	private static Editor _editor;
	
	public static boolean init() {
		if(_editor == null || !_editor.exists()) {
			log.debug("INIT _EDITOR");
			_editor = new RTEditor();
			
		}
		if(!_editor.exists()) {
			_editor = null;
			_tasks = null;
			return false;
		}
		log.debug("CHECK FOR TASKS OBJECT");
		if(_editor.getEditorType() == EditorType.RICHTEXT) {
			if(_tasks == null ||_tasks.getEditorType() != EditorType.RICHTEXT){
				log.debug("INIT RTE TASKS");
				_tasks = new RTEditorTasks();
			}
		} 
		return true;
	}
	
	public static AbstractRTEditorTasks tasks() {
		if(_tasks == null || _editor == null || !_editor.exists()) {
			init();
		} else {
			log.debug("NO NEED TO INIT TASKS");
		}
		return _tasks;
	}
	
	public static Editor rte() {
		if(_editor == null || !_editor.exists())
			init();
		else 
			log.debug("NO NEED TO INIT EDITOR");
		return _editor;
	}
	
	public static String selectText(SeleniumTestObject oObject) {
		return tasks().selectText(oObject);
	}
	public static void clickBackgroundColorButton() {
		tasks().clickBackgroundColorButton();		
	}
	
	public static void clickBold() {
		tasks().clickBold();
	}
	
	public static void clickBulletedList() {
		tasks().clickBulletedList();
	}
	
	public static void clickHorizontalRule() {
		tasks().clickHorizontalRule();
	}
	
	public static void clickInsertImage() {
		tasks().clickInsertImage();
	}
	
	public static void clickInsertLink() {
		tasks().clickInsertLink();
	}
	
	public static void clickItalics() {
		tasks().clickItalics();
	}
	
	public static void clickNumberedList() {
		tasks().clickNumberedList();
	}
	
	public static void clickPageBreak() {
		tasks().clickPageBreak();
	}
		
	public static void clickStrikethrough() {
		tasks().clickStrikethrough();
	}
	
	public static void clickText(SeleniumTestObject oObject) {
		tasks().clickText(oObject);
	}
	
	public static void clickTextColorButton() {
		tasks().clickTextColorButton();
	}
	
	public static void clickUnderline() {
		tasks().clickUnderline();
	}
	
	public static void clickUndoButton() {
		tasks().clickUndoButton();		
	}
	
	public static void selectFontFamily(FontFamily fontFamily){
		rte().oToolbar.selectFontFamily(fontFamily);
	}
	
	public static void selectFontSize(FontSize fontSize) {
		rte().oToolbar.selectFontSize(fontSize);
	}
	
	public static void selectHeadingSize(Heading h) {
		rte().oToolbar.selectHeadingSize(h);
	}
	public static void selectTextColor(Color color) {
		rte().oToolbar.selectTextColor(color);
	}

	public static void selectBackgroundColor(Color color) {
		rte().oToolbar.selectBackgroundColor(color);
	}
	
	public static void selectAlign(Align align) {
		rte().oToolbar.selectAlign(align);
	}
	
	public static String getSelectedFontFamily() {
		return tasks().getSelectedFontFamily();
	}
	
	public static String getSelectedFontSize() {
		return tasks().getSelectedFontSize();
	}
	
	public static void insertImage(String sValue) {
		tasks().insertImage(sValue);
	}
	
	public static void setBackgroundColor(SeleniumTestObject oObject, Color color) {
		tasks().setBackgroundColor(oObject, color);
	}
	
	public static void setBackgroundColor(SeleniumTestObject oObject, String sHexColor) {
		tasks().setBackgroundColor(oObject, sHexColor);
	}
	
	public static void setFontFamily(SeleniumTestObject oObject, FontFamily fontFamily) {
		tasks().setFontFamily(oObject, fontFamily);
	}
	
	public static void setFontSize(SeleniumTestObject oObject, FontSize fontSize) {
		log.info("Set size: "+fontSize.pixels);
		tasks().setFontSize(oObject, fontSize);
	}
	
	public static void setHeadingSize(SeleniumTestObject oObject, Heading heading) {
		tasks().setHeadingSize(oObject, heading);
	}
	
	public static void setHeadingSize(SeleniumTestObject oObject, int iValue) {
		tasks().setHeadingSize(oObject, iValue);
	}
	
	public static void setTextColor(SeleniumTestObject oObject, Color color) {
		tasks().setTextColor(oObject, color);
	}
	
	public static void setTextColor(SeleniumTestObject oObject, String sValue) {
		tasks().setTextColor(oObject, sValue);
	}
	
	public static void alignText(SeleniumTestObject oObject, Align align) {
		tasks().alignText(oObject, align);
	}
	
	public static void italicText(SeleniumTestObject oObject) {
		log.info("Italicise text");
		tasks().italicText(oObject);
	}
	
	public static void boldText(SeleniumTestObject oObject) {
		tasks().boldText(oObject);
	}
	
	public static void strikethroughText(SeleniumTestObject oObject) {
		tasks().strikethroughText(oObject);
	}
	
	public static void underlineText(SeleniumTestObject oObject) {
		tasks().underlineText(oObject);
	}
	
	public static boolean verifyAlign(SeleniumTestObject oObject, Align align) {
		return tasks().verifyAlign(oObject, align);
	}
	
	public static boolean verifyBackgroundColor(SeleniumTestObject oObject, Color color) {
		return tasks().verifyBackgroundColor(oObject, color);
	}
	
	public static boolean verifyBackgroundColor(SeleniumTestObject oObject, String sExpectedText) {
		return tasks().verifyBackgroundColor(oObject, sExpectedText);
	}
	
	public static boolean verifyBold(SeleniumTestObject oObject) {
		return tasks().verifyBold(oObject);
	}
	
	public static boolean verifyBulletedList(SeleniumTestObject oObject, String sExpectedText) {
		return tasks().verifyBulletedList(oObject, sExpectedText);
	}
	
	public static boolean verifyFontFamily(SeleniumTestObject oObject, FontFamily fontFamily) {
		return tasks().verifyFontFamily(oObject, fontFamily);
	}
	
	public static boolean verifyFontSize(SeleniumTestObject oObject, FontSize fontSize) {
		log.info("Verify font size: "+fontSize.pixels);
		return tasks().verifyFontSize(oObject, fontSize);
	}
	
	public static boolean verifyHeading(SeleniumTestObject oObject, Heading heading) {
		return tasks().verifyHeading(oObject, heading);
	}
	
	public static boolean verifyHorizontalRule() {
		return tasks().verifyHorizontalRule();
	}
	
	public static boolean verifyItalics(SeleniumTestObject oObject) {
		log.info("Verify Italics");
		return tasks().verifyItalics(oObject);
	}
	
	public static boolean verifyNumberedList(SeleniumTestObject oObject, String sExpectedText) {
		return tasks().verifyNumberedList(oObject, sExpectedText);
	}
	
	public static boolean verifyStrikethrough(SeleniumTestObject oObject) {
		return tasks().verifyStrikethrough(oObject);
	}
	
	public static boolean verifyTextColor(SeleniumTestObject oObject, Color color) {
		return tasks().verifyTextColor(oObject, color);
	}
	
	public static boolean verifyTextColor(SeleniumTestObject oObject, String sHexColor) {
		return tasks().verifyTextColor(oObject, sHexColor);
	}
	
	public static boolean verifyUnderline(SeleniumTestObject oObject) {
		return tasks().verifyUnderline(oObject);
	}
}
