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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;

import com.sohelper.datatypes.StackoverflowAnswer;

/**
 * This page contains the answers that was fetched from Stack Overflow.
 * 
 * @author Sebastian Raubach
 * @author Maroun Maroun
 *
 */
public class AnswerPage extends WizardPage {

    private Composite container;

    /** where the answers are displayed */
    private Browser browser;

    private Link linkToAnswer;

    private List<StackoverflowAnswer> answers;

    private int answerCount = 0;


    public AnswerPage() {
        super("Answer Page");
        setTitle("SO ready to help");
        setDescription("Best SO answers");
        setControl(browser);
    }

    @Override
    public void createControl(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;

        container = new Composite(parent, SWT.NONE);
        container.setLayout(layout);

        createAnswerArea(container);
        setControl(container);
        setPageComplete(false);
    }

    private void createAnswerArea(Composite container) {
        linkToAnswer = new Link(container, SWT.NONE);
        
        GridData gridDataUser = new GridData();
        gridDataUser.horizontalAlignment = SWT.FILL;
        linkToAnswer.setLayoutData(gridDataUser);
        
        // launches the operating system executable associated with the file or URL
        linkToAnswer.addListener(SWT.Selection, event -> Program.launch(event.text));
        
        // add empty label to the grid
        new Label(container, SWT.NONE);

        browser = new Browser(container, SWT.BORDER);

        GridData gridDataBrowser = new GridData();
        gridDataBrowser.grabExcessVerticalSpace = true; 
        gridDataBrowser.grabExcessHorizontalSpace = true; 
        gridDataBrowser.horizontalAlignment = SWT.FILL;
        gridDataBrowser.verticalAlignment = SWT.FILL;
        browser.setLayoutData(gridDataBrowser);
        
        // add empty label to the grid
        new Label(container, SWT.NONE);

        Composite btns = new Composite(container, SWT.NONE);
        RowLayout rowLayout = new RowLayout();
        btns.setLayout(rowLayout);

        final Button previousBtn = new Button(btns, SWT.PUSH);
        final Button nextBtn = new Button(btns, SWT.PUSH);
        previousBtn.setText("Previous answer");
        nextBtn.setText("Next answer");

        Listener listener = (event) -> 
        {
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
        };

        nextBtn.addListener(SWT.Selection, listener);
        previousBtn.addListener(SWT.Selection, listener);

        setAnswerContent();
    }

    /**
     * Sets the content of the Stack Overflow answer:
     * <li> The answer itself
     * <li> The username
     * <li> The reputation
     * <li> The link to the answer
     */
    private void setAnswerContent() {
        if (this.answers == null) {
            return;
        }
        browser.setText("<html>" + answers.get(answerCount).getBody() + "</html>");        
        linkToAnswer.setText(getAnswerDetails(answers.get(answerCount)));
        linkToAnswer.update();
    }

    /**
     * Returns the link to the answer and to the user page along with his reputation and the count vote for the answer.
     * @param stackoverflowAnswer the answer that's being viewed.
     * @return The answer vote count as a link to the answer and the username with his reputation as a link to the user page.
     */
    private String getAnswerDetails(StackoverflowAnswer stackoverflowAnswer) {
        String linkToAnswer = answers.get(answerCount).getUrl();
        String voteCount = stackoverflowAnswer.getVoteCount();
        String answarLinkLbl = "<a href=\"http://" + linkToAnswer + "\" style=\"text-decoration:none\">" + voteCount + " votes</a>";
        String userLinkLbl = "<a href=\"http://" + stackoverflowAnswer.getUserUrl() + "\" style=\"text-decoration:none\">" 
                                                                                        + stackoverflowAnswer.getUser() + "</a>";

        return answarLinkLbl + ", by " + userLinkLbl + " - " + stackoverflowAnswer.getReputation();
    }

    /**
     * Sets the answer and resets <code>answerCount</code>.
     * This method MUST call `setAnswerContent` in order to update the browser.
     * @param answers the answer being viewed.
     */
    public void setAnswer(List<StackoverflowAnswer> answers) {
        this.answers = answers;
        this.answerCount = 0;
        setAnswerContent();
    }
}