package appobjects.web.editors;

import appobjects.web.LJAppObject;

public class RTEditor extends Editor {
	private static String locator =  "e-${unid}-bodyrich";
	public RTEToolbar toolbar;
	
	public RTEditor(String unid) {
		super(LJAppObject.parseLocator(locator, unid));
		CONTAINER_ID = locator;
		oToolbar = new RTEToolbar(unid);
		init();
	}
	
	public RTEditor() {
		CONTAINER_ID = locator;
	}
	
	@Override
	public void init() {
		super.init();
		if(oToolbar == null || !oToolbar.exists())
			oToolbar = new RTEToolbar(LJAppObject.getDynamicId());
	}
	
	public EditorType getEditorType() {
		return EditorType.RICHTEXT;
	}
}
