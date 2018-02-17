package org.jflicks;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jflicks.job.JobContainer;
import org.jflicks.job.JobEvent;
import org.jflicks.job.JobListener;
import org.jflicks.job.JobManager;
import org.jflicks.slint.Entry;
import org.jflicks.slint.OtoolInfo;
import org.jflicks.slint.OtoolJob;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName ) {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {

        System.out.println("start");
        final OtoolJob job = new OtoolJob();
        job.setPath("/Users/djb/Comskip/comskip");
        job.addJobListener(new JobListener() {
            public void jobUpdate(JobEvent event) {

                if (event.getType() == JobEvent.COMPLETE) {

                    OtoolInfo info = job.getOtoolInfo();
                    if (info != null) {

                        System.out.println(info.getId());
                        Entry[] array = info.getEntries();
                        if ((array != null) && (array.length > 0)) {

                            for (Entry e : array) {
                                System.out.println(e.toString());
                            }
                        }
                    }
                }
            }
        });
        JobContainer jc = JobManager.getJobContainer(job);
        jc.start();

        while (jc.isAlive()) {

            try {

                Thread.sleep(1000);

            } catch (Exception ex) {
            }
        }

        System.out.println("end");
        assertTrue(true);
    }

}
