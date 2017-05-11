/*
 * Copyright 2017 Accenture. All Rights Reserved.
 * The trademarks used in these materials are the properties of their respective owners.
 * This work is protected by copyright law and contains valuable trade secrets and
 * confidential information.
 *
 */

package com.watson;

import java.io.File;

import javax.print.DocFlavor.STRING;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.http.RequestBuilder;
import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.util.ResponseConverterUtils;
import com.ibm.watson.developer_cloud.util.Validator;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifierOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Builder;
import okhttp3.RequestBody;

public class Helper extends VisualRecognition {
	VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);

	public Helper(String versionDate, String apiKey) {
		super(versionDate, apiKey);
	}

	public ServiceCall<VisualClassifier> getClassifierDetails(String classifierId,
			String versionDate) {
		Validator.isTrue(classifierId != null && !classifierId.isEmpty(),
				"classifierId cannot be null or empty");

		RequestBuilder requestBuilder = RequestBuilder.get(String.format(PATH_CLASSIFIER,
				classifierId));
		requestBuilder.query(VERSION, versionDate);
		ServiceCall serviceCall = createServiceCall(requestBuilder.build(),
				ResponseConverterUtils.getObject(VisualClassifier.class));
		System.out.println(serviceCall.execute());
		return serviceCall;
	}

	public ImageClassifierBean classifyAnImage(String apiKey, String fileLocation, String classifierId,
			String versionDate) {
		VisualRecognition service = new VisualRecognition(versionDate);
		service.setApiKey(apiKey);
		System.out.println("Classify an image");
		ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
				.classifierIds(classifierId).images(new File(fileLocation)).build();
		VisualClassification result = service.classify(options).execute();
		System.out.println(result + "\n \n \n");
		
		ImageClassifierBean imageClassifierBean=getImageClassName(result);
		return imageClassifierBean;
	}

	private ImageClassifierBean getImageClassName(VisualClassification result) {
		 ImageClassifierBean imageClassifierBean=new ImageClassifierBean();  

		JSONObject jsonObject=new JSONObject(result); 
		JSONArray jsonArray=jsonObject.getJSONArray("images"); 
		JSONObject jsonObject2=jsonArray.getJSONObject(0);
		JSONArray jsonArray2 =jsonObject2.getJSONArray("classifiers");
		if(jsonArray2.length()!=0){
		JSONObject jsonObject4 =jsonArray2.getJSONObject(0);
		JSONArray jsonArray3=jsonObject4.getJSONArray("classes");
		imageClassifierBean.setClassName(jsonArray3.getJSONObject(0).getString("name").toString());
		imageClassifierBean.setFileName(jsonObject2.getString("image"));
		}else{
			imageClassifierBean.setClassName("Unidentified Document");
			imageClassifierBean.setFileName(jsonObject2.getString("image"));
		}
		return imageClassifierBean;
		 
	}

	public void trainWatson(String versionDate,String classifierName,String negativeFilelocation, String[] positiveFileLocations, String[] positiveFileNames) {

		com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifierOptions.Builder builder = new ClassifierOptions.Builder();
		
			for(int i=0;i<positiveFileLocations.length;i++)
		{
			builder.addClass(positiveFileNames[i], new File(positiveFileLocations[i]));
		}
		/*builder.addClass("beagle", new File("beagle.zip"));
		builder.addClass("goldenRetriever", new File("goldenRetriever.zip"));
		builder.addClass("husky", new File("husky.zip"));*/
		
		builder.negativeExamples(new File(negativeFilelocation));
		builder.classifierName(classifierName);
		ClassifierOptions options = builder.build();
		Validator.notNull(options, " options cannot be null");

		Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
		bodyBuilder.addFormDataPart(PARAM_NAME, options.classifierName());

		// Classes
		for ( String className : options.classNames()) {
			String dataName = className + "_" + PARAM_POSITIVE_EXAMPLES;
			RequestBody requestBody = RequestBody.create(HttpMediaType.BINARY_FILE,
					options.positiveExamplesByClassName(className));
			bodyBuilder.addFormDataPart(dataName, options.positiveExamplesByClassName(className)
					.getName(), requestBody);
		}

		if (options.negativeExamples() != null) {
			RequestBody requestBody = RequestBody.create(HttpMediaType.BINARY_FILE,
					options.negativeExamples());
			bodyBuilder.addFormDataPart(PARAM_NEGATIVE_EXAMPLES, options.negativeExamples()
					.getName(), requestBody);
		}

		RequestBuilder requestBuilder = RequestBuilder.post(PATH_CLASSIFIERS);
		requestBuilder.query(VERSION, versionDate).body(bodyBuilder.build());
		ServiceCall serviceCall = createServiceCall(requestBuilder.build(),
				ResponseConverterUtils.getObject(VisualClassifier.class));
		System.out.println(serviceCall.execute());
	}

	public String unzipFiles(String sourceZipLocation, String sourceZipName, String destinationUnzipLocation) {

		ZipFile file;
		try {
			file = new ZipFile(sourceZipLocation+sourceZipName);
			file.extractAll(destinationUnzipLocation);
		} catch (ZipException e) {
			e.printStackTrace();
		}
		return destinationUnzipLocation+sourceZipName.substring(0, sourceZipName.length()-4);
	}
	
	
	private static final String PARAM_NAME = "name";
	private static final String PARAM_NEGATIVE_EXAMPLES = "negative_examples";
	private static final String PARAM_POSITIVE_EXAMPLES = "positive_examples";
	private static final String PATH_CLASSIFIER = "/v3/classifiers/%s";
	private static final String PATH_CLASSIFIERS = "/v3/classifiers";
	private static final String URL = "https://gateway-a.watsonplatform.net/visual-recognition/api";
}
