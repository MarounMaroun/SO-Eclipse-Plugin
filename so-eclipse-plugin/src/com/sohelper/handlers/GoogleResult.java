package com.sohelper.handlers;

import java.net.URL;

/**
 * Represents a response from Google search.
 */
public class GoogleResult {

	private String title;
    private URL url;
    private String source;
    

	public String getTitle() {
		return title;
	}
    
	public void setTitle(String title) {
		this.title = title;
	}
	
	public URL getUrl() {
		return url;
	}
	
	public void setUrl(URL url) {
		this.url = url;
	}
	
    public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
    
}