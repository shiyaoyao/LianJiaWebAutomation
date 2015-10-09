package tasks.web.commons;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import tasks.base.BaseTask;
import appobjects.web.commons.LJFilterBox;

public class LJFilterBoxTasks extends BaseTask {
	private static final Logger log = Logger.getLogger(LJMenuTasks.class);
	

	public static boolean isFilterOptionExist(String optionType){		
		List<WebElement> options = LJFilterBox.getAllFilterOptions();
		
		int i = 0;
    	while (i < options.size()){
    		WebElement option = options.get(i);
    		if(option.getText().equals(optionType))
    			return true;
    		i++;
    	}
    	
    	return false;
	}
	
}
