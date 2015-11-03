package com.sohelper.datatypes;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This class is used to construct a Stack Overflow answer.
 */
public class StackoverflowAnswer {

	private Element element;
	private String user;
	private String reputation;
	private String url;
	private String body;
	private String voteCount;
	private String userUrl;
	
	private Boolean isAcceptedAnswer;
	
	// String Constants
	private final String ACCEPTED_ANSWER = "acceptedAnswer";
	private final String ITEM_PROP = "itemprop";
	
	public StackoverflowAnswer(Element element) {
		try {
			this.element = element;
			String acceptedAnswerText = element.getElementsByAttributeValue(ITEM_PROP, ACCEPTED_ANSWER).attr(ITEM_PROP).toString();
			if(acceptedAnswerText != null && !acceptedAnswerText.isEmpty() && ACCEPTED_ANSWER.equals(acceptedAnswerText)){
				this.isAcceptedAnswer = true;
			}else{
				this.isAcceptedAnswer = false;
			}
			Element userElement = element.select("table.fw").select("div.user-info").last().select("a").last();
			this.user = userElement.text();
			this.userUrl = "stackoverflow.com" + userElement.attr("href");
						
			Elements reputationElement = element.select("div.user-details").select("span.reputation-score");
			if (reputationElement.size() == 1) {
				this.reputation = reputationElement.text();
			} else {
				this.reputation = reputationElement.text().split("\\s+")[1];
			}
			
			this.url = "stackoverflow.com" + element.select("a.short-link").attr("href");
		
			this.body = element.select("div.post-text").html().replace("<code>", "<span style=\"background-color: #DCDCDC\"><code>");
			this.body = this.body.replace("</code>", "</span></code>");
			
			this.voteCount = element.select("span.vote-count-post").text();
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

	public String getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(String voteCount) {
		this.voteCount = voteCount;
	}

	public String getUserUrl() {
		return userUrl;
	}

	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}
	
	public Boolean getIsAcceptedAnswer() {
		return isAcceptedAnswer;
	}

	public void setIsAcceptedAnswer(Boolean isAcceptedAnswer) {
		this.isAcceptedAnswer = isAcceptedAnswer;
	}
}