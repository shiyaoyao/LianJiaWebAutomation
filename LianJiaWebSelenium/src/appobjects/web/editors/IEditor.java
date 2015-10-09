package appobjects.web.editors;
import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public interface IEditor {

	enum EditorType {RICHTEXT, CKEDITOR, PLAINTEXT};
	
	//void init();
	public void setEditorContent(String content);
	public String getEditorContent();
	public String getEditorPlainText();
	public void setFocus();
	public boolean exists();
	public SeleniumTestObject getButtonOnEditor();
	
	public EditorType getEditorType();
}
