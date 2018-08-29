import org.junit.Test;

import sm.com.javajoker.Joker;
import sm.com.javajoker.model.JokeData;
import sm.com.javajoker.repo.ichdj.IchdjDownloadSeed;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class JokerTest {
    @Test
    public void nextJoke_HasData() {
        Joker jokeSource = new Joker();

        JokeData testJokeData = jokeSource.nextJoke(new IchdjDownloadSeed());

        assertTrue(testJokeData.jokeBody.length() > 0);

        assertThat(testJokeData.statusCode, not(JokeData.STATUS_ERROR));

        System.out.println(testJokeData.toString());
    }
}
