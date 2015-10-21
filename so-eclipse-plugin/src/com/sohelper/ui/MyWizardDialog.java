package com.sohelper.ui;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sohelper.answers.StackoverflowAnswer;
import com.sohelper.answers.StackoverflowPost;
import com.sohelper.handlers.GoogleResult;

/**
 * @author Sebastian Raubach
 * @author Maroun Maroun
 *
 */
public class MyWizardDialog extends WizardDialog {
	
	private final String SEARCH_SERVICE = "https://www.google.com/search?as_q=";
		
	public MyWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
	    super.createButtonsForButtonBar(parent);

	    Button finish = getButton(IDialogConstants.NEXT_ID);
	    finish.setText("Get answers!");
	    setButtonLayoutData(finish);
	}

	@Override
	protected void nextPressed() {
		AnswerPage answerPage = (AnswerPage) getCurrentPage().getNextPage();
		if (getCurrentPage() instanceof QuestionPage) {
			QuestionPage questionPage = (QuestionPage) getCurrentPage();

			try {
				String question = questionPage.getQuesiton();
				questionPage.getContainer().run(false, true, new IRunnableWithProgress() 
				{
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						monitor.beginTask("Getting answers from Stack Overflow...", 100);

						List<GoogleResult> googleResults = null;
						try {
							monitor.worked(18);
							googleResults = getGoogleResults(question);
							monitor.worked(50);
						} catch (IOException e) {
							e.printStackTrace();
						}
						List<StackoverflowAnswer> stackoverflowAnswers = new ArrayList<>();
						List<StackoverflowPost> stackoverflowPosts = null;
						try {
							stackoverflowPosts = getStackOverflowPosts(googleResults);
							monitor.worked(80);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						for (StackoverflowPost post : stackoverflowPosts) {
							for (StackoverflowAnswer answer : post.getAnswers()) {
								if (answer.getUrl() == null)
									continue;
								stackoverflowAnswers.add(answer);
							}
						}
						
						monitor.done();
						answerPage.setAnswer(stackoverflowAnswers);
					}
				});
			}
			catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		super.nextPressed();
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

	@Override
	protected void cancelPressed() {
		this.close();
	}
}