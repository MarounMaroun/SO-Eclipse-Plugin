package com.sohelper.handlers;

import java.net.URL;
import java.util.List;
import java.io.Reader;
import java.net.URLEncoder;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStreamReader;

import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionException;

import com.google.gson.Gson;

import com.sohelper.ui.AnswersDialog;
import com.sohelper.answers.StackoverflowAnswer;
import com.sohelper.answers.StackoverflowPost;
import com.sohelper.handlers.GoogleResult.Result;


/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {
	
	private int startFrom = 0;
	private final String SEARCH_SERVICE = "https://ajax.googleapis.com/ajax/services/search/web?v=1.0&start=";

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
			for (Result result : gr.getResponseData().getResults()) {
				if (new URL(result.getUrl()).getHost().equals("stackoverflow.com")) {
					stackoverflowPosts.add(new StackoverflowPost(result.getUrl()));
				}
			}
		}
		return stackoverflowPosts;
	}

	private List<GoogleResult> getGoogleResults(String input) throws IOException {
		ArrayList<GoogleResult> googleResults = new ArrayList<>();
		URL url = null;
		String query;
		
		while (startFrom < 12) {
			query = SEARCH_SERVICE + String.valueOf(startFrom) + "&q=" + URLEncoder.encode(input, "UTF-8");
			
			url = new URL(query);
			Reader reader = new InputStreamReader(url.openStream(), "UTF-8");
			GoogleResult results = new Gson().fromJson(reader, GoogleResult.class);
			
			googleResults.add(results);
			startFrom += 4;
		}
		startFrom = 0;
		
		return googleResults;
	}
}