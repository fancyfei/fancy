package com.fancy.common.util.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.SimpleBookmark;

public class PdfUtil {
	
	/**
	 * 得到文件的书签
	 * 
	 * @author: Administrator 2013-4-28 下午6:24:03
	 * @param reader
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getBookMark(PdfReader reader) {
		List<Map<String, Object>> bmS = SimpleBookmark.getBookmark(reader);
		List<String> result = new ArrayList<String>();
		for(Map<String, Object> bm : bmS) {
			fillBookMark(bm, result);
		}
		return result;
	}
	@SuppressWarnings("unchecked")
	private static void fillBookMark(Map<String, Object> bm, List<String> bmList) {
		bmList.add(bm.get("Title").toString());
		List<Map<String, Object>> kids = (List<Map<String, Object>>) bm.get("Kids");
		if (kids == null)
			return;
		for (Map<String, Object> k : kids) {
			fillBookMark(k, bmList);
		}
		return;
	}
	
	public static String readPdf(PdfReader reader) {
		
		return "";
	}
}
