package appobjects.web.editors;

import appobjects.web.LJAppObject;

public class PTEditor extends Editor {
	private static String locator = "e-${unid}-bodyplain-editor";
	
	public PTEditor(String unid) {
		super(LJAppObject.parseLocator(locator, unid));
		CONTAINER_ID = locator;
	}
	
	public PTEditor() {
		super();
		CONTAINER_ID = locator;
	}
	
	public void setEditorContent(String content) {
		init();
		oEditorContainer.setText(content);
	}
	
	public String getEditorPlainText() {
		init();
		return oEditorContainer.getProperty("value");
	}
	
	public String getEditorContent() {
		init();
		return getEditorPlainText();
	}
	
	public EditorType getEditorType() {
		return EditorType.PLAINTEXT;
	}
}
