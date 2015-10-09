package tasks.web.util;

import java.util.ArrayList;

public class CallPoint {
	protected static ArrayList<String> traceinfo;
	protected static ArrayList<String> traceinfo_offset;
	protected static String _thisClass;
	
	protected static String getMethodInfo(int iDepth, boolean bIncludeClass, boolean bIncludeLine) {
		_thisClass = (new CallPoint()).getClass().getName();
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		String[] methodName = new String[trace.length];
		String[] className = new String[trace.length];
		String[] lineNum = new String[trace.length];
		
		int offset;
		int index;
		String target = "";
		traceinfo = new ArrayList<String>();
		traceinfo_offset = new ArrayList<String>();
		
		for(int x=0; x< trace.length; x++){
			traceinfo.add(trace[x].getClassName()+" : "+trace[x].getMethodName()+" : "+trace[x].getLineNumber());
		}
		offset = getCallerIndex(traceinfo, _thisClass);
		
		for(int i=0; i< trace.length - offset; i++){
			index=offset+i;
			methodName[i] = trace[index].getMethodName();
			className[i] = trace[index].getClassName();
			lineNum[i] = String.valueOf(trace[index].getLineNumber());
			traceinfo_offset.add(className[i]+" : "+methodName[i]+" : "+lineNum[i]);
		}
		
		target = bIncludeClass ? className[iDepth]+" : "+methodName[iDepth] : methodName[iDepth];
		target = bIncludeLine ? target+" : "+lineNum[iDepth] : target;
		
		return target;
	}
	
	private static int getCallerIndex(ArrayList<String> trace, String targetClass) {
		boolean flag = false;
		for (int x=0; x<trace.size(); x++){
			if(!flag && trace.get(x).startsWith(targetClass)){
				flag = true;
				continue;
			}
			if(flag && !trace.get(x).startsWith(targetClass))
				return x;
		}
		return 2;
	}
	
	public static String methodName() {
		return getMethodInfo(0, false, false);
	}
	
	public static String methodCallPoint() {
		return getMethodInfo(0, true, true);
	}
	
	public static String testcaseName() {
		String tc = testcaseCallPoint();
		if(tc.startsWith("NO"))
			return tc;
		tc = tc.replaceAll("\\s*:.*","");
		return tc;
	}
	
	public static String testcaseCallPoint(){
		String testMain = "testMain";
		String testSetUp = "testSetUp";
		ArrayList<String> trace = callTrace();
		for(String s : trace){
			if(s.contains(testMain)){
				s = s.replaceAll(":\\s*testMain\\s*", "");
				return s;
			}
		}
		
		for(String s : trace){
			if(s.contains(testSetUp)){
				s = s.replaceAll(":\\s*"+testSetUp+"\\s*", "");
				return s;
			}
		}
		return "NO : TESTCASE";
	}
	
	public static String callPoint(){
		String s = "["+methodCallPoint()+"] ["+testcaseCallPoint()+"]";
		return s;
	}
	
	public static ArrayList<String> callTrace(){
		getMethodInfo(0, true, true);
		return traceinfo_offset;
	}
}
