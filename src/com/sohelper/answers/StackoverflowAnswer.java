package com.sohelper.answers;

import org.jsoup.nodes.Element;

public class StackoverflowAnswer {

	private Element element;
	private String user;
	private String reputation;
	private String url;
	private String body;
	
	public StackoverflowAnswer(Element element) {
		try {
			this.element = element;
			this.user = element.select("table.fw").select("div.user-info").last().select("a").text();
			this.reputation = element.select("div.user-details").select("span.reputation-score").text();
			this.url = "stackoverflow.com" + element.select("a.short-link").attr("href");
		
			this.body = element.select("div.post-text").html().replace("<code>", "<span style=\"background-color: #DCDCDC\"><code>");
			this.body = this.body.replace("</code>", "</span></code>");
		}
		catch (Exception e) {
			
		}
	}

	public Element getElement() {
		return this.element;
	}
	
	public String getUser() {
		return this.user;
	}
	
	// String because of Jon Skeet
	public String getReputation() {
		return this.reputation;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public String getBody() {
		return this.body;
	}
}
