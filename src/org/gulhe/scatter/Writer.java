package org.gulhe.scatter;

public interface Writer {

	public Writer writeTile(String image, int i, int j);
	public void close();
	Writer closeColumn();
	Writer newColumn();
	Writer closeLine();
	Writer newLine();
	
}
