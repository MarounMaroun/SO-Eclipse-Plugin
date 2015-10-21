package com.sohelper.ui;

import org.eclipse.jface.wizard.Wizard;

/**
 * @author Sebastian Raubach
 * @author Maroun Maroun
 *
 */
public class StackoverflowWizard extends Wizard {
	
	public StackoverflowWizard() {
		setWindowTitle("SO Wizard");
		setNeedsProgressMonitor(true);
	}

	public void addPages() {
		addPage(new QuestionPage());
		addPage(new AnswerPage());
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	@Override
	public boolean canFinish() {
		return getContainer().getCurrentPage() instanceof AnswerPage;
	}
}