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

import org.jflicks.job.JobContainer;
import org.jflicks.job.JobEvent;
import org.jflicks.job.JobManager;
import org.jflicks.job.SystemJob;

import org.pmw.tinylog.Logger;

/**
 * This class will call install_name_tool -change OLD_SETTING NEW_SETTING on a file.
 *
 * @author Doug Barnum
 * @version 1.0
 */
public class InstallNameToolJob extends BaseSlintJob {

    private String oldSetting;
    private String newSetting;
    private String path;

    /**
     * Simple no argument constructor.
     */
    public InstallNameToolJob() {
    }

    public String getOldSetting() {
        return (oldSetting);
    }

    public void setOldSetting(String s) {
        oldSetting = s;
    }

    public String getNewSetting() {
        return (newSetting);
    }

    public void setNewSetting(String s) {
        newSetting = s;
    }

    public String getPath() {
        return (path);
    }

    public void setPath(String s) {
        path = s;
    }

    /**
     * {@inheritDoc}
     */
    public void start() {

        setTerminate(false);
    }

    /**
     * {@inheritDoc}
     */
    public void run() {

        SystemJob job = SystemJob.getInstance("install_name_tool -change "
            + getOldSetting() + " " + getNewSetting() + " " + getPath());
        fireJobEvent(JobEvent.UPDATE, "command: <" + job.getCommand() + ">");
        setSystemJob(job);
        job.addJobListener(this);
        JobContainer jc = JobManager.getJobContainer(job);
        setJobContainer(jc);
        jc.start();

        while (!isTerminate()) {

            JobManager.sleep(getSleepTime());
        }

        fireJobEvent(JobEvent.COMPLETE);
    }

    /**
     * {@inheritDoc}
     */
    public void stop() {

        setTerminate(true);
        JobContainer jc = getJobContainer();
        if (jc != null) {

            jc.stop();
            setJobContainer(null);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void jobUpdate(JobEvent event) {

        if (event.getType() == JobEvent.COMPLETE) {

            SystemJob job = getSystemJob();
            if (job != null) {

                fireJobEvent(JobEvent.UPDATE, "InstallNameToolJob: exit: " + job.getExitValue());
                stop();
            }
        }
    }

}
