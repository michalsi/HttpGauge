import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;


public class TimerTest {

    @Test
    public void ShouldReturnTimeGreaterOrEqualToSleepTime() throws InterruptedException {
        long sleepTime = 1000L;
        Timer timer = new Timer();

        timer.start();
        Thread.sleep(sleepTime);
        timer.stop();
        assertThat("Elapsed time calculated",
                timer.getElapsedTimeInMilliseconds().intValue(), greaterThanOrEqualTo((int) sleepTime));
    }

    @Test
    public void ShouldReturnErrorWhenNoStartTimeProvided() {
        //TODO
    }

    @Test
    public void ShouldReturnErrorWhenNoStopTimeProvided() {
        //TODO
    }

}