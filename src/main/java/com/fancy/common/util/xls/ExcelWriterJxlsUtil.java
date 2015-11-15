package com.fancy.common.util.xls;

import java.io.IOException;
import java.util.Map;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class ExcelWriterJxlsUtil {

	public void wirte(String template, Map<String, Object> data, String destFile) {
		XLSTransformer transformer = new XLSTransformer();
		try {
			transformer.transformXLS(template, data, destFile);
		} catch (ParsePropertyException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
