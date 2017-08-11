package com.sohelper.datatypes;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sohelper.exceptions.StackoverflowParserException;

import lombok.Getter;
import lombok.Setter;

/**
 * This class is used to construct a Stack Overflow answer.
 */
public class StackoverflowAnswer {

	@Getter private Element element;
	@Getter private String user;
	@Getter private String reputation;
	@Getter private String url;
	@Getter private String body;
	@Getter @Setter private String voteCount;
	@Getter @Setter private String userUrl;
	@Getter @Setter private boolean isAccepted;
	@Getter @Setter private boolean isUpVoted;
	
	public StackoverflowAnswer(Element element) throws StackoverflowParserException {
		try {
			this.element = element;
			
			String acceptedAnswerText = element.getElementsByAttributeValue("itemprop", "acceptedAnswer").attr("itemprop").toString();
			this.isAccepted = "acceptedAnswer".equals(acceptedAnswerText);
			
			String votedUp = element.select("span[class=vote-count-post]").text();
			if (votedUp != null && !votedUp.isEmpty()) {
				int votedUpCount = Integer.parseInt(votedUp);
				this.isUpVoted = votedUpCount != 0 && votedUpCount > 0;
			} else {
				this.isUpVoted = false;
			}
			
			Element userElement = element.select("table.fw").select("div.user-info").last().select("a").last();
			this.user = userElement.text();
			this.userUrl = "stackoverflow.com" + userElement.attr("href");
						
			Elements reputationElement = element.select("div.user-details").select("span.reputation-score");
			if (reputationElement.size() == 1) {
				this.reputation = reputationElement.text();
			} else if (reputationElement.size() > 1) {
				this.reputation = reputationElement.text().split("\\s+")[1];
			} else {
				this.reputation = "community wiki";
			}
			
			this.url = "stackoverflow.com" + element.select("a.short-link").attr("href");
		
			this.body = element.select("div.post-text").html().replace("<code>", "<span style=\"background-color: #DCDCDC\"><code>");
			this.body = this.body.replace("</code>", "</span></code>");
			
			this.voteCount = element.select("span.vote-count-post").text();
		}
		catch (Exception e) {
			throw new StackoverflowParserException("Couldn't parse Stack Overflow post with element: " + element.toString());
		}
	}
}
