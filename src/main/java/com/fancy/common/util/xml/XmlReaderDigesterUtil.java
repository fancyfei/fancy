package com.fancy.common.util.xml;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;

import org.apache.commons.digester.Digester;
/**
 * XML解析工具类
 * @author fancy 2012-6-20
 * @version
 */
public class XmlReaderDigesterUtil {

	/**
	 * 解析器
	 */
	private Digester digester;
	/**
	 * 初始化解析器
	 * @param digester
	 */
	public XmlReaderDigesterUtil(Digester digester) {
		digester.setValidating(false);
		this.digester = digester;
	}
	
	public Digester getDigester() {
		return digester;
	}

	public void rebuild(Digester digester) {
		this.digester = digester;
		this.digester.setValidating(false);
	}

	/**
	 * 将XML文件流解析为对象
	 * 
	 * @author: fancy 2012-6-20 下午06:52:06
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public Object parse(InputStream is) throws Exception {
		return digester.parse(is);
	}
	/**
	 * 根据digester定义的模板解析XML流
	 * 
	 * @author: fancy 2012-6-21 上午11:01:20
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public Object parse(Reader is) throws Exception {
		return digester.parse(is);
	}
	/**
	 * 根据digester定义的模板XML文件
	 * 
	 * @author: ranlongfei 2012-7-6 下午3:36:42
	 * @param digester
	 * @param xmlFile
	 * @return
	 * @throws Exception
	 */
	public Object parse(String xmlFile) throws Exception {
		return digester.parse(new File(xmlFile));
	}
}
