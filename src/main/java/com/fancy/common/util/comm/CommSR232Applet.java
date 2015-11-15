package com.fancy.common.util.comm;

import java.applet.Applet;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;
/**
 * SR232端口的访问：波特率9600；数据位8；端口COM1
 * @author fancy 2012-7-5
 * @version
 */
public class CommSR232Applet extends Applet implements Runnable,
		SerialPortEventListener {

	static {
		System.setSecurityManager(null);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 2986170418404511959L;

	private CommPortIdentifier portId;
	// 端口数据准备时间 1秒
	private int delayRead = 10;
	// 4k的buffer空间,缓存串口读入的数据
	private byte[] readBuffer = new byte[1024]; 

	private InputStream inputStream;

	private SerialPort serialPort;

	private Thread readThread;

	public static void main(String args[]) {
		// CommSR232Applet rs = new CommSR232Applet();
		// rs.init();
		List<Float> rv = new ArrayList<Float>();
		Float f1 = new Float(0);
		Float f2 = new Float(2);
		Float f3 = new Float(0);
		rv.add(f1);
		rv.add(f2);
		rv.add(f3);
		rv.remove(f1);
		// System.out.println("".split("=").length);
		System.out.println("= 213.00  00  ".replaceAll("[(^=)|(\\s+)]", ""));
		// System.out.println("-000.00".matches("(-)?+(\\d){3}(\\.)(\\d){2}"));
	}


	/**
	 * 初始化
	 */
	@Override
	public void init() {

		super.init();
		if (this.serialPort != null) {
			System.out.println("没有连接设置!");
			this.serialPort.close();
		}
		try {
			// 参数初始化
			// 设备超时时间 1秒
			int timeout = 1000;
			// 波特率
			int rate = 9600;
			// 数据位
			int dataBits = SerialPort.DATABITS_8;
			// 停止位
			int stopBits = SerialPort.STOPBITS_1;
			// 无奇偶校验
			int parity = SerialPort.PARITY_NONE;
			// 端口名称
			String port = "COM1";
			// 打开端口
			portId = CommPortIdentifier.getPortIdentifier(port);
			serialPort = (SerialPort) portId.open("SerialReader", timeout);
			inputStream = serialPort.getInputStream();
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
			serialPort.setSerialPortParams(rate, dataBits, stopBits, parity);

		} catch (PortInUseException e) {
			System.out.println("端口已经被占用!");
		} catch (TooManyListenersException e) {
			System.out.println("端口监听者过多!");
		} catch (UnsupportedCommOperationException e) {
			System.out.println("端口操作命令不支持!");
		} catch (NoSuchPortException e) {
			System.out.println("端口不存在!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		readThread = new Thread(this);
		readThread.start();
	}

	/**
	 * 事件处理
	 */
	@Override
	public void serialEvent(SerialPortEvent event) {
		try {
			// 等待1秒钟让串口把数据全部接收后在处理
			Thread.sleep(delayRead);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		switch (event.getEventType()) {
		case SerialPortEvent.BI: // 10
		case SerialPortEvent.OE: // 7
		case SerialPortEvent.FE: // 9
		case SerialPortEvent.PE: // 8
		case SerialPortEvent.CD: // 6
		case SerialPortEvent.CTS: // 3
		case SerialPortEvent.DSR: // 4
		case SerialPortEvent.RI: // 5
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2
			break;
		case SerialPortEvent.DATA_AVAILABLE: // 1
			try {

				inputStream.read(readBuffer);

				String str = new String(readBuffer);

				this.setRS232Value(this.calcValue(str));

			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	/**
	 * 
	 * 接收事件
	 * @author: fancy 2012-7-6 上午9:41:02
	 * @param value
	 * @return
	 */
	public float calcValue(String value) {
		if (value == null || value.equals("")) {
			return 0F;
		}
		value = value.replaceAll("(^=)", "").trim();
		String[] vArray = value.split("=");

		List<Float> rv = new ArrayList<Float>();
		boolean isNormal = true;

		if (vArray.length > 0) {
			if (vArray[0].trim().matches("(\\d){2}(\\.)(\\d){3}(-)?+")) {
				isNormal = false;
			}
		} else {
			return 0F;
		}
		if (vArray.length == 1) {
			if (!isNormal) {
				StringBuilder sb = new StringBuilder(vArray[0]);
				return Float.parseFloat(sb.reverse().toString());
			}
		}
		Float tmpL = 0F;
		Float tmpG = 0F;
		Float sum = 0F;
		Float rValue = 0F;
		for (int i = 0; i < vArray.length; i++) {
			String v = vArray[i].trim();
			if (!isNormal) {
				StringBuilder sb = new StringBuilder(v);
				v = sb.reverse().toString();
			}
			if (!v.matches("(-)?+(\\d){3}(\\.)(\\d){2}")) {
				v = "0";
			}
			Float f = new Float(v);
			if (f > 0) {
				if (f > tmpL) {
					tmpL = f;
				}
				if (f < tmpG) {
					tmpG = f;
				}

				rv.add(f);
				sum += f;
			}
		}
		if (rv.size() < 1) {
			return 0F;
		}
		if (rv.size() > 3) {
			rv.remove(tmpL);
			rv.remove(tmpG);
			sum -= tmpL;
			sum -= tmpG;
		}
		rValue = sum / rv.size();
		int scale = 2;// 设置位数
		BigDecimal bd = new BigDecimal((double) rValue);
		// 四舍五入
		bd = bd.setScale(scale, BigDecimal.ROUND_HALF_DOWN);
		rValue = bd.floatValue();
		return rValue;
	}

	public void setRS232Value(float value) {
		 try {
		 getAppletContext().showDocument(new URL("javascript:setRS232Value(\""
		 + value + "\");"));
		 } catch (MalformedURLException e) {
		 e.printStackTrace();
		 }

//		JSObject win = JSObject.getWindow(this);
//		win.eval("setRS232Value('" + value + "');");
		// System.out.println(value);
		// JSObject document = (JSObject) win.getMember("rs232Input_old");
		//
		// System.out.println(document.getMember("value"));
		// JSObject form = (JSObject) document.getMember("rs232Input");
		// System.out.println(form);

		// win.call("setRS232Value", new Object[]{value});
	}

	@Override
	public void run() {
		try {
			// 等待1秒钟让串口把数据全部接收后在处理
			Thread.sleep(delayRead);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		if (this.serialPort != null) {
			this.serialPort.close();
		}
		System.out.println("RS232 Already Closed!");
		super.destroy();
	}

}
