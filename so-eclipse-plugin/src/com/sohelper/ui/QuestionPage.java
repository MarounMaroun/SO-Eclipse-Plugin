package com.sohelper.ui;

import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * This page is the "question" page - where the user insert their question.
 * 
 * @author Sebastian Raubach
 * @author Maroun Maroun
 *
 */
class QuestionPage extends WizardPage {
	private Text questionText;
	private Composite container;

	public QuestionPage() {
		super("Question Page");
		setTitle("SO ready to help");
		setDescription("Enter your question");
		setPageComplete(true);
	}
	
	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 20;
		Label label1 = new Label(container, SWT.NONE);
		label1.setText("Question: ");

		questionText = new Text(container, SWT.BORDER | SWT.SINGLE);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		questionText.setLayoutData(gd);
		
		Composite optionsCont = new Composite(container, SWT.NONE);
		GridLayout optionsLayout = new GridLayout();
		optionsLayout.horizontalSpacing = 20;
		optionsLayout.numColumns = 2;
		GridData optionsData = new GridData(GridData.VERTICAL_ALIGN_END);
		optionsData.horizontalSpan = 2;
		optionsData.horizontalAlignment = GridData.FILL;
		optionsCont.setLayoutData(optionsData);
		optionsCont.setLayout(optionsLayout);
		
		Button check = new Button(optionsCont, SWT.CHECK);
		Label onlyAcceptedLbl = new Label(optionsCont, SWT.NONE);
		onlyAcceptedLbl.setText("Accepted only (soon)");
		check.setSelection(false); 
		check.setEnabled(false);
				
		Button votedUpCheckbox = new Button(optionsCont, SWT.CHECK);
		Label onlyVotedUpLbl = new Label(optionsCont, SWT.NONE);
		onlyVotedUpLbl.setText("Voted only (soon)");
		votedUpCheckbox.setSelection(false); 
		votedUpCheckbox.setEnabled(false);
		
		// required to avoid an error in the system
		setControl(container);
		setPageComplete(true);
	}

	public String getQuesiton() {
		return questionText.getText();
	}
	
	@Override
	public boolean canFlipToNextPage()
	{
		return true;
	}

	public IWizardContainer getContainer()
	{
		return super.getContainer();
	}
}