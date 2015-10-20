package com.sohelper.ui;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sohelper.answers.StackoverflowAnswer;
import com.sohelper.answers.StackoverflowPost;
import com.sohelper.handlers.GoogleResult;


/**
 * Wizard for the SO plug-in.
 * This will appear after clicking on "Ask me" button.
 *
 */
public class SOHelperWizard extends ApplicationWindow {

	private WizardDialog dialog;
	SOWizard questionWizard;
	
	public SOHelperWizard(Shell parentShell) {
		super(parentShell);
		questionWizard = new SOWizard();
		
		dialog = new WizardDialog(getShell(), questionWizard);
	}
	
	public void showWizard() {
		dialog.open();
	}
	
	public List<StackoverflowAnswer> getAnswers() {
		return questionWizard.getAnswers();
	}
}


class SOWizard extends Wizard {
	
	private final String SEARCH_SERVICE = "https://www.google.com/search?as_q=";
	
	String question;
	AnswersPage qp = new AnswersPage();
	AnswersPage ap = new AnswersPage();
	List<StackoverflowAnswer> answers;

	public SOWizard() {
		setWindowTitle("SO ready to help");
		setNeedsProgressMonitor(true);
		
		DialogSettings dialogSettings = new DialogSettings("userInfo");
		setDialogSettings(dialogSettings);
	}

	public void addPages() {
		addPage(qp);
	}
	
	public List<StackoverflowAnswer> getAnswers() {
		return answers;
	}


	public boolean performFinish() {
		try {
			question = qp.getQuestion();
			getContainer().run(true, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Getting answers", 100);
					monitor.worked(20);
					
					List<GoogleResult> googleResults = null;
					try {
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
					answers = stackoverflowAnswers;
					
					monitor.done();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		} 

		return true;
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
	
	public boolean performCancel() {
		boolean ans = MessageDialog.openConfirm(getShell(), "Confirmation", "Are you sure to cancel the task?");
		return ans;
	}  
}