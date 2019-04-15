package com.slackassessment.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.slackassessment.impl.FileOperationClientImpl;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import java.io.File;


public class FileOperationsUtil {


	/**
	 * @return String - lowercase form of the original uploaded file
	 */
	public static String convertFileNameToLowerCase(File file) {
		return file.getName().toLowerCase();
	}

	public static JSONObject testUploadFile(File file, String token) throws IOException, JSONException {

		// MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
		multipartBuilder.addFormDataPart("token", token);
		multipartBuilder.setType(MultipartBody.FORM);
		multipartBuilder.addFormDataPart("file", file.getName(),
				RequestBody.create(MediaType.parse("multipart/form-data"), file));
		JSONObject responseObject = FileOperationClientImpl.fileUpload(multipartBuilder);
		return responseObject;
	}
}
