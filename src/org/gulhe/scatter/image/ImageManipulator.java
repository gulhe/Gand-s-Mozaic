package org.gulhe.scatter.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.gulhe.scatter.properties.PropertyHelper;
import org.gulhe.scatter.properties.SelfAwareProperty;

public class ImageManipulator {
	static void makeLogoRed() {
		String source = "/logoforum.JPG";
		String fileType = "jpg";
		String result = "red.jpg";
		makeImageRed(source, fileType, result);
	}

	static void resizeImageFromFolder(File[] listFiles, String destFolder) {
		for (int index = 0; index < listFiles.length; index++) {
			File file = listFiles[index];

			try {

				BufferedImage read = ImageIO.read(file);
				SelfAwareProperty properties = PropertyHelper
						.getImageProperties(file);
				int height = Integer.valueOf(properties
						.getProperty(PropertyHelper.HEIGHT));
				int width = Integer.valueOf(properties
						.getProperty(PropertyHelper.WIDTH));
				float fHeight = height;
				float fWidth = width;
				float ratio = fWidth / fHeight;
				float etalon = 214f / 317f;
				int shoudAddW = 0;
				int shoudAddH = 0;
				if (ratio < etalon) {
					shoudAddW = Math.round(height * etalon) - width;
				}
				if (ratio > etalon) {
					shoudAddH = Math.round(width / etalon) - height;
				}
				if (ratio != etalon) {
					System.out.println(shoudAddW + " : " + shoudAddH);
				}
				int totalW = width + shoudAddW;
				int shouldAddLeft = shoudAddW / 2;
				int totalH = height + shoudAddH;
				int shouldAddTop = shoudAddH / 2;
				BufferedImage write = new BufferedImage(totalW, totalH,
						BufferedImage.TYPE_INT_RGB);

				if (shoudAddW != 0) {
					for (int j = 0; j < height; j++) {
						for (int i = 0; i < shouldAddLeft; i++) {
							write.setRGB(i, j, read.getRGB(0, j));
						}
						for (int i = width; i < totalW; i++) {
							write.setRGB(i, j, read.getRGB(width - 1, j));
						}
					}
				}
				if (shoudAddH != 0) {
					for (int i = 0; i < width; i++) {
						for (int j = 0; j < shouldAddTop; j++) {
							write.setRGB(i, j, read.getRGB(i, 0));
						}
						for (int j = height; j < totalH; j++) {
							write.setRGB(i, j, read.getRGB(i, height - 1));
						}
					}
				}

				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						int rgb = read.getRGB(i, j);
						write.setRGB(i + shouldAddLeft, j + shouldAddTop, rgb);
					}
				}
				File destFile = new File(destFolder + "/" + file.getName());
				ImageIO.write(write, "jpg", destFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static InputStream getResourceAsStream(String source) {
		return ImageManipulator.class.getResourceAsStream(source);
	}
	static void makeImageRed(String source, String fileType, String result) {
		InputStream resourceAsStream = getResourceAsStream(source);

		BufferedImage read = null;
		// BufferedImage redWrite = null;
		try {
			read = ImageIO.read(resourceAsStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < read.getWidth(); i++) {
			for (int j = 0; j < read.getHeight(); j++) {
				int rgb = read.getRGB(i, j);
				Color color = new Color(rgb);
				System.out.println(color.getRed());
				Color color2 = new Color(color.getRed(), 0, 0);
				read.setRGB(i, j, color2.getRGB());
			}
		}
		try {
			ImageIO.write(read, fileType, new File(result));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
