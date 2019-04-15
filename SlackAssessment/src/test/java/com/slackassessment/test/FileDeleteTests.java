package com.slackassessment.test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.slackassessment.exceptions.ThumbNailUrlsMissingException;
import com.slackassessment.impl.FileOperationClientImpl;
import com.slackassessment.utils.FileOperationsUtil;


public class FileDeleteTests {
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
	 * Test to validate if File Delete Operation is successful 
	 * API Response returned ok-->true
	 * 
	 * @throws IOException
	 * @throws JSONException
	 * @throws ThumbNailUrlsMissingException
	 * 
	 */
	@Test(groups= {"SmokeTests"})
	public void fileDeleteHappyPathTest() throws IOException, JSONException {
		JSONObject responseObject = FileOperationsUtil.testUploadFile(file,token);
		JSONObject slackFile = responseObject.getJSONObject("file");
        String fileId = slackFile.getString("id");
        
        String body =  "{\"file\": \"" + fileId + "\"}";
        JSONObject responseDeleteObject = FileOperationClientImpl.delete(body, token);
        Boolean fileObjectStatus = responseDeleteObject.getBoolean("ok");
        
        //Assertion to check if Delete operation worked.
		assertTrue(fileObjectStatus, "File delete operation Successful");

	} 
	
	
	/**
	 * Test to validate if File Delete Operation is successful 
	 * API Response returned ok-->true
	 * 
	 * @throws IOException
	 * @throws JSONException
	 * @throws ThumbNailUrlsMissingException
	 * 
	 */
	@Test
	public void fileDeleteConfirmTest() throws IOException, JSONException {
		JSONObject responseObject = FileOperationsUtil.testUploadFile(file,token);
		JSONObject slackFile = responseObject.getJSONObject("file");
        String fileId = slackFile.getString("id");
        
        String body =  "{\"file\": \"" + fileId + "\"}";
        JSONObject responseDeleteObject = FileOperationClientImpl.delete(body, token);
        Boolean fileDeleteObjectStatus = responseDeleteObject.getBoolean("ok");
        
        //Assertion to check if Delete operation worked.
		assertTrue(fileDeleteObjectStatus, "File delete operation Successful");
		
		JSONObject responseDeleteAgainObject = FileOperationClientImpl.delete(body, token);
		Boolean fileDeleteAgainObjectStatus = responseDeleteAgainObject.getBoolean("ok");
        
        //Assertion to check if Delete operation worked.
		Assert.assertFalse(fileDeleteAgainObjectStatus, "File Already Deleted");

	} 
	
	//FHQBL5MUH

	/**
	 * Test to validate if File Delete Operation is successful 
	 * API Response returned ok-->true
	 * 
	 * @throws IOException
	 * @throws JSONException
	 * @throws ThumbNailUrlsMissingException
	 * 
	 */
	@Test
	public void fileDeleteNonExistentFileTest() throws IOException, JSONException {
		
        String fileId = "FHQBL5MUH";
        
        String body =  "{\"file\": \"" + fileId + "\"}";
        JSONObject responseObject = FileOperationClientImpl.delete(body, token);
        Boolean fileObjectStatus = responseObject.getBoolean("ok");
        String actualError = responseObject.getString("error");
        
        //Assertion to check if Delete operation worked.
		Assert.assertFalse(fileObjectStatus, "File delete operation Unsuccessful");
		Assert.assertEquals(actualError, "file_deleted");

	} 
	
}
