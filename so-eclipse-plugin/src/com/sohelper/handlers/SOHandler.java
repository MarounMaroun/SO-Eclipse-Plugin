package com.sohelper.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sohelper.answers.StackoverflowAnswer;
import com.sohelper.ui.AnswersDialog;
import com.sohelper.ui.SOHelperWizard;


/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SOHandler extends AbstractHandler {
	

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		SOHelperWizard q = new SOHelperWizard(window.getShell());
		q.showWizard();
		List<StackoverflowAnswer> stackoverflowAnswers = q.getAnswers();
		AnswersDialog dialog = new AnswersDialog(window.getShell(), stackoverflowAnswers);
		dialog.open();
		
		return null;
	}
}