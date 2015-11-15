package com.fancy.common.util.exception;

public class BizException {

	private static String lvmamaClassFlag = "com.lvmama";
	public static void throwException2() {
		throwException1();
	}
	public static void throwException1() {
		throwException();
	}
	public static void throwException() {
		throw new RuntimeException("Throw..");
	}
	public static void main(String[]a) {
		
		try{
			throwException2();
		} catch(Exception e) {
			System.out.println(getTrace(e));
			e.printStackTrace();
		}
		
	}
	
	
	
	public static String getTrace(Exception e){
		if(e == null) {
			return "";
		}
		StringBuilder exceptionResult = new StringBuilder(e.toString());
		StackTraceElement[] stList = e.getStackTrace();
		if(stList == null) {
			return exceptionResult.toString();
		}
		if(exceptionResult.length() > 0) {
			exceptionResult.append(" Caused by:(");
		}
		for(StackTraceElement ste : stList) {
			if(true) {
				if(!ste.getClassName().contains(lvmamaClassFlag)) {
					continue;
				}
			}
			exceptionResult.append(ste.getFileName().replace(".java", "."));
			exceptionResult.append(ste.getMethodName());
			exceptionResult.append(":");
			exceptionResult.append(ste.getLineNumber());
			exceptionResult.append("<-");
		}
		exceptionResult.append(")");
		return exceptionResult.toString();
	}
}
