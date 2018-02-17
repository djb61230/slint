/*
    This file is part of JFLICKS.

    JFLICKS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JFLICKS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JFLICKS.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.jflicks.slint;

import org.jflicks.job.AbstractJob;
import org.jflicks.job.JobContainer;
import org.jflicks.job.JobListener;
import org.jflicks.job.SystemJob;

/**
 * Simple base class to contain properties that extensions will need.
 *
 * @author Doug Barnum
 * @version 1.0
 */
public abstract class BaseSlintJob extends AbstractJob implements JobListener {

    private SystemJob systemJob;
    private JobContainer jobContainer;

    /**
     * Simple no argument constructor.
     */
    public BaseSlintJob() {
    }

    /**
     * An instance of SystemJob is used to run a command line program.
     *
     * @return A SystemJob instance.
     */
    protected SystemJob getSystemJob() {
        return (systemJob);
    }

    /**
     * An instance of SystemJob is used to run a command line program.
     *
     * @param j A SystemJob instance.
     */
    protected void setSystemJob(SystemJob j) {
        systemJob = j;
    }

    /**
     * An instance of JobContainer is needed to run a SystemJob.
     *
     * @return A JobContainer instance.
     */
    protected JobContainer getJobContainer() {
        return (jobContainer);
    }

    /**
     * An instance of JobContainer is needed to run a SystemJob.
     *
     * @param j A JobContainer instance.
     */
    protected void setJobContainer(JobContainer j) {
        jobContainer = j;
    }

}
