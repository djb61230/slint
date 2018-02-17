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
package org.jflicks.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

/**
 * Some very basic methods that capture some common tasks.  Implemented here
 * so other classes can reduce duplicated code.
 *
 * @author Doug Barnum
 * @version 1.0
 */
public final class Util {

    /**
     * Default empty constructor.
     */
    private Util() {
    }

    /**
     * Parse a string and determine it's integer value.  If there is a
     * problem then return a default value.
     *
     * @param s A given string to parse.
     * @param defaultValue A default value to return on any problem.
     * @return The value of the parsed string.
     */
    public static int str2int(String s, int defaultValue) {

        int result = defaultValue;

        if (s != null) {

            try {

                result = Integer.parseInt(s);

            } catch (Exception ex) {

                result = defaultValue;
            }
        }

        return (result);
    }

    /**
     * Parse a string and determine it's long value.  If there is a
     * problem then return a default value.
     *
     * @param s A given string to parse.
     * @param defaultValue A default value to return on any problem.
     * @return The value of the parsed string.
     */
    public static long str2long(String s, long defaultValue) {

        long result = defaultValue;

        if (s != null) {

            try {

                result = Long.parseLong(s);

            } catch (Exception ex) {

                result = defaultValue;
            }
        }

        return (result);
    }

    /**
     * Parse a string and determine it's double value.  If there is a
     * problem then return a default value.
     *
     * @param s A given string to parse.
     * @param defaultValue A default value to return on any problem.
     * @return The value of the parsed string.
     */
    public static double str2double(String s, double defaultValue) {

        double result = defaultValue;

        if (s != null) {

            try {

                result = Double.parseDouble(s);

            } catch (Exception ex) {

                result = defaultValue;
            }
        }

        return (result);
    }

    /**
     * Parse a string and determine it's boolean value.  If there is a
     * problem then return a default value.
     *
     * @param s A given string to parse.
     * @param defaultValue A default value to return on any problem.
     * @return The value of the parsed string.
     */
    public static boolean str2boolean(String s, boolean defaultValue) {

        boolean result = defaultValue;

        if (s != null) {

            try {

                result = Boolean.parseBoolean(s);

            } catch (Exception ex) {

                result = defaultValue;
            }
        }

        return (result);
    }

    /**
     * Using the system properties determine if we are running on Windows.
     *
     * @return True if we are on Windows.
     */
    public static boolean isWindows() {
        return (isOsName("Windows"));
    }

    /**
     * Using the system properties determine if we are running on Mac.
     *
     * @return True if we are on Mac.
     */
    public static boolean isMac() {
        return (isOsName("Mac"));
    }

    /**
     * Using the system properties determine if we are running on Linux.
     *
     * @return True if we are on Linux.
     */
    public static boolean isLinux() {
        return (isOsName("Linux"));
    }

    private static boolean isOsName(String name) {

        boolean result = false;

        Properties p = System.getProperties();
        if (p != null) {

            String s = p.getProperty("os.name");
            if (s != null) {

                result = s.startsWith(name);
            }
        }

        return (result);
    }

    /**
     * A nice method that will load a properties file for a given object.
     * The properties file is assumed to be in the same package location
     * as the object.  This makes for reading properties files that are
     * located in jar files easy.  Handy for runtime read-only properties.
     *
     * @param o A given object.
     * @param s A properties file name.
     * @return An instantiated Properties file.
     */
    public static Properties findProperties(Object o, String s) {

        Properties result = new Properties();

        if ((o != null) && (s != null)) {

            URL url = o.getClass().getResource(s);
            try {

                InputStream is = url.openStream();
                result.load(is);
                is.close();

            } catch (Exception ex) {

                result = null;
            }
        }

        return (result);
    }

    /**
     * Simple method to load a Properties object from a file path.
     *
     * @param path The given File as a String path to read.
     * @return A Properties instance.
     */
    public static Properties findProperties(String path) {
        return findProperties(new File(path));
    }

