package com.herald.ezherald.academic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataTypeTransition {

	public static String InputStreamToString(InputStream in) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();

	}

	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\Admin\\Desktop\\I would.txt");
		InputStream in = new FileInputStream(file);
		System.out.println(DataTypeTransition.InputStreamToString(in));
	}

}
