package cn.net.sikai.cc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipLoader implements MeleteLoader{

	private File root;
	private String rootPath;
	private File cc;
	private boolean unzipped;
	private InputStream cc_inputStream = null;
	private final int BUFFER = 4096;

	private ZipLoader(File the_cc, File dir) throws IOException {
		root = dir;
		cc = the_cc;
		unzipped = false;
		rootPath = root.getCanonicalPath();
	}

	ZipLoader(File dir) throws IOException {
		root = dir;
		// this is for site archive.
		// the stream resets on close. We get it at EOF, so need to reset it
		unzipped = true;
		rootPath = root.getCanonicalPath();
	}

	private void unzip() throws FileNotFoundException, IOException {
		if (!unzipped) {
			BufferedOutputStream dest = null;
			InputStream fis = null;
			ZipInputStream zis = null;
			try {
				if (cc_inputStream != null)
					fis = cc_inputStream;
				else
					fis = new FileInputStream(cc);
				System.out.println("unzip fis " + fis);
				zis = new ZipInputStream(new BufferedInputStream(fis));
				ZipEntry entry;
				while ((entry = zis.getNextEntry()) != null) {
					System.out.println("zip name " + entry.getName());
					File target = new File(root, entry.getName());
					// not sure if you can put things like .. into a zip file,
					// but be careful
					if (!target.getCanonicalPath().startsWith(rootPath))
						throw new FileNotFoundException(target.getCanonicalPath());
					if (entry.isDirectory()) {
						if (!target.mkdirs())
							throw new IOException("Unable to make temporary directory");
					} else {
						if (target.getParentFile().exists() == false) {
							target.getParentFile().mkdirs();
						}
						int count;
						byte data[] = new byte[BUFFER];
						FileOutputStream fos = new FileOutputStream(target);
						dest = new BufferedOutputStream(fos, BUFFER);
						while ((count = zis.read(data, 0, BUFFER)) != -1) {
							dest.write(data, 0, count);
						}
						dest.flush();
						dest.close();
						dest = null;
						System.out.println("wrote file " + target);
					}
				}
			} catch (Exception x) {
				System.out.println("exception " + x);
			} finally {
				if (zis != null) {
					try {
						zis.close();
					} catch (Exception ignore) {
					}
				}
				if (fis != null) {
					try {
						fis.close();
					} catch (Exception ignore) {
					}
				}
				if (dest != null) {
					try {
						dest.close();
					} catch (Exception ignore) {
					}
				}
			}
			unzipped = true;
		}
	}

 
	public InputStream getFile(String the_target) throws FileNotFoundException, IOException {
		unzip();
		// System.out.println("getfile " + root + "::" + the_target + "::" +
		// (new File(root, the_target)).getCanonicalPath());
		File f = new File(root, the_target);
		// check for people using .. or other tricks
		if (!f.getCanonicalPath().startsWith(rootPath))
			throw new FileNotFoundException(f.getCanonicalPath());
		return new FileInputStream(new File(root, the_target));
	}

	public static MeleteLoader getUtilities(File the_cc, String unzip_dir)
			throws FileNotFoundException, IOException {
		File unzip = new File(unzip_dir, the_cc.getName());
		if (!unzip.exists()) {
			if (!unzip.mkdir())
				throw new IOException("unable to make temporary directory");
		}
		return new ZipLoader(the_cc, unzip);
	}

	// for site archive, where the file is already unzipped
	public static MeleteLoader getUtilities(String unzip_dir) throws FileNotFoundException, IOException {
		File unzip = new File(unzip_dir);
		if (!unzip.exists()) {
			throw new IOException("unzipped directory doesn't exist");
		}
		return new ZipLoader(unzip);
	}

	public static MeleteLoader getUtilities(File the_cc) throws FileNotFoundException, IOException {
		return getUtilities(the_cc, System.getProperty("java.io.tmpdir"));
	}

}
