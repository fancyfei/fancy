package com.fancy.common.util.pdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class XpdfToText {
    // PDF文件名
    private File pdffile;
    // 转换器的存放位置，默认在c:\xpdftest\xpdf下面
    private String CONVERTOR_STORED_PATH = "E:\\tools\\Xpdf\\bin32\\";
    // 转换器的名称，默认为pdftotext
    private String CONVERTOR_NAME = "pdftotext";

    // 构造函数，参数为pdf文件的路径
    public XpdfToText(File pdffile) throws IOException {
    	this.pdffile = pdffile;
    }
    // 构造函数，参数为pdf文件的路径
    public XpdfToText(String pdffile) throws IOException {
    	this.pdffile = new File(pdffile);
    }

    // 将pdf转为文本文档，参数为目标文件的路径
    public void toTextFile(String targetfile) throws IOException {
    	toTextFile(targetfile, true);
    }

    // 将pdf转为文本文档，参数1为目标文件的路径，
    // 参数2为true则表示使用PDF文件中的布局
    public void toTextFile(String targetfile, boolean isLayout)
            throws IOException {
//    	String[] cmd = getCmd(new File(targetfile), isLayout);
        String[] cmd = getPdfInfoCmd(new File(targetfile));
        Process p = Runtime.getRuntime().exec(cmd);
        InputStream pi = p.getInputStream();
        File file = new File(targetfile);
        if(file.exists()) {
        	file.delete();
        }
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        while(true) {
        	int i = pi.read();
        	if(i == -1) {
        		break;
        	}
        	fos.write(i);
        }
        pi.close();
        fos.close();
    }
    public String getPdfRealFileName()
    		throws IOException {
//    	String[] cmd = getCmd(new File(targetfile), isLayout);
    	String[] cmd = getPdfInfoCmd();
    	Process p = Runtime.getRuntime().exec(cmd);
    	InputStream pi = p.getInputStream();
    	InputStreamReader read = new InputStreamReader(pi,"GB2312");
		BufferedReader br = new BufferedReader(read);
		try{
			String result = br.readLine().split(":")[1].replace(" ", "");
			return result;
		} catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			br.close();
		}
		return null;
    }

    // 获取PDF转换器的路径
    public String getCONVERTOR_STORED_PATH() {
        return CONVERTOR_STORED_PATH;
    }

    // 设置PDF转换器的路径
    public void setCONVERTOR_STORED_PATH(String path) {
        if (!path.trim().endsWith("\\"))
            path = path.trim() + "\\";
        this.CONVERTOR_STORED_PATH = path;
    }

    // 解析命令行参数
    private String[] getCmd(File targetfile, boolean isLayout) {
    	
    	// 命令字符
    	String command = CONVERTOR_STORED_PATH + CONVERTOR_NAME;
    	// PDF文件的绝对路径
    	String source_absolutePath = pdffile.getAbsolutePath();
    	// 输出文本文件的绝对路径
    	String target_absolutePath = targetfile.getAbsolutePath();
    	// 保持原来的layout
    	String layout = "-layout";
    	// 设置编码方式
    	String encoding = "-enc";
    	String character = "GBK";
    	// 设置不打印任何消息和错误
    	String mistake = "-q";
    	// 页面之间不加入分页
    	String nopagebrk = "-nopgbrk";
    	// 如果isLayout为false，则设置不保持原来的layout
    	if (!isLayout)
    		layout = "";
    	return new String[] { command, layout, encoding, character, mistake,
    			nopagebrk, source_absolutePath, target_absolutePath };
    }
    private String[] getPdfInfoCmd(File targetfile) {

        // 命令字符
        String command = CONVERTOR_STORED_PATH + "pdfinfo";
        // PDF文件的绝对路径
        String source_absolutePath = pdffile.getAbsolutePath();
        return new String[] { command, source_absolutePath};
    }
    private String[] getPdfInfoCmd() {
    	
    	// 命令字符
    	String command = CONVERTOR_STORED_PATH + "pdfinfo";
    	// PDF文件的绝对路径
    	String source_absolutePath = pdffile.getAbsolutePath();
    	return new String[] { command, source_absolutePath};
    }
}