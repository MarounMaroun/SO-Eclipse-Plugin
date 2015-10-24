package com.sohelper.fetchers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.sohelper.datatypes.GoogleResult;


public class GoogleFetcher {
	
	private final static String SEARCH_SERVICE = "https://www.google.com/search?as_q=";
	private final static String DOMAIN = ":stackoverflow.com";


	/**
	 * Searches in Google for a query and returns a list containing <code>GoogleResult</code> objects.
	 * 
	 * @param input the query to search.
	 * @param monitor 
	 * @return A list containing results of type <code>GoogleResult</code>.
	 * @throws IOException If a connection couldn't be established.
	 */
	public static List<GoogleResult> getGoogleResults(String input, IProgressMonitor monitor) throws IOException {
		ArrayList<GoogleResult> googleResults = new ArrayList<>();

		String query = SEARCH_SERVICE + input + DOMAIN;
		monitor.worked(10);
		
		Document doc = Jsoup.connect(query).userAgent("Mozilla").ignoreHttpErrors(true).timeout(0).get();

		Elements links = doc.select("li[class=g]");
		
		int size = links.size();

		for (int i=0; i < size; i++) {
			monitor.worked(20 / size);
			
			GoogleResult gr = new GoogleResult();

			// get title
			Elements titles = links.get(i).select("h3[class=r]");
			String title = titles.text();

			// extract link to the post in Stack Overflow
			Pattern p = Pattern.compile("url\\?q=(.*?)&");
			Matcher m = p.matcher(links.get(i).select("h3.r > a").attr("href"));

			String linkToPost = "";

			if (m.find()) {
				linkToPost = m.group(1);
			}

			gr.setTitle(title);
			gr.setUrl(new URL(linkToPost));

			googleResults.add(gr);
		}

		return googleResults;
	}
	
}
