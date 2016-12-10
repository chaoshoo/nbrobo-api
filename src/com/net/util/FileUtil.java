package com.net.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件目录管理公共类
 * 
 * @date 2011-5-23
 * @author xup
 * 
 */
public class FileUtil {
	private static final Logger Log = LoggerFactory.getLogger(FileUtil.class);

	private static File file;

	/**
	 * 判断文件或目录是否存在
	 * 
	 * @param dirName
	 *            文件名
	 * @return 存在true \不存在false
	 * @version CETCMS 2011-6-13
	 * @author yuanc
	 */
	public static Boolean isDir(String dirName) {
		file = new File(dirName);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 创建目录-不存在就创建
	 * 
	 * @param dirName
	 *            目录文件名
	 * @author xup
	 * @date 2011-5-23
	 */
	public static void createDirectory(String dirName) {
		file = new File(dirName);
		try {
			if (!FileUtil.isDir(dirName))
				file.mkdir();

		} catch (Exception e) {
			Log.error("[文件操作]  创建目录错误:目录名：" + dirName + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 删除目录
	 * 
	 * @param dirName
	 *            目录文件名
	 * @version CETCMS 2011-6-13
	 * @author yuanc
	 */
	public static void deleteDirectory(String dirName) {
		File srcFile = new File(dirName);
		// 如果是文件，或者该文件夹下是空的就直接删除
		try {
			// 判断是否文件，是否空的文件夹
			if (srcFile.isFile() || srcFile.list().length == 0) {
				// 是文件或者空文件夹则直接删除
				srcFile.delete();

			} else { // 如果不是文件，并且文件夹下也不是空的

				// 获取文件夹下面所有的文件及文件夹
				File[] files = srcFile.listFiles();
				// 遍历文件夹
				for (File newFile : files) {
					// 递归调用进行是否文件与文件夹的判断,进行相应的删除
					deleteDirectory(dirName);
					// 直到当前文件夹为空，就删除当前的文件夹
					newFile.delete();
				}
			}
		} catch (Exception ex) {
			Log.error("[文件操作]  删除目录错误:目录名" + dirName + ex.getMessage());
		}
	}

	/**
	 * 覆盖方式写
	 * 
	 * @param fileName
	 *            文件名
	 * @param context
	 *            内容
	 * @author xup
	 * @date 2011-5-23
	 */
	public static void writeFileForOver(String fileName, String context) {
		OutputStream os;
		try {
			os = new FileOutputStream(fileName);
			// 套上字符缓冲流
			BufferedOutputStream bout = new BufferedOutputStream(os);

			byte[] buff = context.getBytes();

			bout.write(buff);
			bout.flush();
			bout.close();

		} catch (FileNotFoundException e) {
			Log.error("[文件操作]  文件没找到:文件名" + fileName + e.getMessage());
		} catch (IOException e) {
			Log.error("[文件操作]  文件覆盖写入出错:文件名" + fileName + e.getMessage());
		}
	}

	/**
	 * 追加方式写
	 * 
	 * @param fileName
	 *            文件名
	 * @param context
	 *            内容
	 * @author xup
	 * @date 2011-5-23
	 */
	public static void writerFileForAppend(String fileName, String context) {
		OutputStream os;
		try {
			os = new FileOutputStream(fileName, true);
			// 创建字符缓冲流
			BufferedOutputStream bout = new BufferedOutputStream(os);

			byte[] buff = context.getBytes();

			bout.write(buff);
			bout.flush();
			bout.close();

		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			Log.error("[文件操作]  文件没找到:文件名" + fileName + e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			Log.error("[文件操作]  文件追加写入出错:文件名" + fileName + e.getMessage());
		}
	}

	/**
	 * 读取文件内容
	 * 
	 * @param fileName
	 *            文件名
	 * @return String 文件内容
	 * @author xup
	 * @throws IOException
	 * @date 2011-5-23
	 */
	public static String readFile(String fileName) throws IOException {
		StringBuilder stb = new StringBuilder();
		InputStream is = new FileInputStream(fileName);
		// 定义一个字节类型数组，所有的文件都是字节形式存在的
		byte[] buff = new byte[200];// 每次从文件中读取200个字节
		// 每次读取的实际字节长度
		int length = 0;

		// is.read()方法：从buff缓中区的第0个位开始读取字节，每次最多读取200，
		// 方法会返回一个实际读取的长度，用length接收，当值为-1时，表示所有的字节全部读取完毕
		while ((length = is.read(buff, 0, 200)) != -1) {
			// 把字节以平台的默认编码方式转为字符，从buff的第0个位开始转换，实际要转换的长度是length
			stb.append(new String(buff, 0, length,"UTF-8"));
		}
		// 关闭流
		is.close();
		return stb.toString();

	}

	/**
	 * 获取当前目录下所有的文件名
	 * 
	 * @param dirName
	 *            路径名
	 * @param postfix
	 *            文件后缀名
	 * @return 目录下所有文件名String[]
	 * @author xup
	 * @date 2011-5-24
	 */
	public static String[] getCurrFile(String dirName, String postfix) {

		File dirFile = new File(dirName);
		String[] fileNames = dirFile.list();
		List<String> list = new ArrayList<String>();
		// 文件过滤，只需要txt文件,防止非指令文件读取
		for (String name : fileNames) {
			// 文件名过滤
			if (name.endsWith(postfix)) {
				// System.out.println(name); // 只打印该目录下的.xml文件
				list.add(dirName + "/" + name);
			}
		}

		if (fileNames.length == 0)
			return null;
		// 把泛型转成字符数组
		String[] result = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	/**
	 * 文件备份 移动 先把文件复制到指定的路径文件名， 复制成功之后再把源文件删除,删除成功时才返回为真
	 * 
	 * @param inFile
	 *            源文件
	 * @param outFile
	 *            目标文件
	 * @author xup
	 * @return 返回备份结果信息
	 * @date 2011-6-13
	 */
	public static Boolean moveFile(String inFile, String outFile) {
		Boolean result = false;
		Boolean falg = FileUtil.copyFile(inFile, outFile);
		// 当文件复制成功时才执行删除
		if (falg) {
			FileUtil.delFile(inFile);
			result = true;
		}

		return result;
	}

	/**
	 * 删除文件--删除单个文件
	 * 
	 * @param fileName
	 *            文件名
	 * @author xup
	 * @date 2011-6-13
	 */
	public static void delFile(String fileName) {
		File delFile = new File(fileName);
		delFile.delete();
	}

	/**
	 * 复制文件
	 * 
	 * @param inFile
	 *            源文件
	 * @param outFile
	 *            目标文件
	 * @return
	 * @throws Exception
	 */
	public static Boolean copyFile(String inFile, String outFile) {
		Boolean falg = true;
		// 源文件装载
		File in = new File(inFile);
		try {
			// 输出目标文件装载
			File out = new File(outFile);
			// 套上输入文件流--源文件流
			FileInputStream fin = new FileInputStream(in);
			// 套上输出文件流---目标流
			FileOutputStream fout = new FileOutputStream(out);
			// 一次性读取的最大字节数
			int length = 2097152;// 2m内存
			byte[] buffer = new byte[length];
			while (true) {
				// 读入内存
				int ins = fin.read(buffer);
				// 值为-1表示读到文件尾
				if (ins == -1) {
					fin.close();
					fout.flush();
					fout.close();
					break;

				} else
					// 把buffer中的字节内容写入（输出）到目标文件
					fout.write(buffer, 0, ins);
			}
		} catch (Exception e) {
			Log.error("[文件操作]文件复制出错" + e.getMessage());
			falg = false;
		}
		return falg;
	}

	/**
	 * 
	 * 得到发布根目录
	 * 
	 * @return
	 * @author yuanchong Company huilet 2013-1-11
	 */
	public static String getRootPath() {

		// file:/D:/ResumeParser/WebRoot/WEB-INF/classes/util/Application.class

		String result = FileUtil.class.getResource("FileUtil.class").toString();

		int index = result.indexOf("WEB-INF");

		if (index == -1) {

			index = result.indexOf("bin");

		}
		result = result.substring(0, index);
		if (result.startsWith("jar")) {

			// 当class文件在jar文件中时，返回"jar:file:/F:/ ..."样的路径

			result = result.substring(10);

		} else if (result.startsWith("file")) {

			// 当class文件在class文件中时，返回"file:/F:/ ..."样的路径

			result = result.substring(6);

		}

		if (result.endsWith("/")){

			result = result.substring(0, result.length() - 1);// 不包含最后的"/"
		}
		Properties props=System.getProperties(); //获得系统属性集    
		String osName = props.getProperty("os.name"); //操作系统名称    
//		System.out.println("os------>"+osName);
		if("Linux".equals(osName)){
			result = "/" +result;
		}
		return result;
	}
	
	public static  void main(String atgs[]){
		Properties props=System.getProperties(); //获得
	     System.out.println("文件分隔符："+file.separator);  
		
	}

}
