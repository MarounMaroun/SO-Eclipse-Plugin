package com.sohelper.ui;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.sohelper.datatypes.GoogleResult;
import com.sohelper.datatypes.StackoverflowAnswer;
import com.sohelper.datatypes.StackoverflowPost;
import com.sohelper.fetchers.GoogleFetcher;
import com.sohelper.fetchers.StackoverflowFetcher;

/**
 * Custom <code>WizardDialog</code>, used by the handler with an instance of <code>StackoverflowWizard</code>.
 * 
 * @author Sebastian Raubach
 * @author Maroun Maroun
 *
 */
public class MyWizardDialog extends WizardDialog {

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
				final String question = questionPage.getQuesiton();
				// if User has not given any input question then do not process and show error dialog box
				if (question == null || question.trim().isEmpty()) {
					MessageBox questionErrorBox = new MessageBox(new Shell(), SWT.OK | SWT.ICON_ERROR);
					questionErrorBox.setMessage("Please enter your question.");
					questionErrorBox.setText("Error");
					questionErrorBox.open();
					return;
				}
				questionPage.getContainer().run(false, true, (monitor) ->
				{					
					monitor.beginTask("Getting answers from Stack Overflow...", 100);

					try {
						List<GoogleResult> googleResults = GoogleFetcher.getGoogleResults(question, monitor);
						List<StackoverflowPost> stackoverflowPosts = StackoverflowFetcher.getStackoverflowPosts(googleResults, monitor);
						List<StackoverflowAnswer> stackoverflowAnswers = StackoverflowFetcher.getStackoverflowAnswers(stackoverflowPosts, monitor, questionPage);

						answerPage.setAnswer(stackoverflowAnswers);

					} catch(IOException e) {
						e.printStackTrace();
					}

					monitor.done();
				});
			}
			catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		super.nextPressed();
	}

	@Override
	protected void cancelPressed() {
		this.close();
	}
}