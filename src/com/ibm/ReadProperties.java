package com.ibm;

import java.io.*;
import java.util.Properties;

public class ReadProperties {
	public Properties readPropertiesFile() throws IOException {

		String filepath="\\\\va10dwvfns300\\Shared\\Gauri\\Sakya\\PropertyFile\\config.properties";
		Properties prop=null;
		FileInputStream fis = new FileInputStream(filepath);
		try {
			prop = new Properties();
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fis.close();
		}
		return prop;
	}

}
