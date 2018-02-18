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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import org.jflicks.job.JobContainer;
import org.jflicks.job.JobEvent;
import org.jflicks.job.JobListener;
import org.jflicks.job.JobManager;
import org.jflicks.util.Util;

import org.apache.commons.io.FileUtils;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.ConsoleWriter;

/**
 * Command line program to do our work.
 */
public final class App {

    private static void block(JobContainer jc) {

        if (jc != null) {

            // Block until we are done.
            while (jc.isAlive()) {

                try {

                    Thread.sleep(50);

                } catch (Exception ex) {
                }
            }
        }
    }

    private static void changeToLibrary(String changeText, String path) {

        if ((changeText != null) && (path != null)) {

            InstallNameToolJob job = new InstallNameToolJob();
            job.setChangeText(changeText);
            job.setPath(path);

            JobContainer jc = JobManager.getJobContainer(job);
            jc.start();
            block(jc);
        }
    }

    private static Entry[] process(Entry entry) {

        Entry[] result = null;

        if (entry != null) {

            OtoolJob job = new OtoolJob();
            job.setPath(entry.getRealPath());
            Logger.debug(entry.getRealPath());

            JobContainer jc = JobManager.getJobContainer(job);
            jc.start();
            block(jc);

            OtoolInfo info = job.getOtoolInfo();
            if (info != null) {

                result = info.getEntries();
            }

        } else {

            Logger.debug("entry is null (bad)");
        }

        return (result);
    }

    private static void process(List<Entry> list, Entry entry) {

        System.out.print(".");
        if (entry != null) {

            OtoolJob job = new OtoolJob();
            job.setPath(entry.getRealPath());

            JobContainer jc = JobManager.getJobContainer(job);
            jc.start();
            block(jc);

            OtoolInfo info = job.getOtoolInfo();
            if (info != null) {

                Entry[] array = info.getEntries();
                if ((array != null) && (array.length > 0)) {

                    for (Entry e : array) {
                        if ((!e.isLeaf()) && (!list.contains(e))) {

                            list.add(e);
                            process(list, e);
                        }
                    }

                } else {

                    Logger.debug("No entries likely (bad)");
                }

            } else {

                Logger.debug("Null OtoolInfo (bad)");
            }

        } else {

            Logger.debug("entry is null (bad)");
        }

        System.out.print(".");
    }

    private static void usage() {

        System.out.println("usage:");
        System.out.print("\tjava -jar path/to/slint-x.x.jar -p path/to/program -o path/to/output_directory ");
        System.out.println("[-l libraryName] (default 'lib') [-d [DEBUG|OFF]] (default 'OFF') [-h (usage)]\n");
        System.out.println("\tThe program can be an executable or a shared library.");
        System.out.println("\tThe output_directory will be created if it does not exist.");
        System.out.println("\tThe output_directory/libraryName will be created if it does not exist.");
        System.out.println("\tAlso the output_directory/libraryName will be emptied if it does exist.\n");
        System.out.println("\tTurning on DEBUG will result in messages written to the console.\n");

        System.exit(0);
    }

