package org.gulhe.scatter.pompage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import org.gulhe.scatter.NoWebsiteException;
import org.jsoup.select.Elements;

public class Pompeur {

	static void hardCorePompage() {
		String host = "http://www.imdb.com";
		String user = "/user/ur23361580";
		String baseUrl = host + user
				+ "/watchlist?sort=list_order,asc&mode=grid&page=";
		int pageAmount = 17;
		String errorLogFolder = "losMerdouilles";
		String downloadFolder = "images";

		ArrayList<String> allHrefs = PageParser.getUrlsOfAllMoviePages(baseUrl,
				pageAmount);
		int imageAmount = allHrefs.size();
		Long lImageAmount = new Long(imageAmount);
		double log10 = Math.log10(lImageAmount);
		long log10ofImageAmount = Math.round(Math.ceil(log10)) + 1;
		for (int i = 0; i < imageAmount; i++) {
			String href = allHrefs.get(i);

			Elements image = null;
			try {
				image = PageParser.getImageNode(href);
			} catch (SocketTimeoutException e) {
				String pageInError = errorLogFolder + "/merdouille-"
						+ completeNumber(i, log10ofImageAmount)
						+ "-Paltemps.html";
				downloadFileSafe(pageInError, href);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (image != null) {
				String imageUrl = image.attr("src");
				String imageTitle = cleanTitle(image.attr("title"));
				try {
					String imageName = downloadFolder + "/image-"
							+ completeNumber(i, log10ofImageAmount) + "-"
							+ imageTitle + ".jpg";
					downloadFile(imageName, imageUrl);
				} catch (NoWebsiteException e) {
					System.err
							.println("Hu ho ! on dirait une merdouille vide ?!");
					downloadFileSafe(errorLogFolder + "/merdouille-"
							+ completeNumber(i, log10ofImageAmount)
							+ "-SansTitre.html", href);
				}
			}
		}
	}

	private static String completeNumber(int i, long log10ofImageAmount) {
		String ret = String.valueOf(i);
		while (ret.length() < log10ofImageAmount) {
			ret = "0" + ret;
		}
		return ret;
	}

	static void downloadFileSafe(String fileName, String website) {
		try {
			downloadFile(fileName, website);
		} catch (NoWebsiteException e) {
			e.printStackTrace();
		}
	}

	static void downloadFile(String fileName, String website)
			throws NoWebsiteException {
		if (website == null || website.isEmpty()) {
			throw new NoWebsiteException("There is no website specified");
		}
		try {
			System.out.println("Downloading File From: " + website
					+ "\n\t as > " + fileName);
			InputStream inputStream = openUrlAsInputStream(website);
			OutputStream outputStream = new FileOutputStream(fileName);
			byte[] buffer = new byte[2048];
			int length = 0;
			while ((length = inputStream.read(buffer)) != -1) {
				// System.out.println("Buffer Read of length: " + length);
				outputStream.write(buffer, 0, length);
			}
			inputStream.close();
			outputStream.close();
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
	}

	private static String cleanTitle(String imageTitle) {
		return imageTitle.replaceAll(" ", "-").replaceAll("\\?", "")
				.replaceAll("!", "").replaceAll("---", "-")
				.replaceAll("\\.", "_").replaceAll("/", "SLASH")
				.replaceAll("/", "ANTISLASH").replaceAll(":", "")
				.replaceAll(";", "");
	}

	private static InputStream openUrlAsInputStream(String website)
			throws MalformedURLException, IOException {
		URL url = new URL(website);
		InputStream inputStream = url.openStream();
		return inputStream;
	}

	@Deprecated
	static void downloadImageTutorialspoint() {
		String fileName = "digital_image_processing.jpg";
		String website = "http://tutorialspoint.com/java_dip/images/"
				+ fileName;
		try {
			downloadFile(fileName, website);
		} catch (NoWebsiteException e) {
			System.err.println("Don't mess with pre written code");
			e.printStackTrace();
		}
	}
}
