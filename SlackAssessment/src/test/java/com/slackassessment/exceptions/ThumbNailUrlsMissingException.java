package com.slackassessment.exceptions;

public class ThumbNailUrlsMissingException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 289538424062583019L;

	public ThumbNailUrlsMissingException()
	  {
	    super("Not all Expected ThumbNail URL's are present");
	  }
}
