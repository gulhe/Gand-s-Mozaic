package org.gulhe.scatter.writer;

import java.io.FileWriter;
import java.io.IOException;

import org.gulhe.scatter.Writer;

public class HtmlWriterImpl implements Writer {

	private FileWriter fileWriter;

	public HtmlWriterImpl(int heightDelta, String string) {
		
		try {
			this.fileWriter = new FileWriter(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fileWriter.append("<html>" + "<head><style type='text/css'>"
					+ "tr {margin: 0;padding: 0;}" + "img {height: "
					+ (heightDelta * 2) + "px;}" + "</style></head>"
					+ "<body><table cellspacing='0' cellpadding='0'>\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Writer newLine() {
		try {
			this.fileWriter.append("\t<tr>\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public Writer closeLine() {
		try {
			fileWriter.append("\t\t</td>\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public Writer newColumn() {
		try {
			fileWriter.append("\t\t<td>\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public Writer closeColumn() {
		try {
			fileWriter.append("\t</tr>\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public Writer writeTile(String image,int x,int y) {
		String imageTag = "\t\t\t<img src=\"images/" + image + "\" />\n";
		System.out.println(imageTag);
		try {
			fileWriter.append(imageTag);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public void close() {
		try {
			fileWriter.append("</table></body></html>");
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
