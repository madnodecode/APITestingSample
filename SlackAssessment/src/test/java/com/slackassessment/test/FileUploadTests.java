package com.slackassessment.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.slackassessment.exceptions.*;
import com.slackassessment.impl.FileOperationClientImpl;
import com.slackassessment.utils.FileOperationsUtil;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUploadTests {

	File file;
	String token;

	/**
	 * This method loads the class to retrieve the resource.
	 * 
	 */
	@BeforeTest(groups = { "SmokeTests" })
	public void prepForTest() {
		ClassLoader classLoader = getClass().getClassLoader();
		Properties property = new Properties();
		String configResourceName = "config.properties";
		InputStream input;

		{
			try {
				input = classLoader.getResourceAsStream(configResourceName);
				property.load(input);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		file = new File(classLoader.getResource(property.getProperty("test_image_png_lessthan_1mb")).getFile());
		token = property.getProperty("adminuser_token");
	}

	/**
	 * Test to validate if File Upload Operation is successful File ID is returned
	 * as part of the API Response All expected Thumbnail URL's are present
	 * 
	 * @throws IOException
	 * @throws JSONException
	 * @throws ThumbNailUrlsMissingException
	 * 
	 */
	@Test(groups = { "SmokeTests" })
	public void fileUploadHappyPathTest() throws IOException, JSONException, ThumbNailUrlsMissingException {
		JSONObject responseObject = FileOperationsUtil.testUploadFile(file, token);
		Boolean fileObjectStatus = responseObject.getBoolean("ok");
		JSONObject fileJsonObj = responseObject.getJSONObject("file");
		Iterator<String> keys = fileJsonObj.keys();

		List<String> expectedThumbNailUrls = new ArrayList<String>(
				Arrays.asList("thumb_64", "thumb_80", "thumb_160", "thumb_360", "thumb_480"));

		int counter = 0;
		while (keys.hasNext()) {
			String currentDynamicKey = (String) keys.next();
			if (expectedThumbNailUrls.contains(currentDynamicKey)) {
				counter++;
			}
		}

		// Assertions to check below
		/*
		 * a. File upload operation was successful b. if File ID exists c. If all the
		 * expected Thumbnail URLS are present
		 */

		assertTrue(fileObjectStatus, "File upload operation unsuccessful");
		Assert.assertNotNull(fileJsonObj.get("id"));
		Assert.assertEquals(counter == expectedThumbNailUrls.size(), true);

	}

	/**
	 * @throws IOException
	 * @throws JSONException
	 */
	@Test
	void testFileUploadValidFiles() throws IOException, JSONException {

		Map<String, String> thumbURLMap = new HashMap<String, String>();
		String expectedFileName = FileOperationsUtil.convertFileNameToLowerCase(file);
		JSONObject responseObject = FileOperationsUtil.testUploadFile(file, token);
		Boolean fileObjectStatus = responseObject.getBoolean("ok");
		JSONObject fileJsonObj = responseObject.getJSONObject("file");

		Iterator<String> keys = fileJsonObj.keys();

		extractThumbNailUrlToMap(thumbURLMap, expectedFileName, fileJsonObj, keys);

		assertTrue(fileObjectStatus, "File upload operation unsuccessful");
		Assert.assertNotNull(fileJsonObj.get("id"));
		for (String fileName : thumbURLMap.keySet()) {
			Assert.assertEquals(expectedFileName, fileName);
		}

	}

	/**
	 * Method to extractThumbNailURLs from JSON Response to a MAP
	 * @param thumbURLMap
	 * @param expectedFileName
	 * @param fileJsonObj
	 * @param keys
	 * @throws JSONException
	 */
	private void extractThumbNailUrlToMap(Map<String, String> thumbURLMap, String expectedFileName,
			JSONObject fileJsonObj, Iterator<String> keys) throws JSONException {
		while (keys.hasNext()) {
			String currentKey = (String) keys.next();
			if (currentKey.matches("thumb_[0-9]*")) {
				String expectedThumbFileExtension = expectedFileName.substring(0, expectedFileName.indexOf("."))
						+ expectedFileName.substring(expectedFileName.indexOf("."));
				thumbURLMap.put(expectedThumbFileExtension, fileJsonObj.getString(currentKey));
			}
		}
	}

}
