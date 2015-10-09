package appobjects.web.commons;

import java.util.ArrayList;

public class LJListView extends LJContainer{
	public static char nonbreakingspace = '\u00a0';
	public LJContainer rowContainer;
		
	public LJListView(String listId) {
		super(listId);
	}	
	
	public LJContainer getViewContainer(String containerLocator) {
		return new LJContainer(containerLocator);
	}
	
	public class ListRow extends LJContainer {
		public String rowID;
		public int rowIndex;
		
		public ListRow() {
			super("css=li[data-index='0']");
		}

		public ListRow(int rowIndex) {
			super("css=li[data-index='" + rowIndex + "']");
		}
		
	}
	
	public ListRow getRow(int iRow) {
		return new ListRow(iRow);
	}

	public ListRow selectRow(int i, int iClickAction) {
		ListRow row = getRow(i);
		if (row.exists()) {
			selectRow(row, iClickAction);
			return row;
		}
		return null;
	}

	public ArrayList<LJContainer> getChildrenWithDataIndex(String sLocator){
		int i = 0;
		ArrayList<LJContainer> children = new ArrayList<LJContainer>();
		LJContainer child = new LJContainer(sLocator+"'0']");
		while(child.exists() && i < 100){
			children.add(child);
			i++;
			child = new LJContainer(sLocator+"'" + i + "']");
		}
		return children;
	}
	
	/**
	 * Selects the specified row.
	 * 
	 * Use one of the class static strings, LEFTCLICK, DOUBLECLICK, RIGHTCLICK
	 * to specify the click action
	 * 
	 * @param row
	 * @param iClickAction
	 *            LEFTCLICK, DOUBLECLICK, or RIGHTCLICK
	 */
	public void selectRow(ListRow row, int iClickAction) {
		if (row.exists()) {
				row.click();
		}
	}
	
	public LJListView getChildObject(String sLocator) {
		return new LJListView(sLocator);
	}
	
}
