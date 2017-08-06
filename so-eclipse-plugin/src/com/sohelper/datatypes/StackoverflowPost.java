package com.sohelper.datatypes;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.sohelper.exceptions.StackoverflowParserException;

/**
 * This class is used to construct a post from Stack Overflow.
 */
public class StackoverflowPost {
	
	private String url;
	private Document doc;

	public StackoverflowPost(String url) throws IOException {
		this.url = url;
		this.doc = Jsoup.connect(url).userAgent("Mozilla").ignoreHttpErrors(true).timeout(0).get();
	}
	
	public String getUrl() {
		return this.url;
	}
	
	/**
	 * Returns the answers in this post.
	 * @return A list containing <code>StackoverflowAnswer</code>s in this post.
	 */
	public List<StackoverflowAnswer> getAnswers() {
		Elements stackoverflowAnswers = doc.select("#answers > div");
		List<StackoverflowAnswer> soAnswers = new ArrayList<>();
		
		for (Element element : stackoverflowAnswers) {
			try {
				StackoverflowAnswer answer = new StackoverflowAnswer(element);
				soAnswers.add(answer);
			} catch (StackoverflowParserException e) {
				// do not add unparsed answers
			}
		}

		return soAnswers;
	}
}
