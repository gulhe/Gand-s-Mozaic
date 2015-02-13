package org.gulhe.scatter.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;



public class PropertyHelper {
	
	public static final String HEIGHT = "height";
	public static final String WIDTH = "width";
	public static final String AVERAGE_RED = "averageRed";
	public static final String AVERAGE_GREEN = "averageGreen";
	public static final String AVERAGE_BLUE = "averageBlue";
	public static final String TECH_I_AM = "tech_i.am";

	
	public static void setProperties(File file, Properties properties)
			throws IOException {
		String name = file.toString().replaceAll(".jpg", ".properties");
		OutputStream oPS = new FileOutputStream(name);
		properties.store(oPS, "Comments ?");
	}

	public static SelfAwareProperty getImageProperties(File image)
			throws IOException {
		String proFileName = image.toString().replaceAll(".jpg", ".properties");
		return getPropertiesByFilename(proFileName);

	}

	public static SelfAwareProperty getPropertiesByFilename(String proFileName)
			throws IOException {
		SelfAwareProperty ret = new SelfAwareProperty(proFileName);
		try {
			InputStream inputStream = new FileInputStream(proFileName);
			if (inputStream != null) {
				ret.load(inputStream);
			}
		} catch (FileNotFoundException e) {
			System.out.println("new properties file : " + proFileName);
		}
		return ret;
	}
}
