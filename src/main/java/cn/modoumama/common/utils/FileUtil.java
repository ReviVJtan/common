package cn.modoumama.common.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件操作公共方法
 * 
 * @ClassName: FileUtil
 */
public class FileUtil {
	public static final Long M = 1024 * 1024L;

	/**
	 * 保存压缩文件
	 * 
	 * @param file
	 * @return
	 */
	public static String saveCompressFile(MultipartFile file) {
		final long maxSize = 300 * M;// 设置上传文件最大值
		// 允许上传的文件格式的列表
		final String[] allowedExt = new String[] { ".rar", ".zip", ".tar", ".gz", ".7z", ".bz2", ".cab", ".iso" };
		return saveFile(file, maxSize, allowedExt);
	}

	/**
	 * 保存文档文件
	 * 
	 * @param file
	 * @return
	 */
	public static String saveDocFile(MultipartFile file) {
		final long maxSize = 300 * M;// 设置上传文件最大值
		// 允许上传的文件格式的列表
		final String[] allowedExt = new String[] { ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".pdf", ".txt",
				".md", ".xml" };
		return saveFile(file, maxSize, allowedExt);
	}

	/**
	 * 保存视频文件
	 * 
	 * @param file
	 * @return
	 */
	public static String saveVideoFile(MultipartFile file) {
		final long maxSize = 100 * M;// 设置上传文件最大值
		// 允许上传的文件格式的列表
		final String[] allowedExt = new String[] { ".flv", ".swf", ".mkv", ".avi", ".rm", ".rmvb", ".mpeg", ".mpg",
				".ogg", ".ogv", ".mov", ".wmv", ".mp4", ".webm", ".mp3", ".wav", ".mid" };
		return saveFile(file, maxSize, allowedExt);
	}

	/**
	 * 保存图片
	 * 
	 * @param file
	 * @return
	 */
	public static String saveImgageFile(MultipartFile file) {
		final long maxSize = 30 * M;// 设置上传文件最大值
		// 允许上传的文件格式的列表
		final String[] allowedExt = new String[] { "jpg", "jpeg", "gif", "png", "swf", "bmp" };
		return saveFile(file, maxSize, allowedExt);
	}

	public static String saveFile(MultipartFile file) {
		return saveFile(file, null, null);
	}

	/**
	 * 保存文件到本地
	 * 
	 * @param file 文件
	 * @param path 路径本地
	 * @param url 网络路径
	 * @return
	 */
	public static String saveFile(MultipartFile file, final Long maxSize, final String[] allowedExt) {
		String hUrl = null;
		if (file != null) {
			String fileName = file.getOriginalFilename();

			if (maxSize != null && maxSize > 0) {
				if (file.getSize() > maxSize) {
					return hUrl;
				}
			}

			// 获取后缀
			String fileEx;
			try {
				fileEx = StringUtils.getFileSuffix(fileName);
			} catch (Exception e1) {
				return hUrl;
			}

			if (allowedExt != null && allowedExt.length > 0) {
				if (!(ArrayUtil.isContain(fileEx.substring(1), allowedExt))) {
					return hUrl;
				}
			}

			fileName = UUID.randomUUID().toString().replace("-", "") + fileEx;
			File targetFile = new File(FileSavePathUtils.FILE_SAVE_PATH, fileName);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			// 保存
			try {
				file.transferTo(targetFile);
				hUrl = FileSavePathUtils.FILE_URL_PATH + fileName;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return hUrl;
	}

	/**
	 * 获得指定文件的byte数组
	 */
	public static byte[] getBytes(String filePath) {
		File file = new File(filePath);
		return getBytes(file);
	}

	/**
	 * 获得指定文件的byte数组
	 */
	public static byte[] getBytes(File file) {
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * 根据byte数组，生成文件
	 */
	public static File getFile(byte[] bfile, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
				dir.mkdirs();
			}
			file = new File(filePath + "\\" + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}
}
