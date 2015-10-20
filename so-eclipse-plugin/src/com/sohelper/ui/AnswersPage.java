package com.sohelper.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * This page is the "question" page - where the user insert their question.
 */
class AnswersPage extends WizardPage {

	Text questionArea;
	
	public String getQuestion() {
		return questionArea.getText();
	}

	AnswersPage() {
		super("QuestionPage");
		setDescription("Enter a question");
	}
	
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.BORDER);
		composite.setLayout(new GridLayout(2, false));

		// where the user inserts the text
		questionArea = new Text(composite, SWT.V_SCROLL);
		GridData data = new GridData();
		
		// declare new font for the question area
	    Font initialFont = questionArea.getFont();
	    FontData[] fontData = initialFont.getFontData();
	    for (int i = 0; i < fontData.length; i++) {
	      fontData[i].setHeight(17);
	    }
	    Font newFont = new Font(composite.getDisplay(), fontData);
	    questionArea.setFont(newFont);
		
	    // stretch the text to the whole component
		data.grabExcessVerticalSpace = true;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		data.verticalAlignment = SWT.FILL;
		questionArea.setLayoutData(data);

		setControl(composite);
	}
}