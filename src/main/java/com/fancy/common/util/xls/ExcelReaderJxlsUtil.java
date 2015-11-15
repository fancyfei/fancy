package com.fancy.common.util.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jxls.reader.ReaderBuilder;
import net.sf.jxls.reader.XLSReader;
import net.sf.jxls.reader.XLSSheetReader;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.xml.sax.SAXException;

/**
 * Excel处理工具类
 * 
 * @author fancy 2012-6-19
 * @version
 */
public class ExcelReaderJxlsUtil {
	
	private InputStream config;
	
	private XLSReader mainReader;
	
	public ExcelReaderJxlsUtil(InputStream config) {
		rebuild(config);
	}
	
	public ExcelReaderJxlsUtil(String configPath) {
		rebuild(configPath);
	}
	public void rebuild(String configPath) {
		try {
			this.config = new FileInputStream(configPath);
			mainReader = ReaderBuilder.buildFromXML(config);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	public void rebuild(InputStream config) {
		try {
			this.config = config;
			mainReader = ReaderBuilder.buildFromXML(config);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 将mainReader中为空名的解释器更名为sheetName
	 * 
	 * @author: fancy 2012-6-19 下午05:57:01
	 * @param mainReader
	 * @param sheetName
	 */
	public void addDefaultXLSSheetReader(String sheetName) {
		Map<?, ?> map = mainReader.getSheetReaders();
		if (map != null && map.get(null) != null) {
			mainReader.addSheetReader(sheetName, (XLSSheetReader) map.get(null));
			map.remove(null);
		}
	}
	
	/**
	 * 解析XLS文件到类中
	 * <br>只读列表并且xml配置为paydatas对象的列表
	 * @author: fancy 2012-6-19 下午06:08:38
	 * @param dataXLS
	 * @param xmlConfig
	 * @return
	 * @throws Exception
	 */
	public List<Object> readXLSList(String dataXLS) throws Exception {
		Map<String, List<Object>> beans = new HashMap<String, List<Object>>();
		InputStream inputXLS = null;
		try {
			inputXLS = new FileInputStream(dataXLS);
		} catch (FileNotFoundException e) {
			throw new Exception("文件不存在");
		}
		// 加入默认值
		addDefaultXLSSheetReader(getSheetName(new FileInputStream(dataXLS), 0));
		List<Object> paydatas = new ArrayList<Object>();
		beans.put("paydatas", paydatas);
		mainReader.read(inputXLS, beans);
		return beans.get("paydatas");
	}

	/**
	 * 获得index对应的Sheet名
	 * 
	 * @author: fancy 2012-6-19 下午05:44:00
	 * @param xls
	 * @param index
	 * @return
	 * @throws Exception
	 */
	public static String getSheetName(InputStream xls, int index) throws Exception {
		Workbook workbook = WorkbookFactory.create(xls);
		return workbook.getSheetName(index);
	}

	/**
	 * 获得index对应的Sheet名
	 * 
	 * @author: fancy 2012-6-19 下午05:45:48
	 * @param xls
	 * @param index
	 * @return
	 * @throws Exception
	 */
	public static String getSheetName(File xls, int index) throws Exception {
		Workbook workbook = WorkbookFactory.create(xls);
		return workbook.getSheetName(index);
	}
}
