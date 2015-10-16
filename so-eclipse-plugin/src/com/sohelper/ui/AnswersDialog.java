package com.sohelper.ui;


import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

import com.sohelper.answers.StackoverflowAnswer;

public class AnswersDialog extends TitleAreaDialog {

	private Composite area;
	private Browser browser;
	private Link linkToAnswer;
	private int answerCount = 0;
	private List<StackoverflowAnswer> answers;

	public AnswersDialog(Shell parentShell, List<StackoverflowAnswer> answers) {
		super(parentShell);
		this.answers = answers;
	}
	
	/**
	 * Adds "next" and "previous" buttons
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) { 
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", true);
		createButton(parent, IDialogConstants.BACK_ID, "Previous",true); 
		createButton(parent, IDialogConstants.NEXT_ID, "Next", true); 
	}

	@Override
	public void create() {
		super.create();
		setTitle("Best SO answers");
		setDialogHelpAvailable(false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		area = (Composite) super.createDialogArea(parent);
		
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL_VERTICAL;
		gridData.horizontalAlignment= GridData.FILL_HORIZONTAL;
		gridData.widthHint = 600;
		gridData.heightHint = 400;
		
		area.setLayoutData(gridData);
		GridLayout layout = new GridLayout();
		
		area.setLayout(layout);		
		createAnswerArea(area);
		return area;
	}

	private void createAnswerArea(Composite container) {
		linkToAnswer = new Link(area, SWT.NONE);
		
		// Launches the operating system executable associated with the file or URL
		linkToAnswer.addListener(SWT.Selection, event -> Program.launch(event.text));
		
		browser = new Browser(container, SWT.BORDER);
		
		GridData gridDataBrowser = new GridData();
		gridDataBrowser.grabExcessVerticalSpace = true; 
		gridDataBrowser.horizontalAlignment = SWT.FILL;
		gridDataBrowser.verticalAlignment = SWT.FILL;
		
		GridData gridDataUser = new GridData();
		gridDataUser.horizontalAlignment = SWT.FILL;
		
		browser.setLayoutData(gridDataBrowser);
		linkToAnswer.setLayoutData(gridDataUser);
		setAnswerContent();
	}

	private void setAnswerContent() {
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

	@Override
	protected boolean isResizable() {
		return true;
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.OK_ID == buttonId) {
			okPressed();
		} else if (IDialogConstants.CANCEL_ID == buttonId) {
			cancelPressed();
		} else if (IDialogConstants.NEXT_ID == buttonId) {
			if (answerCount == answers.size() - 1) {
				answerCount = 0;
			} else { 	
				answerCount += 1;
			}
			setAnswerContent();
		} else if (IDialogConstants.BACK_ID == buttonId) {
			if (answerCount == 0) {
				answerCount = answers.size() - 1;
			} else {
				answerCount -= 1;
			}
				
			setAnswerContent();
		}
	}
} 