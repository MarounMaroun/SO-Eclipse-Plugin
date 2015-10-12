package com.sohelper.ui;


import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.dialogs.IDialogConstants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import com.sohelper.answers.StackoverflowAnswer;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class AnswersDialog extends TitleAreaDialog {

	private Composite area;
	private Browser browser;
	private Label userLabel;
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
		userLabel = new Label(area, SWT.NONE);
		browser = new Browser(container, SWT.BORDER);
		
		GridData gridDataBrowser = new GridData();
		gridDataBrowser.grabExcessVerticalSpace = true; 
		gridDataBrowser.horizontalAlignment = SWT.FILL;
		gridDataBrowser.verticalAlignment = SWT.FILL;
		
		GridData gridDataUser = new GridData();
		gridDataUser.horizontalAlignment = SWT.FILL;
		
		browser.setLayoutData(gridDataBrowser);
		userLabel.setLayoutData(gridDataUser);
		setAnswerContent();
	}

	private void setAnswerContent() {
		browser.setText("<html>" + answers.get(answerCount).getBody() + "</html>");		
		userLabel.setText("By: " + answers.get(answerCount).getUser() + " (" + answers.get(answerCount).getReputation().split("\\s+")[0] + ")");
		userLabel.update();
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