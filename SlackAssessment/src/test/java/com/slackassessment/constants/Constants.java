package com.slackassessment.constants;

import okhttp3.MediaType;

public class Constants {

	 public static final String token = "xoxp-591371961266-591278981011-611182364887-8b5d7f1e792e0a5844b03a38dcba6b45";
	 public static final String uploadUrl = "https://slack.com/api/files.upload";
	 public static final String listUrl = "https://slack.com/api/files.list";
	 public static final String deleteUrl = "https://slack.com/api/files.delete";
	 public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
}
