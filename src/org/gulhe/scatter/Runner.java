package org.gulhe.scatter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import javax.imageio.ImageIO;

import org.gulhe.scatter.analysis.BroadRGBSearchStructure;
import org.gulhe.scatter.analysis.Point;
import org.gulhe.scatter.properties.PropertyHelper;
import org.gulhe.scatter.properties.SelfAwareProperty;
import org.gulhe.scatter.writer.ImageWriterImpl;

/**
 * 
 * DEGEULASSUM EST !
 * 
 */
public class Runner {

	static final String malignantMerde;
	static {
		byte[] badBytes = new byte[3];
		badBytes[0] = -17;
		badBytes[1] = -65;
		badBytes[2] = -67;
		malignantMerde = new String(badBytes);
	}

	public static void main(String[] args) {

		File folder = new File("images");

		File[] listFiles = folder.listFiles();

		BroadRGBSearchStructure pixelCloud = new BroadRGBSearchStructure();
		Collection<Point> pixelCrowd = new HashSet<>();
		for (int index = 0; index < listFiles.length; index++) {
			File file = listFiles[index];
			String fileName = file.getAbsolutePath();
			if (fileName.endsWith("_resize.properties")
					&& !fileNameContaminatedInWindows(fileName)) {
				try {
					SelfAwareProperty prop = PropertyHelper
							.getPropertiesByFilename(fileName);
					Point currentPoint = new Point(prop);
					pixelCloud.add(currentPoint);
					pixelCrowd.add(currentPoint);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}

		BufferedImage read = null;
		try {
			read = ImageIO.read(Runner.class.getResource("/goal.jpg"));
			int notRepresentable = 0;
			int width = read.getWidth();
			int height = read.getHeight();
			int widthDelta = 2;
			int heightDelta = 3;
			int topMax = 0;
			int tileColumnAmount = (width / widthDelta) + 1;
			int tileRowAmount = (height / heightDelta) + 1;
			Writer writer = new ImageWriterImpl(tileColumnAmount,
					tileRowAmount, widthDelta, heightDelta, "testPage2.jpg");
			for (int i = 0; i < tileColumnAmount; i++) {
				writer.newColumn();
				for (int j = 0; j < tileRowAmount; j++) {
					writer.newLine();
					final Color color = computeAverageColor(read, i
							* widthDelta, (i + 1) * widthDelta,
							j * heightDelta, (j + 1) * heightDelta);
					Collection<Point> yield = pixelCloud.getClosest(color, 200);
					int size = yield.size();
					for (Point point : yield) {
						point.incrementCount();
					}
					if (size == 0) {
						notRepresentable++;
						int superficie = i * height + j + 1;
						log("gotOne total : " + notRepresentable + " on ["
								+ superficie + "] so approximately ["
								+ ((notRepresentable * 100) / superficie)
								+ "%] missing");
					} else {
						topMax = Math.max(topMax, size);
						log(size + " / " + topMax);
						final Point currentPoint = new Point(color.getRed(),
								color.getGreen(), color.getBlue());
						ArrayList<Point> arrayList = new ArrayList<>(yield);
						if (size > 1) {
							Collections.sort(arrayList,
									new Comparator<Point>() {
										@Override
										public int compare(Point o1, Point o2) {
											Double d1 = currentPoint
													.distance(o1);
											Double d2 = currentPoint
													.distance(o2);
											return d1.compareTo(d2);
										}
									});
						}

						Point bestOption = arrayList.get(0);
						String propName = bestOption.getProps().getName();
						// System.out.println(propName);
						File file = new File(propName);
						String jpgName = file.getName().replaceAll(
								".properties", ".jpg");
						writer.writeTile(jpgName,i,j);
					}
					System.out.println("i : [" + i + "/" + tileColumnAmount
							+ "], j : [" + j + "/" + tileRowAmount + "]");
					writer.closeColumn();
				}
				writer.closeLine();
			}
			writer.close();
			int superficie = tileColumnAmount * tileRowAmount;
			log("we got [" + notRepresentable
					+ "] tiles that can't be represented on a total ["
					+ superficie + "] so approximately ["
					+ ((notRepresentable * 100) / superficie) + "%] missing");
		} catch (IOException e) {
			e.printStackTrace();
		}

		int useless = 0;
		int topMax = 0;
		for (Point point : pixelCrowd) {
			if (point.getCount() == 0) {
				useless++;
			} else {
				topMax = Math.max(topMax, point.getCount());
				// log(point.getCount());
			}
		}
		log("we got [" + useless + "] on [" + pixelCrowd.size()
				+ "] so approx of the herd ["
				+ ((useless * 100) / pixelCrowd.size()) + "]");
		log("topUse : " + topMax);

	}

	private static boolean fileNameContaminatedInWindows(String fileName) {
		return fileName.contains(malignantMerde);
	}

	static void setAverageColorPropertiesForAFile(File file) {
		try {
			BufferedImage read = ImageIO.read(file);
			SelfAwareProperty properties = PropertyHelper
					.getImageProperties(file);
			Color averageColor = computeAverageColor(read);
			int averageRed = averageColor.getRed();
			int averageGreen = averageColor.getGreen();
			int averageBlue = averageColor.getBlue();
			properties.setProperty(PropertyHelper.AVERAGE_RED,
					String.valueOf(averageRed));
			properties.setProperty(PropertyHelper.AVERAGE_GREEN,
					String.valueOf(averageGreen));
			properties.setProperty(PropertyHelper.AVERAGE_BLUE,
					String.valueOf(averageBlue));
			PropertyHelper.setProperties(file, properties);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Color computeAverageColor(BufferedImage read) {
		int height = read.getHeight();
		int width = read.getWidth();
		return computeAverageColor(read, width, height);
	}

	private static Color computeAverageColor(BufferedImage read, int right,
			int bottom) {
		return computeAverageColor(read, 0, right, 0, bottom);
	}

	private static Color computeAverageColor(BufferedImage read, int left,
			int right, int top, int bottom) {
		int totalRed = 0;
		int totalGreen = 0;
		int totalBlue = 0;
		for (int i = left; (i < right) && (i < read.getWidth()); i++) {
			for (int j = top; (j < bottom) && (j < read.getHeight()); j++) {
				int rgb = read.getRGB(i, j);
				Color color = new Color(rgb);
				totalRed += color.getRed();
				totalGreen += color.getGreen();
				totalBlue += color.getBlue();
			}
		}
		int amountOfPixels = (bottom - top) * (right - left);
		Color averageColor = new Color(totalRed / amountOfPixels, totalGreen
				/ amountOfPixels, totalBlue / amountOfPixels);
		return averageColor;
	}

	static void log(String msg) {
		// System.out.println(msg);
	}

	static void log(Integer message) {
		log(String.valueOf(message));
	}
}
