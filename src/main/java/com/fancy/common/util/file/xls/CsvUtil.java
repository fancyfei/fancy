package com.fancy.common.util.file.xls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * CSV格式的文本转换工具类
 * 
 * @author fancy 2012-7-10
 * @version
 */
public class CsvUtil {

	/**
	 * 从流中读取列表
	 * 
	 * @author: fancy 2012-7-10
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<String[]> read(InputStreamReader is) throws IOException {
		BufferedReader buf = new BufferedReader(is);
		ArrayList<String[]> spList = new ArrayList<String[]>();
		try {
			for (String stemp = buf.readLine(); stemp != null;) {
				if (stemp == null || "".equals(stemp)) {
					continue;
				}
				String[] array = stemp.split(",");
				if (array == null || array.length < 0) {
					continue;
				}
				spList.add(array);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			buf.close();
		}
		return spList;
	}
}
