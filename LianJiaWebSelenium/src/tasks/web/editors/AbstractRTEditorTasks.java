package tasks.web.editors;

import appobjects.web.editors.IEditor.EditorType;
import appobjects.web.bases.IRTEToolbarStrings;

import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public abstract class AbstractRTEditorTasks extends EditorTasks implements IRTEToolbarStrings {
	
	public abstract void init();

	public abstract EditorType getEditorType();
	
	public abstract String selectText(SeleniumTestObject oObject);

	public abstract void clickBackgroundColorButton();

	public abstract void clickBold();

	public abstract void clickBulletedList();

	public abstract void clickHorizontalRule();

	public abstract void clickInsertImage();

	public abstract void clickInsertLink();

	public abstract void clickItalics();

	public abstract void clickNumberedList();

	public abstract void clickPageBreak();

	public abstract void clickStrikethrough();

	public abstract void clickText(SeleniumTestObject oObject);

	public abstract void clickTextColorButton();

	public abstract void clickUnderline();
	
	public abstract void clickUndoButton();

	public abstract String getSelectedFontFamily();

	public abstract String getSelectedFontSize();

	// public abstract void insertImage(Boolean b1, Boolean b2);
	public abstract void insertImage(String sValue);

	// public abstract void pasteImageActiveX(String s, Boolean b);
	
	public abstract void setAlign(SeleniumTestObject oObject, Align align);
	
	public abstract void setBulletedList(SeleniumTestObject oObject);
	
	public abstract void setNumberedList(SeleniumTestObject oObject);

	public abstract void setBackgroundColor(SeleniumTestObject oObject, Color color);

	public abstract void setBackgroundColor(SeleniumTestObject oObject, String sValue);

	public abstract boolean supportsBackgroundColor();
	
	public abstract void setFontFamily(SeleniumTestObject oObject, FontFamily fontFamily);

	public abstract void setFontSize(SeleniumTestObject oObject, FontSize fontSize);

	public abstract void setHeadingSize(SeleniumTestObject oObject, Heading heading);

	public abstract void setHeadingSize(SeleniumTestObject oObject, int iValue);

	public abstract void setTextColor(SeleniumTestObject oObject, Color color);

	public abstract void setTextColor(SeleniumTestObject oObject, String sValue);

	public abstract void alignText(SeleniumTestObject oObject, Align align);

	public abstract void italicText(SeleniumTestObject oObject);

	public abstract void boldText(SeleniumTestObject oObject);

	public abstract void strikethroughText(SeleniumTestObject oObject);
	
	public abstract boolean supportsStrikethrough();

	public abstract void underlineText(SeleniumTestObject oObject);

	public abstract boolean verifyAlign(SeleniumTestObject oObject, Align align);

	public abstract boolean verifyBackgroundColor(SeleniumTestObject oObject, Color color);

	public abstract boolean verifyBackgroundColor(SeleniumTestObject oObject, String sValue);

	public abstract boolean verifyBold(SeleniumTestObject oObject);

	public abstract boolean verifyBulletedList(SeleniumTestObject oObject, String sValue);

	public abstract boolean verifyFontFamily(SeleniumTestObject oObject, FontFamily fontFamily);

	public abstract boolean verifyFontSize(SeleniumTestObject oObject, FontSize fontSize);

	public abstract boolean verifyHeading(SeleniumTestObject oObject, Heading heading);

	public abstract boolean verifyHorizontalRule();

	public abstract boolean verifyItalics(SeleniumTestObject oObject);

	public abstract boolean verifyNumberedList(SeleniumTestObject oObject, String sValue);

	public abstract boolean verifyStrikethrough(SeleniumTestObject oObject);

	public abstract boolean verifyTextColor(SeleniumTestObject oObject, Color color);

	public abstract boolean verifyTextColor(SeleniumTestObject oObject, String sValue);

	public abstract boolean verifyUnderline(SeleniumTestObject oObject);

	public abstract void selectFontFamily(FontFamily family);

	public abstract void selectFontSize(FontSize size);

	public abstract void selectHeadingSize(Heading h);

	public abstract void selectTextColor(Color color);

	public abstract void selectBackgroundColor(Color color);
	
	public abstract void selectAlign(Align align);
}
