package appobjects.web.editors;

public class CKEditor extends Editor {
	private static String locator = "";
	
	public CKEditor(String unid) {
		super(unid);
		CONTAINER_ID = locator;
	}
	
	public CKEditor() {
		super();
		CONTAINER_ID = locator;
	}
	
	public EditorType getEditorType() {
		return EditorType.CKEDITOR;
	}
}
