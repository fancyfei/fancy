package com.fancy.common.util.file.xls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;

import com.fancy.common.util.model.Employee;

public class TestExcelUtil extends TestCase {

	@Test
	public void testReadXLSList() {
		String xmlConfig = "E:/workspace-sprint1/fancy/src/test/resources/testdepartments.xml";
		String dataXLS = "E:/workspace-sprint1/fancy/src/test/resources/departmentdata.xls";
		List<Object> result = null;
		try {
			ExcelReaderJxlsUtil util = new ExcelReaderJxlsUtil(xmlConfig);
			result = util.readXLSList(dataXLS);
		} catch (Exception e) {
			e.printStackTrace();
			fail("错误：" + e.getMessage());
		}
		System.out.println(result);
		assertNotNull(result);
	}
	@Test
	public void testWriteXLSList() {
		String template = "E:/workspace-sprint1/fancy/src/test/resources/departmenttemplate.xlsx";
		String destFile = "E:/departmentdata.xls";
		try {
			ExcelWriterBatchUtil writer = new ExcelWriterBatchUtil(template);
			Map<String, Object> data = new HashMap<String, Object>();
			List<Employee> empList = new ArrayList<Employee>();
			for(int i = 0; i < 1000; i++){
				empList.add(new Employee("企业在生产经营过程中为获得另一项资产-name"+i, i+20, 1000+100*i, 50*i));
			}
			data.put("excelList", empList);
			writer.transform(data);
			empList.clear();
			Map<String, Object> data1 = new HashMap<String, Object>();
			data1.put("excelList", empList);
			for(int i = 0; i < 1000; i++){
				empList.add(new Employee("追加数据而不覆盖原来数据的例子-name"+i, i+20, 1000+100*i, 50*i));
			}
			writer.transform(data);
			writer.write(destFile);
		} catch (Exception e) {
			e.printStackTrace();
			fail("错误：" + e.getMessage());
		}
	}

}
