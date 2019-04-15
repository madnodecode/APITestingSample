package com.slackassessment.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.slackassessment.exceptions.ThumbNailUrlsMissingException;
import com.slackassessment.impl.FileOperationClientImpl;
import com.slackassessment.utils.FileOperationsUtil;

public class FileListTests {

	File file;
	String token;

	/**
	 * This method loads the class to retrieve the resource.
	 * 
	 */
	@BeforeTest(groups= {"SmokeTests"})
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
	 * Test to validate if File List Operation is successful 
	 * API Response returned ok-->true
	 * 
	 * @throws IOException
	 * @throws JSONException
	 * @throws ThumbNailUrlsMissingException
	 * 
	 */
	@Test(groups= {"SmokeTests"})
	public void fileListHappyPathTest() throws IOException, JSONException {
		JSONObject responseObject = FileOperationsUtil.testUploadFile(file,token);
		JSONObject fileObject = responseObject.getJSONObject("file");
        String fileId = fileObject.getString("id");
        
        Map<String, String> params = new HashMap<String,String>();
        params.put("token", token);
        params.put("types", "images");
        JSONObject listFilesObject = FileOperationClientImpl.listFiles(params);
        Boolean fileListObjectStatus = listFilesObject.getBoolean("ok");
        
        //Assertion to check if List operation worked.
      	assertTrue(fileListObjectStatus, "File List operation Successful");
      	
	}
	
	
	/**
	 * Test to validate if File List Operation is successful 
	 * API Response returned ok-->true
	 * 
	 * @throws IOException
	 * @throws JSONException
	 * @throws InterruptedException 
	 * @throws ThumbNailUrlsMissingException
	 * 
	 */
	@Test
	public void fileListPreviouslyUploadedExistsTest() throws IOException, JSONException, InterruptedException {
        String fileId = "FHQH08FFB"; // instead of hardcoding this can also be read from data store
        
        Map<String, String> params = new HashMap<String,String>();
        params.put("token", token);
        params.put("types", "images");
        
        JSONObject listFilesObject = FileOperationClientImpl.listFiles(params);
        Boolean fileListObjectStatus = listFilesObject.getBoolean("ok");
        
        //Assertion to check if List operation worked.
      	assertTrue(fileListObjectStatus, "File List operation Successful");
        JSONArray listFileObject = listFilesObject.getJSONArray("files");
        
        Map<String, Integer> filesMap = new HashMap<String,Integer>();
        int lengthOfFiles = listFileObject.length(); 
        
        for (int index = 0; index < lengthOfFiles; index++) {
            JSONObject listFiles = listFileObject.getJSONObject(index);
            String listFileId = listFiles.getString("id");
            filesMap.put(listFileId, index);
        }

        Assert.assertEquals(filesMap.containsKey(fileId),true);
	}
	
	/**
	 * Test to validate if File List Operation is successful 
	 * API Response returned ok-->true
	 * 
	 * @throws IOException
	 * @throws JSONException
	 * @throws InterruptedException 
	 * @throws ThumbNailUrlsMissingException
	 * 
	 */
	@Test
	public void fileListJustUploadExistsTest() throws IOException, JSONException, InterruptedException {
		JSONObject responseObject = FileOperationsUtil.testUploadFile(file,token);
		JSONObject fileObject = responseObject.getJSONObject("file");
        String fileId = fileObject.getString("id");
        
        Map<String, String> params = new HashMap<String,String>();
        params.put("token", token);
        params.put("types", "images");
       
        //Introducing delay to ensure list gets the file just uploaded above. 
        // This can be done in a better way to reduce test run time.
        Thread.sleep(20000);
        
        JSONObject listFilesObject = FileOperationClientImpl.listFiles(params);
        Boolean fileListObjectStatus = listFilesObject.getBoolean("ok");
        
        //Assertion to check if List operation worked.
      	assertTrue(fileListObjectStatus, "File List operation Successful");
        JSONArray listFileObject = listFilesObject.getJSONArray("files");
        
        Map<String, Integer> filesMap = new HashMap<String,Integer>();
        int lengthOfFiles = listFileObject.length(); 
        
        for (int index = 0; index < lengthOfFiles; index++) {
            JSONObject listFiles = listFileObject.getJSONObject(index);
            String listFileId = listFiles.getString("id");
            filesMap.put(listFileId, index);
        }

        Assert.assertEquals(filesMap.containsKey(fileId),true);
	}
}