    /**
     * Simple method to load a Properties object from a File.
     *
     * @param file The given File to read.
     * @return A Properties instance.
     */
    public static Properties findProperties(File file) {

        Properties result = null;

        if (file != null) {

            result = new Properties();
            FileInputStream fis = null;
            try {

                fis = new FileInputStream(file);
                result.load(fis);
                fis.close();

            } catch (IOException ex) {

                result = null;

            } finally {

                if (fis != null) {

                    try {

                        fis.close();

                    } catch (IOException ex) {

                        fis = null;
                    }
                }
            }
        }

        return (result);
    }

    /**
     * Write out our properties with the tags in alpha order.
     *
     * @param f A File to write - will clobber so be careful.
     * @param p The Properties object to write.
     */
    public static void writeProperties(File f, Properties p) {

        if ((f != null) && (p != null)) {

            Set<String> set = p.stringPropertyNames();
            String[] array = set.toArray(new String[set.size()]);
            Arrays.sort(array);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; i++) {

                String value = p.getProperty(array[i]);
                if (value == null) {

                    value = "";
                }

                sb.append(array[i] + "=" + value + "\n");
            }

            try {

                Util.writeTextFile(f, sb.toString());

            } catch (IOException ex) {

                ex.printStackTrace();
            }
        }
    }

    /**
     * Read the entire file into a byte array.
     *
     * @param file The file to read.
     * @return The file contents as a byte array.
     */
    public static byte[] read(File file) {

        byte[] result = null;

        if ((file != null) && (file.exists()) && (file.isFile())) {

            FileInputStream fis = null;
            DataInputStream dis = null;

            try {

                int offset = 0;
                int count = 0;
                int total = (int) file.length();
                result = new byte[total];
                fis = new FileInputStream(file);
                dis = new DataInputStream(fis);
                dis.readFully(result);
                dis.close();
                fis.close();
                fis = null;

            } catch (IOException ex) {
            } finally {

                if (fis != null) {

                    try {

                        dis.close();
                        fis.close();

                    } catch (IOException ex) {

                        dis = null;
                        fis = null;
                    }
                }
            }
        }

        return (result);
    }

    /**
     * Read a text file into a String array object.
     *
     * @param file File object representing a text file.
     * @return The file read into a String array object.
     */
    public static String[] readTextFile(File file) {

        String[] result = null;

        ArrayList<String> work = new ArrayList<String>();
        if (file != null) {

            BufferedReader in = null;

            try {

                in = new BufferedReader(new FileReader(file));
                String line = null;
                while ((line = in.readLine()) != null) {
                    work.add(line);
                }

                result = (String[]) work.toArray(new String[work.size()]);
                in.close();
                in = null;

            } catch (IOException e) {

                result = null;

            } finally {

                if (in != null) {

                    try {

                        in.close();

                    } catch (IOException ex) {

                        throw new RuntimeException(ex);
                    }
                }
            }
        }

        return (result);
    }

    /**
     * Write a text file from a String object.
     *
     * @param f File object representing a text file.
     * @param data The data to write.
     * @throws IOException on an error.
     */
    public static void writeTextFile(File f, String data) throws IOException {

        if ((f != null) && (data != null)) {

            FileOutputStream fos = null;
            try {

                fos = new FileOutputStream(f);
                fos.write(data.getBytes());

            } finally {

                if (fos != null) {

                    fos.close();
                }
            }
        }
    }

    /**
     * Checks the to see if the 2 Strings are equal, or both are null.
     *
     * @param one String one to compare.
     * @param two String two to compare.
     * @return True if both Strings are null, else returns String.equals();
     */
    public static boolean equalOrNull(Object one, Object two) {

        boolean result = false;

        if ((one == null) && (two == null)) {

            result = true;

        } else if ((one == null) || (two == null)) {

            result = false;

        } else {

            result = one.equals(two);
        }

        return (result);
    }

}

