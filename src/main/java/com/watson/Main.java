/*
 * Copyright 2017 Accenture. All Rights Reserved.
 * The trademarks used in these materials are the properties of their respective owners.
 * This work is protected by copyright law and contains valuable trade secrets and
 * confidential information.
 *
 */

package com.watson;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;

public class Main {
	static Property property = new Property();
	static String apiKey = property.getApiKey();
	static String imageToClassify = property.getImageLocation();
	static String classifierId = property.getClassifierId();
	static String negativeFileLocation = property.getClassifierId();
	static String[] positiveFileLocations = property.getPositiveFileLocations();
	static String classifierName = property.getClassifierName();
	static String[] positiveFileNames = property.getPositiveFileNames();
	static String sourceZipLocation = property.getSourceZipLocation();
	static String sourceZipName = property.getSourceZipName();
	static String destinationUnzipLocation = property.getDestinationUnzipLocation();

	
	public static void main(String[] args) throws IOException {
		Helper helper = new Helper(VisualRecognition.VERSION_DATE_2016_05_20, apiKey);
		
		String unzippedFilesLoc=helper.unzipFiles(sourceZipLocation,sourceZipName,destinationUnzipLocation);

		File[] images=FileUtils.getFile(unzippedFilesLoc).listFiles();
		for(File image:images)
		{
			ImageClassifierBean imageClassifierBean=helper.classifyAnImage(apiKey, image.getAbsolutePath(), classifierId,	VisualRecognition.VERSION_DATE_2016_05_20);
			System.out.println(imageClassifierBean.getFileName()+" is identified as "+imageClassifierBean.getClassName());
		}
		
		FileUtils.deleteDirectory(new File(unzippedFilesLoc));
	}
	
	private void trainingTheEngine()
	{
		Helper helper = new Helper(VisualRecognition.VERSION_DATE_2016_05_20, apiKey);
//		This will train watson for the Positive Images  
		helper.trainWatson(VisualRecognition.VERSION_DATE_2016_05_20, classifierName, negativeFileLocation,positiveFileLocations,positiveFileNames);

	}

	private void getClassifierDetails()
	{
//		helper.getClassifierDetails(classifierId, VisualRecognition.VERSION_DATE_2016_05_20);
	}
}
