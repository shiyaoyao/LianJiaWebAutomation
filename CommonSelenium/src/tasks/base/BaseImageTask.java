package tasks.base;

import java.net.URL;
import java.security.CodeSource;

public abstract class BaseImageTask extends BaseTask {

	public String getJarName(){
		CodeSource src = this.getClass().getProtectionDomain().getCodeSource();
		String jarS = "";
			 
		if(src!=null){
			URL jar = src.getLocation();
			jarS = jar.toString();
			jarS = jarS.substring(jarS.lastIndexOf("/")+1);
		}
		return jarS;
	}
}
