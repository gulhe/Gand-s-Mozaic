package org.gulhe.scatter.writer;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.gulhe.scatter.Writer;

public class ImageWriterImpl implements Writer {

	private BufferedImage imageWriter;
	private String resultFileName;
	private int tileHeight;
	private int tileWidth;
	private int scale=20;
	
	private int yOffset(int yCurrentPosition) {return yCurrentPosition*tileHeight*scale;}
	private int xOffset(int xCurrentPosition) {return xCurrentPosition*tileWidth*scale;}
	

	public ImageWriterImpl(int colAmount, int rowAmount, int tileWidth, int tileHeight,
			String resultFileName) {
		this.tileHeight = tileHeight;
		this.tileWidth = tileWidth;
		this.resultFileName = resultFileName;
		imageWriter = new BufferedImage((colAmount+2)*tileWidth*scale, (rowAmount+2)*tileHeight*scale, BufferedImage.TYPE_INT_RGB);
	}

	@Override
	public Writer newLine() {
		return this;
	}

	@Override
	public Writer closeLine() {
		return this;
	}

	@Override
	public Writer newColumn() {
		return this;
	}

	@Override
	public Writer closeColumn() {
		return this;
	}

	@Override
	public Writer writeTile(String image, int x, int y) {
		BufferedImage resized = getProperlySizedImage(image);

		for (int i = 0; i < resized.getWidth(); i++) {
			for (int j = 0; j < resized.getHeight(); j++) {
//				System.out.println("Horrible debug : ["+imageWriter.getWidth()+"]["+imageWriter.getHeight()+"] should be greater than ["+(i+xOffset())+"]["+(j+yOffset())+"]");
				imageWriter.setRGB(i+xOffset(x+1), j+yOffset(y+1), resized.getRGB(i, j));
			}
		}
		return this;
	}
	@Override
	public void close() {
		try {
			ImageIO.write(imageWriter, "jpg", new File(resultFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BufferedImage getProperlySizedImage(String image) {
		File imgFile = new File("images/"+image);

		InputStream resourceAsStream = null;
		try {
			resourceAsStream = new FileInputStream(imgFile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} 
		
		BufferedImage read = null;
		try {
			read = ImageIO.read(resourceAsStream);
		} catch (Exception e) {
			(new Exception("can't seem to read ["+image+"]",e)).printStackTrace();
		}

		return resize(read, tileWidth*scale, tileHeight*scale);
	}

	private BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}
}
