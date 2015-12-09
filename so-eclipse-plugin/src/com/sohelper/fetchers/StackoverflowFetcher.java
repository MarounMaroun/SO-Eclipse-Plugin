package com.sohelper.fetchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.sohelper.datatypes.GoogleResult;
import com.sohelper.datatypes.StackoverflowAnswer;
import com.sohelper.datatypes.StackoverflowPost;
import com.sohelper.ui.QuestionPage;

/**
 * Fetches Stack Overflow posts from <code>GoogleResult</code>. 
 * 
 * @author maroun
 *
 */
public class StackoverflowFetcher {
	
	/**
	 * Gets Stack Overflow posts from given <code>GoogleResult</code> object.
	 * 
	 * @param googleResults List of <code>GoogleResult</code>.
	 * @param monitor 
	 * @return List of Stack Overflow posts.
	 * @throws IOException If any I/O exception occurs.
	 */
	public static List<StackoverflowPost> getStackoverflowPosts(List<GoogleResult> googleResults, IProgressMonitor monitor) throws IOException {
		List<StackoverflowPost> stackoverflowPosts = new ArrayList<>();

		int size = googleResults.size();
		
		for (int i=0; i < size; i++) {
			monitor.worked(60 / size);
			stackoverflowPosts.add(new StackoverflowPost(googleResults.get(i).getUrl().toString()));
		}
		return stackoverflowPosts;
	}

	/**
	 * Gets Stack Overflow answers from a given <code>StackoverflowPost</code> object.
	 * @param stackoverflowPosts List of Stack Overflow posts.
	 * @return List of Stack Overflow answers.
	 */
	public static List<StackoverflowAnswer> getStackoverflowAnswers(List<StackoverflowPost> stackoverflowPosts, IProgressMonitor monitor, QuestionPage qp) {
		List<StackoverflowAnswer> stackoverflowAnswers = new ArrayList<>();
		List<StackoverflowAnswer> finalAnswerList = new ArrayList<>();
		
		int size = stackoverflowPosts.size();
		
		for (int i=0; i < stackoverflowPosts.size(); i++) {
			monitor.worked(10 / size);
			for (StackoverflowAnswer answer : stackoverflowPosts.get(i).getAnswers()) {
				if (answer.getUrl() == null){
					continue;
				}
				stackoverflowAnswers.add(answer);
			}
		}
		/**
		 * 1. First get all answers. 
		 * 2. Get user's preference from UI.
		 * 3. Filter answers accordingly.
		 */
		if(qp.isAcceptedOnly() && qp.isUpVotedOnly()){
			for (StackoverflowAnswer stackoverflowAnswer : stackoverflowAnswers) {
					if(stackoverflowAnswer.isAccepted() && stackoverflowAnswer.isUpVoted()){
						finalAnswerList.add(stackoverflowAnswer);
					}
			}
			return finalAnswerList;
		}else if(qp.isAcceptedOnly()){
			for (StackoverflowAnswer stackoverflowAnswer : stackoverflowAnswers) {
				if(stackoverflowAnswer.isAccepted()){
					finalAnswerList.add(stackoverflowAnswer);
				}
			}
			return finalAnswerList;
		}else if(qp.isUpVotedOnly()){
			for (StackoverflowAnswer stackoverflowAnswer : stackoverflowAnswers) {
				if(stackoverflowAnswer.isUpVoted()){
					finalAnswerList.add(stackoverflowAnswer);
				}
			}
			return finalAnswerList;
		}else{
			return stackoverflowAnswers;
		}
	}
}