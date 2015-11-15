package com.fancy.common.util.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;
import net.sf.jxls.util.Util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 批量 only support 2007+ 
 * 单个sheet页导出transform(String, Map<String, Object>, String)
 * @version
 */
public class ExcelWriterBatchUtil {

	private String template;
	private XLSTransformer transformer;
	private SXSSFWorkbook book;
	private int count = 1;
	
	public ExcelWriterBatchUtil(String template) {
		this.template = template;
		transformer = new XLSTransformer();
	}
	/**
	 * 单个导出
	 * 
	 * @param template
	 * @param data
	 * @param dest
	 * @throws IOException
	 */
	public void transform(String template, Map<String, Object> data, String dest) throws IOException {
		try {
			XLSTransformer transformer = new XLSTransformer();
			transformer.transformXLS(template, data, dest);
		} catch (ParsePropertyException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} 
	}
	/**
	 * 批量导出
	 * 
	 * @param data
	 * @throws FileNotFoundException
	 */
	public void transform(Map<String, Object> data) throws FileNotFoundException{
		FileInputStream templateIs = new FileInputStream(template);
		try {
			XSSFWorkbook bookTmp = (XSSFWorkbook)transformer.transformXLS(templateIs , data);
			bookTmp.setSheetName(0, "第"+count+"页");
			if(count == 1) {
				book = new SXSSFWorkbook(bookTmp);
			} else {
				Sheet newSheet = book.createSheet(bookTmp.getSheetName(0));
				Sheet sheet = bookTmp.getSheetAt(0);
				Util.copySheets(newSheet, sheet);
			}
			count++;
		} catch (ParsePropertyException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} finally{
			try {
				templateIs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void write(String destFile) throws IOException {
		File f = new File(destFile);
		if(!f.exists()) {
			f.createNewFile();
		}
		Util.writeToFile(destFile, book);
	}
}
