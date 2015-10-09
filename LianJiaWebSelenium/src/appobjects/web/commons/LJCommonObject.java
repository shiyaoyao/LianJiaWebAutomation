package appobjects.web.commons;

import org.apache.log4j.Logger;

public class LJCommonObject extends LJContainer{
	protected static final Logger log = Logger.getLogger(LJCommonObject.class);
	
	public LJCommonObject(String sLocator){
		super(sLocator);
	}
}