package com.bupticet.paperadmin.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFile {



	/**
	 * Creates a Zip archive. If the filename passed in is a directory,
	 * 
	 * 
	 * the directory's contents will be made into a Zip file
	 * 
	 * 
	 */

	public static void makeZip(String fileName) throws IOException,
			FileNotFoundException

	{

		File file = new File(fileName);

		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file
				+ ".zip"));

		int len = fileName.length();

		if ((fileName.lastIndexOf('\\') > 0)
				&& (fileName.lastIndexOf('\\') != len - 1))

			len = len + 1;

		else if ((fileName.lastIndexOf('/') > 0)
				&& (fileName.lastIndexOf('/') != len - 1))

			len = len + 1;

		recurseFiles(file, zos, len);

		zos.close();

	}

	/**
	 * Recurses down a directory and its subdirectories to
	 * 
	 * 
	 * look for files to add to the Zip.
	 * 
	 * 
	 * If the current file being looked at is not a directory,
	 * 
	 * 
	 * the method adds it to the Zip file.
	 * 
	 * 
	 */

	private static void recurseFiles(File file, ZipOutputStream zos,
			int nLenToDel)

	throws IOException, FileNotFoundException

	{

		if (file.isDirectory())

		{

			String[] fileNames = file.list();

			if (fileNames != null)

				for (int i = 0; i < fileNames.length; i++)

					recurseFiles(new File(file, fileNames[i]), zos, nLenToDel);

			// file, fileNames[i] for full path

		} else

		{

			FileInputStream fin = new FileInputStream(file);

			BufferedInputStream in = new BufferedInputStream(fin);

			byte[] buf = new byte[1024];

			int len;

			StringBuffer strBuf = new StringBuffer(file.toString());

			String strEntry = strBuf.delete(0, nLenToDel).toString();

			// System.out.println(strEntry);

			ZipEntry zipEntry = new ZipEntry(strEntry);

			zos.putNextEntry(zipEntry);

			while ((len = in.read(buf)) >= 0)

			{

				zos.write(buf, 0, len);

			}

			in.close();

			zos.closeEntry();

		}

	}

}