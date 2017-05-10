/*
 * Copyright 2017 Accenture. All Rights Reserved.
 * The trademarks used in these materials are the properties of their respective owners.
 * This work is protected by copyright law and contains valuable trade secrets and
 * confidential information.
 *
 */

package com.watson;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;

public class Main {
	static Property property = new Property();
	static String apiKey = property.getApiKey();
	static String imageToClassify = property.getImageLocation();
	static String classifierId = property.getClassifierId();
	static String negativeFileLocation = property.getClassifierId();
	static String[] positiveFileLocations = property.getPositiveFileLocations();
	static String classifierName=property.getClassifierName();
	static String[] positiveFileNames=property.getPositiveFileNames();
	
	public static void main(String[] args) {

		Helper helper = new Helper(VisualRecognition.VERSION_DATE_2016_05_20, apiKey);
		helper.classifyAnImage(apiKey, imageToClassify, classifierId,	VisualRecognition.VERSION_DATE_2016_05_20);
//		This will train watson for the Positive Images  
//		helper.trainWatson(VisualRecognition.VERSION_DATE_2016_05_20, classifierName, negativeFileLocation,positiveFileLocations,positiveFileNames);
//		helper.getClassifierDetails(classifierId, VisualRecognition.VERSION_DATE_2016_05_20);
		
	}

}