    public static void main(String[] args) {

        String program = null;
        String output = null;
        String library = "lib";
        String debug = "OFF";

        if (Util.isMac()) {

            for (int i = 0; i < args.length; i += 2) {

                if (args[i].equalsIgnoreCase("-p")) {

                    program = args[i + 1];

                } else if (args[i].equalsIgnoreCase("-o")) {

                    output = args[i + 1];

                } else if (args[i].equalsIgnoreCase("-d")) {

                    debug = args[i + 1];

                } else if (args[i].equalsIgnoreCase("-l")) {

                    library = args[i + 1];

                } else if (args[i].equalsIgnoreCase("-h")) {

                    usage();
                }
            }

            Configurator.defaultConfig()
               .writer(new ConsoleWriter())
               .level(Level.valueOf(debug))
               .activate();

            if ((program != null) && (output != null)) {

                try {

                    boolean shouldContinue = true;
                    File libDir = null;
                    File dir = new File(output);
                    if ((dir.exists()) && (dir.isDirectory())) {

                        System.out.println("Output directory exists...");
                        libDir = new File(dir, library);
                        if ((libDir.exists()) && (libDir.isDirectory())) {

                            System.out.println("Output library directory exists...");

                        } else {

                            if (!libDir.exists()) {

                                FileUtils.forceMkdir(libDir);
                                System.out.println("Output library directory created.");

                            } else if (libDir.isFile()) {

                                shouldContinue = false;
                                System.out.println("Output library directory exists but is a file.  Quiting.");
                            }
                        }

                    } else {

                        if (!dir.exists()) {

                            FileUtils.forceMkdir(dir);
                            libDir = new File(dir, library);
                            FileUtils.forceMkdir(libDir);
                            System.out.println("Output directory and library created.");

                        } else if (dir.isFile()) {

                            shouldContinue = false;
                            System.out.println("Output directory exist but is a file.  Quiting.");
                        }
                    }

                    if (shouldContinue) {

                        long start = System.currentTimeMillis();
                        StringBuilder sb = new StringBuilder();

                        Set<PosixFilePermission> perms644 = new HashSet<PosixFilePermission>();
                        perms644.add(PosixFilePermission.OWNER_READ);
                        perms644.add(PosixFilePermission.OWNER_WRITE);
                        perms644.add(PosixFilePermission.GROUP_READ);
                        perms644.add(PosixFilePermission.OTHERS_READ);

                        FileUtils.cleanDirectory(libDir);
                        Entry root = Entry.parse(program);
                        System.out.println("Finding dependencies for " + root);
                        ArrayList<Entry> list = new ArrayList<Entry>();
                        process(list, root);
                        System.out.println(".");
                        if (list.size() > 0) {

                            System.out.println("Copying files...");
                            File froot = new File(program);
                            File fdest = new File(dir, froot.getName());
                            Files.copy(Paths.get(program), Paths.get(fdest.getPath()),
                                StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                            for (Entry e : list) {

                                File from = new File(e.getRealPath());
                                File ldest = new File(libDir, from.getName());
                                Files.copy(Paths.get(from.getPath()), Paths.get(ldest.getPath()),
                                    StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                                Files.setPosixFilePermissions(Paths.get(ldest.getPath()), perms644);
                            }

                            // First do the program.  Get it's entries.
                            File croot = new File(dir, root.getName());
                            String newPath = croot.getPath();
                            root = Entry.parse(newPath);
                            Entry[] rarray = process(root);
                            if ((rarray != null) && (rarray.length > 0)) {

                                System.out.println("First updating " + newPath + " to point to " + libDir.getPath());
                                for (Entry e : rarray) {

                                    if (!e.isLeaf()) {

                                        sb.append(" -change ");
                                        sb.append(e.toString());
                                        sb.append(" @executable_path/");
                                        sb.append(library);
                                        sb.append("/");
                                        sb.append(e.getName());
                                    }
                                }
                                changeToLibrary(sb.toString().trim(), newPath);

                                // Now to process the files in the library directory.
                                int count = 0;
                                Iterator<File> iterator = FileUtils.iterateFiles(libDir, null, false);
                                while (iterator.hasNext()) {
                                    count++;
                                    iterator.next();
                                }
                                iterator = FileUtils.iterateFiles(libDir, null, false);
                                int index = 1;
                                while (iterator.hasNext()) {

                                    File lfile = iterator.next();
                                    newPath = lfile.getPath();
                                    Entry lentry = Entry.parse(newPath);
                                    Entry[] larray = process(lentry);
                                    if ((larray != null) && (larray.length > 0)) {

                                        System.out.println("Updating " + newPath + " pointing to co-located lib("
                                            + index++ + " of " + count + ").");
                                        sb.setLength(0);
                                        for (Entry e : larray) {

                                            if (!e.isLeaf()) {

                                                sb.append(" -change ");
                                                sb.append(e.toString());
                                                sb.append(" @loader_path/");
                                                sb.append(e.getName());
                                            }
                                        }
                                        changeToLibrary(sb.toString().trim(), newPath);
                                    }
                                }

                                long now = System.currentTimeMillis();
                                System.out.println("Took " + ((now - start) / 1000) + " seconds.");

                            } else {

                                System.out.println("No entries to change for " + newPath);
                            }
                        }
                    }

                } catch (IOException ex) {

                    System.out.println("Error " + ex.getMessage() + " Quitting.");
                }

            } else {

                if (program == null) {

                    System.out.println("No program given to work on.");

                } else if (output == null) {

                    System.out.println("No output directory given to put files.");
                }
                usage();
            }

        } else {

            System.out.println("Sorry just supporting the Mac at this time.");
        }
    }

}
