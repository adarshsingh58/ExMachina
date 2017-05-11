/*
 * Copyright 2017 Accenture. All Rights Reserved.
 * The trademarks used in these materials are the properties of their respective owners.
 * This work is protected by copyright law and contains valuable trade secrets and
 * confidential information.
 *
 */

package com.watson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class Property {
	static Properties properties;
	static {
		properties = new Properties();
		try {
			properties.load(Main.class.getClassLoader().getResourceAsStream(
					"credentials.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getNegativeFileLocation(){
		return properties.getProperty("negativeFileLocation");
	}
	
	public static String[] getPositiveFileNames(){
		return properties.getProperty("positiveFileNames").split(",");
	}
	public static String[] getPositiveFileLocations(){
		return properties.getProperty("positiveFileLocations").split(",");
	}
	public static String getClassifierName(){
		return properties.getProperty("classifierName");
	}

	public static String getClassifierId() {
		return properties.getProperty("classifierId");
	}

	public static String getImageLocation() {
		return properties.getProperty("imageToClassify");
	}

	public static String getSourceZipLocation() {

		return properties.getProperty("sourceZipLocation");
	}
	
	public static String getSourceZipName() {

		return properties.getProperty("sourceZipName");
	}
	public static String getApiKey() {

		return properties.getProperty("apiKey");
	}
	public static String getDestinationUnzipLocation() {

		return properties.getProperty("destinationUnzipLocation");
	}
	
}
