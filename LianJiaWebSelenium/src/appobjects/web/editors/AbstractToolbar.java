package appobjects.web.editors;
import appobjects.web.bases.IRTEToolbarStrings;


public abstract class AbstractToolbar implements IRTEToolbarStrings {

	public abstract boolean exists();
	
	public abstract void selectBold();

	public abstract void selectUnderline();
	
	public abstract void selectStrikethrough();
	
	public abstract boolean supportsStrikethrough();

	public abstract void selectBulletedList();

	public abstract void selectHorizontalRule();

	public abstract void selectInsertImage();

	public abstract void selectInsertLink();

	public abstract void selectItalics();

	public abstract void selectNumberedList();

	public abstract void selectPageBreak();
	
	public abstract void selectIndent();

	public abstract void selectOutdent();

	public abstract String getSelectedFontFamily();

	public abstract String getSelectedFontSize();

	public abstract void insertImage(String imageFilePath);

	public abstract void selectAlign(Align align);
	
	public void alignLeft() {
		selectAlign(Align.LEFT);
	}
	
	public void alignCenter() {
		selectAlign(Align.CENTER);
	}
	
	public void alignRight() {
		selectAlign(Align.RIGHT);
	}
	
	public abstract void selectFontFamily(FontFamily family);

	public abstract void selectFontSize(FontSize size);

	public abstract void selectHeadingSize(Heading h);

	public abstract void selectHeadingSize(int iH);

	public abstract void selectTextColor(Color color);

	public abstract void selectTextColor(String sColor);

	public abstract void selectBackgroundColor(Color color);
	
	public abstract boolean supportsBackgroundColor();
	
	public abstract void selectUndo();

	
}
