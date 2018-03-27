package com.fancy.common.util.file.pdf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class PdfboxToText {
	private File file;
	public PdfboxToText(File file){
		this.file = file;
	}
	public PdfboxToText(String filePath){
		this.file = new File(filePath);
	}
	public String getTextFromPdf(){
		String result = null;
		FileInputStream is = null;
        PDDocument document = null;
        try {
            is = new FileInputStream(file);
            PDFParser parser = new PDFParser(is);
            parser.parse();
            document = parser.getPDDocument();
            PDFTextStripper stripper = new PDFTextStripper();
            result = stripper.getText(document);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {is.close();}catch(IOException e){e.printStackTrace();}
            }
            if (document != null) {
            	try{document.close();}catch (IOException e){e.printStackTrace();}
            }
        }
        return result;
	}

	public void toTextFile(String filePath){
		String pdfContent = getTextFromPdf();
		try{
			File f = new File(filePath);
			if(!f.exists()){
				System.out.println("not exist");
				f.createNewFile();
			}
			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.write(pdfContent);
			output.close();
		}catch (Exception e) {
		e.printStackTrace();
		}
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}	
}
