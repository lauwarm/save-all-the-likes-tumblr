package io.zzzz.owo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DownloadFile {
	
	private static final String substringUrl = "/tumblr";
	
	public void downloadFile(String photoURL, String filePath) throws IOException {
		try {
			URL url = new URL (photoURL);
			String path = filePath + photoURL.substring(photoURL.indexOf(substringUrl), photoURL.length());
			InputStream in = url.openStream();
			FileOutputStream fos = new FileOutputStream(new File(path));
			int length = -1;
			byte[] buffer = new byte[1024];
			while ((length = in.read(buffer)) > -1) {
				fos.write(buffer, 0, length);
			}
			fos.close();
			in.close();
			System.out.println("file downloaded");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
