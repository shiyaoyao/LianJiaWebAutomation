package tasks.web.util;

import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.web.WebBrowser;

public class LoadURLThread extends Thread {
	private String url;
	
	private boolean pass = false;

	public LoadURLThread(String _url) {
		super();
		this.url = _url;
	}

	@Override
	public void run() {
		try {
			if (url.isEmpty()) {
				pass = false;
				return;
			}
			System.out.println("Load URL: (" + url + ")");
			WebBrowser.loadURL(url);
			Platform.sleep(1);
			pass = true;
		} catch (Exception e) {
			System.out.println("Assertion failure...");
			pass = false;
			e.printStackTrace();
			SeleniumUtils.pressEnter();
		}
	}

	public boolean didPass() {
		return pass;
	}

}
