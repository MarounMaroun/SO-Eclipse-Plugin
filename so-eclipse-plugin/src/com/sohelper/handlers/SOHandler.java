package com.sohelper.handlers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sohelper.answers.StackoverflowAnswer;
import com.sohelper.answers.StackoverflowPost;
import com.sohelper.ui.AnswersDialog;


/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SOHandler extends AbstractHandler {
	
	private final String SEARCH_SERVICE = "https://www.google.com/search?as_q=";

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		InputDialog askDialog = new InputDialog(window.getShell(), "SO ready to help", "Enter your question:", "", e -> { return null; });
		
		if (askDialog.open() == Window.OK) {
			String input =  askDialog.getValue();
			try {
				List<GoogleResult> googleResults = getGoogleResults(input);
				List<StackoverflowAnswer> stackoverflowAnswers = new ArrayList<>();
				List<StackoverflowPost> stackoverflowPosts = getStackOverflowPosts(googleResults);
				
				for (StackoverflowPost post : stackoverflowPosts) {
					for (StackoverflowAnswer answer : post.getAnswers()) {
						if (answer.getUrl() == null)
							continue;
						stackoverflowAnswers.add(answer);
					}
				}
				AnswersDialog dialog = new AnswersDialog(window.getShell(), stackoverflowAnswers);
				dialog.open();
				 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private List<StackoverflowPost> getStackOverflowPosts(List<GoogleResult> googleResults) throws IOException {
		List<StackoverflowPost> stackoverflowPosts = new ArrayList<>();
		
		for (GoogleResult gr : googleResults) {
				stackoverflowPosts.add(new StackoverflowPost(gr.getUrl().toString()));
		}
		return stackoverflowPosts;
	}

	private List<GoogleResult> getGoogleResults(String input) throws IOException {
		ArrayList<GoogleResult> googleResults = new ArrayList<>();

		String query = SEARCH_SERVICE + input + ":stackoverflow.com";
        Document doc = Jsoup.connect(query).userAgent("Mozilla").ignoreHttpErrors(true).timeout(0).get();
        
        Elements links = doc.select("li[class=g]");
        
        for (Element link : links) {
        	GoogleResult gr = new GoogleResult();
        	
        	// get titles
        	Elements titles = link.select("h3[class=r]");
        	String title = titles.text();
        	
        	// extract link to the post in Stack Overflow
            Pattern p = Pattern.compile("url\\?q=(.*?)&");
            Matcher m = p.matcher(link.select("h3.r > a").attr("href"));
            
            String linkToPost = null;
            
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