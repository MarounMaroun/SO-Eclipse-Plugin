package so.basic.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;

import com.sohelper.datatypes.GoogleResult;
import com.sohelper.datatypes.StackoverflowAnswer;
import com.sohelper.datatypes.StackoverflowPost;
import com.sohelper.fetchers.GoogleFetcher;


public class BasicTest {

    private static final String GOOGLE_QUERY = "how to read file line by line in java";
    private static final String STACK_OVERFLOW_LINK = "https://stackoverflow.com/questions/409784/whats-the-simplest-way-to-print-a-java-array";

    @Test
    public void testGoogleFetcher() {
        List<GoogleResult> googleResults = GoogleFetcher.getGoogleResults(GOOGLE_QUERY, new NullProgressMonitor());
        for (GoogleResult res : googleResults) {
            assertThat(res.getUrl().toString(), containsString("stackoverflow.com"));
        }
    }
    
    @Test
    public void testStackoverflowFetcher() throws IOException {
        StackoverflowPost soPost = new StackoverflowPost(STACK_OVERFLOW_LINK);
        List<StackoverflowAnswer> answers = soPost.getAnswers();
        assertTrue(answers.size() > 1);

        for (StackoverflowAnswer answer : answers) {
            assertFalse(answer.getBody().isEmpty());
            assertFalse(answer.getUser().isEmpty());
            assertFalse(answer.getReputation().isEmpty());
            assertFalse(answer.getUrl().isEmpty());
            assertFalse(answer.getUserUrl().isEmpty());
            String voteCount = null;
            try {
                voteCount = answer.getVoteCount();
                Integer.valueOf(voteCount);
            } catch (Exception e) {
                fail("Got unexpected vote: " + voteCount);
            }
        }
    }    
    
}
