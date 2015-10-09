package tasks.client;

import tasks.base.BaseTask;

import com.lianjia.BaseTestCaseInterface;
import com.lianjia.automation.core.web.WebBrowser;

public class Client extends BaseTask {
	public static void open(BaseTestCaseInterface test) {
        String url = test.getEnvProperty("url");
        WebBrowser.loadURL(url);
    }
    
	public static void open(BaseTestCaseInterface test, String url) {
        WebBrowser.loadURL(url);
    }
    
    public static void close() {
        WebBrowser.close();
    }
}
