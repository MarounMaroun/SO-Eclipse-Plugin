package com.sohelper.ui;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;

import com.sohelper.answers.StackoverflowAnswer;

/**
 * This page contains the answers, with previous and next buttons.
 * 
 * @author Sebastian Raubach
 * @author Maroun Maroun
 *
 */
public class AnswerPage extends WizardPage {
	private Composite container;
	private Browser browser;
	private Link linkToAnswer;
	private int answerCount = 0;
	private List<StackoverflowAnswer> answers;

	public AnswerPage() {
		super("Answer Page");
		setTitle("SO ready to help");
		setDescription("Best SO answers");
		setControl(browser);
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);

		createAnswerArea(container);
		setControl(container);
		setPageComplete(false);
	}

	private void createAnswerArea(Composite container) {
		linkToAnswer = new Link(container, SWT.NONE);
		new Label(container, SWT.NONE);

		// Launches the operating system executable associated with the file or URL
		linkToAnswer.addListener(SWT.Selection, event -> Program.launch(event.text));

		browser = new Browser(container, SWT.BORDER);
		new Label(container, SWT.NONE);

		GridData gridDataBrowser = new GridData();
		gridDataBrowser.grabExcessVerticalSpace = true; 
		gridDataBrowser.grabExcessHorizontalSpace = true; 
		gridDataBrowser.horizontalAlignment = SWT.FILL;
		gridDataBrowser.verticalAlignment = SWT.FILL;
		browser.setLayoutData(gridDataBrowser);

		GridData gridDataUser = new GridData();
		gridDataUser.horizontalAlignment = SWT.FILL;

		linkToAnswer.setLayoutData(gridDataUser);

		Composite btns = new Composite(container, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		btns.setLayout(rowLayout);
		final Button previousBtn = new Button(btns, SWT.PUSH);
		final Button nextBtn = new Button(btns, SWT.PUSH);
		previousBtn.setText("Previous answer");
		nextBtn.setText("Next answer");

		Listener listener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (event.widget == nextBtn) {
					if (answerCount == answers.size() - 1) {
						answerCount = 0;
					} else { 	
						answerCount += 1;
					}
					setAnswerContent();
				} else if (answerCount == 0) {
					answerCount = answers.size() - 1;
				} else {
					answerCount -= 1;
				}
				setAnswerContent();
			}
		};
		
		nextBtn.addListener(SWT.Selection, listener);
		previousBtn.addListener(SWT.Selection, listener);

		setAnswerContent();
	}

	private void setAnswerContent() {
		if (this.answers == null) {
			return;
		}
		browser.setText("<html>" + answers.get(answerCount).getBody() + "</html>");		
		linkToAnswer.setText(getLinkToAnswr(answers.get(answerCount)));
		linkToAnswer.update();
	}

	private String getLinkToAnswr(StackoverflowAnswer stackoverflowAnswer) {
		String linkToAnswer = answers.get(answerCount).getUrl();
		String label = " (<a href=\"http://" + linkToAnswer + "\" style=\"text-decoration:none\">" + "go to answer" + "</a>)";
		String userName = stackoverflowAnswer.getUser();

		return "By: " + userName + " - " + stackoverflowAnswer.getReputation() + label;
	}
	
	public void setAnswer(List<StackoverflowAnswer> answers) {
		this.answers = answers;
		this.answerCount = 0;
		setAnswerContent();
	}
}