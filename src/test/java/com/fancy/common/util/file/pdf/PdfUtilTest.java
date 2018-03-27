package com.fancy.common.util.file.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;

import com.lowagie.text.pdf.PdfReader;

public class PdfUtilTest {

	public static void main(String a[]) {
		
		try {
			String fileName = "x:\\book\\family books\\6历史\\pdffiles\\f15.pdf";
			String path = "x:\\book\\family books\\8法律经济军事\\pdffiles\\";
			//			PdfReader reader = new PdfReader(fileName);
//			testPdfContent(reader);
//			readPdfContent(path);
//			System.out.println(realFileName(fileName));
//			printPdfRealName(path);
//			System.out.println(readFile(new File(fileName)));
//			String targetfile = "E:\\TDDOWNLOAD\\tmp\\tmp.txt";
			readFileList(path);
//			InputStreamReader read = new InputStreamReader(new FileInputStream(targetfile),"GB2312");
//			BufferedReader br = new BufferedReader(read);
//			System.out.println(br.readLine());
//			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void readFileList(String path) {
		Set<String> allFile = new HashSet<String>();
		Iterator<File> itor = FileUtils.iterateFiles(new File(path), new String[]{"pdf"}, true);
		String tmppath = "E:\\TDDOWNLOAD\\tmp\\";
		for(;itor.hasNext();) {
			File f = itor.next();
			try {
				String name = readFileX(f);
				System.out.println(name+" <-- "+f.getName());
				if(name == null || name.contains("文件标题")) {
					name = f.getName();
				} else {
					name += ".pdf"; 
				}
				if(allFile.contains(name)) {
					name = f.getName();
				}
				allFile.add(name);
				FileUtils.copyFile(f, new File(tmppath+name));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @author: Administrator 2013-5-2 下午5:20:10
	 * @param reader
	 */
	public static void testBookMark(PdfReader reader) {
		List<String> result = PdfUtil.getBookMark(reader);
		for(String r : result) {
			System.out.println(r);
		}
		System.out.println(reader.getNamedDestination());
	}
	public static void testPdfInfo(PdfReader reader) {
		Map map = reader.getInfo(); 
		Set set = map.keySet(); 
		java.util.Iterator iterator = set.iterator(); 
		while(iterator.hasNext()){ 
			String key = (String) iterator.next(); 
			System.out.println(key + ":" + map.get(key)); 
		}
	}
	public static void testPdfContent(PdfReader reader) {
		try {
//			PdfString s = new PdfString();
			System.out.println(reader.getNumberOfPages());
			byte[] re = reader.getPageContent(5);
			System.out.println(new String(re));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void readPdfContent(String fileName){
		try {
			InputStream input = new FileInputStream(fileName);
			PDDocument document  = PDDocument.load(input);
//			readPdfFileInfo(document); 
			readPdfFileContent(document);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private static void printPdfRealName(String path) throws IOException {
		File dic = new File(path);
		for(File f1 : dic.listFiles()) {
			System.out.println(f1);
			System.out.println(realFileName(f1));
		}
	}
	private static String readFile(File f) {
		PdfboxToText pt = new PdfboxToText(f);
		return pt.getTextFromPdf();
	}
	private static String readFileX(File f) throws IOException {
		XpdfToText pt = new XpdfToText(f);
		return pt.getPdfRealFileName();
	}
	private static String realFileName(File f) {
		String realName = f.getName();
		InputStream input = null;
		PDDocument document = null;
		try {
			input = new FileInputStream(f);
			document  = PDDocument.load(input);
			PDDocumentOutline root = document.getDocumentCatalog().getDocumentOutline();
			PDOutlineItem item = root.getFirstChild();
			for(int i = 0; i < 4; i++){
				realName = item.getTitle();
				item = item.getNextSibling();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(input != null) {
					input.close();
				}
				if(document != null) {
					document.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return realName;
	}
	private static void readPdfFileContent(PDDocument document) {
		try {
			PDDocumentOutline root = document.getDocumentCatalog().getDocumentOutline();
			PDOutlineItem item = root.getFirstChild();
			while (item != null) {
				System.out.println("Item:" + item.getTitle());
				PDOutlineItem child = item.getFirstChild();
				while (child != null) {
					System.out.println("    Child:" + child.getTitle());
					child = child.getNextSibling();
				}
				item = item.getNextSibling();
			}
//			PDFTextStripper pts = new PDFTextStripper(); 
//			String content = pts.getText(document);
//			System.out.println( "内容:" + content ); 
			/*
			PDDocumentCatalog cata = document.getDocumentCatalog(); 
			List pages = cata.getAllPages(); 
			for( int i = 0; i < pages.size(); i++ ) {
				PDPage page = ( PDPage ) pages.get( i ); 
				if( null != page ) {
					PDResources res = page.findResources(); 
//					Map<String, PDXObject> xo = res.getXObjects();
//					System.out.println(xo);
//					COSBase co = res.getCOSObject();
//					System.out.println(co);
					Map<String, PDFont> f = res.getFonts();
					System.out.println(f);
				}
			}
			*/
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	/**
	 * 
	 * @author: Administrator 2013-5-16 下午1:44:53
	 * @param document
	 * @throws IOException
	 */
	private static void readPdfFileInfo(PDDocument document) throws IOException {
		PDDocumentInformation info = document.getDocumentInformation(); 
		System.out.println( "标题:" + info.getTitle() ); 
		System.out.println( "主题:" + info.getSubject() ); 
		System.out.println( "作者:" + info.getAuthor() ); 
		System.out.println( "关键字:" + info.getKeywords() ); 
		 
		System.out.println( "应用程序:" + info.getCreator() ); 
		System.out.println( "pdf 制作程序:" + info.getProducer() ); 
		 
		System.out.println( "作者:" + info.getTrapped() ); 
		 
		System.out.println( "创建时间:" + info.getCreationDate()); 
		System.out.println( "修改时间:" + info.getModificationDate());
	}
	
}
