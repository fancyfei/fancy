package com.fancy.common.util.xml;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;

import org.apache.commons.digester.Digester;
import org.junit.Test;

import com.fancy.common.util.model.Employee;

public class TestXmlToBeanDigesterUtil extends TestCase {

	@Test
	public void testParseXmlDigesterReader() {
		Reader is = new StringReader(
				"<list>" +
					"<emp name='FFF'><age>20</age></emp>" +
					"<emp><name>AA</name></emp>" +
					"<emp><name>BB</name></emp>" +
				"</list>"
				);
		Object o = null;
		try {
			XmlReaderDigesterUtil util = new XmlReaderDigesterUtil(build());
			o = util.parse(is);
		} catch (Exception e) {
			fail("错误" + e.getMessage());
		}
		System.out.println(o);
		assertNotNull(o);
	}

	private Digester build() {
		Digester digester = new Digester();
		digester.setValidating(false);
		digester.addObjectCreate("list", ArrayList.class);
		digester.addObjectCreate("list/emp", Employee.class);
		digester.addSetProperties("list/emp");
		
		digester.addCallMethod("list/emp/name", "setName",0);
		digester.addBeanPropertySetter("list/emp/age");
		digester.addSetNext("list/emp", "add");
		return digester;
	}
	
	public static void main(String[]a){
		HashMap m = new HashMap(10,2F);
		System.out.println(m);
	}
}
