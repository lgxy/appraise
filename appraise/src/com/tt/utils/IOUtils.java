package com.tt.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.http.util.ByteArrayBuffer;

import android.os.Environment;

public class IOUtils {
	/**
	 * 检查是否存在外部存储器
	 * 
	 * @return
	 */
	public static boolean isExternalStorageExist() {
		String status = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(status); 
	}

	public static String getRootDirectory() {
		return Environment.getRootDirectory().getAbsolutePath();
	}

	public static boolean rename(String fn, String newfn) {
		boolean b = true;
		try {
			b = new File(fn).renameTo(new File(newfn));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 获取外部存储器路径
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String getExternalStoragePath() throws FileNotFoundException {
		if (!isExternalStorageExist())
			throw new FileNotFoundException("E000001");
		String esp = Environment.getExternalStorageDirectory().getPath();
		if (esp == null)
			throw new FileNotFoundException("E000002");
		if (!esp.startsWith("/"))
			esp = "/" + esp;
		if (!esp.endsWith("/"))
			esp += "/";
		return esp;
	}

	public static boolean mkDir(String path) {
		File file = new File(path);
		if (file.exists() && file.isDirectory())
			return true;
		return file.mkdirs();
	}

	public static long getFileDate(String fn) {
		File file = new File(fn);
		if (!file.exists())
			return 0;
		long dt = file.lastModified();
		file = null;
		return dt;
	}

	public static long getFileSize(String fn) {
		File file = new File(fn);
		if (!file.exists())
			return 0;
		long len = file.length();
		file = null;
		return len;
	}

	public static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			is = null;
		}

		return sb.toString();

	}

	public static FileInputStream getFileInputStream(String fn) throws FileNotFoundException {
		FileInputStream is = null;
		is = new FileInputStream(fn);
		return is;
	}

	public static FileOutputStream getFileOutputStream(String fn) throws IOException {
		FileOutputStream os = null;
		File file = new File(fn);
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				file = null;
				throw e1;
			}
		os = new FileOutputStream(file);
		return os;
	}

	public static FileOutputStream getFileOutputStream(String fn, boolean append) throws IOException {
		FileOutputStream os = null;
		File file = new File(fn);
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				file = null;
				throw e1;
			}
		os = new FileOutputStream(file, append);
		return os;
	}

	public static byte[] getFile(String name) throws FileNotFoundException {
		InputStream is = getFileInputStream(name);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[512];
		try {
			while (true) {
				int ri = is.read(buffer, 0, buffer.length);
				if (ri <= 0)
					break;
				baos.write(buffer, 0, ri);
			}
			is.close();
			is = null;
			byte[] bs = baos.toByteArray();
			baos.close();
			baos = null;
			return bs;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e2) {
				} finally {
					is = null;
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (Exception e2) {
				} finally {
					baos = null;
				}
			}
		}
		return null;
	}

	public static boolean fileExist(String path) {
		File file = new File(path);
		return file.exists();
	}

	public static boolean isDirectory(String path) {
		File file = new File(path);
		return file.isDirectory();
	}

	public static void saveFile(String fullFileName, byte[] data) throws IOException {
		if (data == null)
			throw new IllegalArgumentException("data is null");
		if (fullFileName == null)
			throw new IllegalArgumentException("filename is null");
		File file = new File(fullFileName);
		FileOutputStream fos = null;

		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				file = null;
				throw e1;
			}

		try {
			fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
			fos.close();
			fos = null;
		} catch (IOException e) {
			throw e;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					fos = null;
				}
			}
		}

	}

	public static boolean saveFile(String fileFullPath, InputStream is) {
		File file = new File(fileFullPath);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		BufferedInputStream bis = new BufferedInputStream(is);
		ByteArrayBuffer baf = new ByteArrayBuffer(2048);
		int len = 0;
		try {
			while ((len = bis.read()) != -1) {
				baf.append((byte) len);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		try {

			fos.write(baf.toByteArray());
			fos.close();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	public static void delete(String path) {
		File file = new File(path);
		if (!file.exists())
			return;
		if (!file.delete())
			file.deleteOnExit();
	}
	
	public static void deleteDir(String dirPath){
		if(dirPath == null || dirPath.trim().length() == 0)
			return ;
		File file = new File(dirPath);
		File[] files = file.listFiles();
		if(files == null || files.length == 0)
			return ;
		for (File f : files) {
			if(f.isFile()){
				f.delete();
			}else if(f.isDirectory()){
				deleteDir(dirPath);
			}
		}
	}

	public static byte[] serialize(Object o) throws NotSerializableException, IOException {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			serialize(o, baos);
			byte bs[] = baos.toByteArray();
			return bs;
		} catch (NotSerializableException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (Exception e2) {
				} finally {
					baos = null;
				}
			}
		}
	}

	public static void serialize(Object o, String fn) throws NotSerializableException, FileNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream(fn);
		serialize(o, fos);
	}

	public static void serialize(Object o, OutputStream ostrm) throws NotSerializableException, IOException {
		if (ostrm == null)
			throw new IOException("output stream is null");
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(ostrm);
			os.writeObject(o);
			os.close();
		} catch (NotSerializableException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (Exception e2) {
				} finally {
					os = null;
				}
			}
		}
	}

	public static Object unSerialize(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		try {
			Object o = unSerialize(bais);
			return o;
		} catch (IOException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		} finally {
			if (bais != null)
				try {
					bais.close();
				} catch (Exception e1) {
				} finally {
					bais = null;
				}
		}
	}

	public static Object unSerialize(InputStream istrm) throws IOException, ClassNotFoundException {
		ObjectInputStream is = null;
		Object o;
		try {
			is = new ObjectInputStream(istrm);
			o = is.readObject();
			is.close();
			is = null;
			return o;
		} catch (IOException e1) {
			throw e1;
		} catch (ClassNotFoundException e2) {
			throw e2;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				} finally {
					is = null;
				}
			}
			if (istrm != null) {
				try {
					istrm.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				} finally {
					istrm = null;
				}
			}
		}
	}

	/** 获得问件名后缀 */
	public static String getFileSuffix(String fn) {
		if (fn == null || fn.trim().length() == 0)
			return null;
		int index = fn.lastIndexOf(".");
		if (index < 0)
			return null;
		return fn.substring(index);
	}

	/**
	 * 单个文件拷贝
	 * 
	 * @parameter targetFilePath 目标路径
	 * @parameter sourceFilePath 源路径
	 * @return
	 */
	public static boolean copyFile(String targetFilePath, String sourceFilePath) {
		if (targetFilePath == null || targetFilePath.trim().length() == 0)
			return false;
		if (sourceFilePath == null || sourceFilePath.trim().length() == 0)
			return false;
		File sourceFile = new File(sourceFilePath);
		if (!sourceFile.exists())
			return false;

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(sourceFilePath));
			bos = new BufferedOutputStream(new FileOutputStream(targetFilePath));
			byte[] b = new byte[1024];
			int n = 0;
			while ((n = bis.read(b, 0, b.length)) != -1) {
				bos.write(b, 0, n);
			}
			bos.flush();
			bos.close();
			bos = null;
			bis.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
}
