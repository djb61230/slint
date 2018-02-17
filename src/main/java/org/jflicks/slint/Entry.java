package org.jflicks.slint;

import java.io.Serializable;

import org.pmw.tinylog.Logger;

/**
 * This class captures the notion of a shared library dependency.
 *
 * @author Doug Barnum
 * @version 1.0
 */
public class Entry implements Serializable, Comparable<Entry> {

    private String originalLocation;
    private String name;
    private String compatibility;
    private boolean leaf;

    private Entry(String originalLocation, String name, String compatibility) {

        setOriginalLocation(originalLocation);
        setName(name);
        setCompatibility(compatibility);
    }

    public static Entry parse(String s) {

        Entry result = null;

        if (s != null) {

            int index = s.indexOf("(");
            if (index != -1) {

                String tmp0 = s.substring(0, index);
                tmp0 = tmp0.trim();
                String tmp1 = s.substring(index);
                tmp1 = tmp1.trim();
                index = tmp0.lastIndexOf("/");
                if (index != -1) {

                    String loc = tmp0.substring(0, index);
                    loc = loc.trim();
                    String n = tmp0.substring(index + 1);
                    result = new Entry(loc, n, tmp1);

                } else {

                    result = new Entry("", tmp0, tmp1);
                }

            } else {

                Logger.debug("Bad Entry String <" + s + ">");
                index = s.lastIndexOf("/");
                if (index != -1) {

                    String loc = s.substring(0, index);
                    loc = loc.trim();
                    String n = s.substring(index + 1);
                    result = new Entry(loc, n, "");

                } else {

                    result = new Entry("", s, "");
                }
            }

        } else {

            Logger.debug("Bad Entry String as it is null.");
        }

        return (result);
    }

    public String getOriginalLocation() {
        return (originalLocation);
    }

    private void setOriginalLocation(String s) {
        originalLocation = s;

        if (s != null) {

            if ((s.startsWith("/System")) || (s.startsWith("/usr/lib"))) {
                setLeaf(true);
            }
        }
    }

    public String getName() {
        return (name);
    }

    private void setName(String s) {
        name = s;
    }

    public String getCompatibility() {
        return (compatibility);
    }

    private void setCompatibility(String s) {
        compatibility = s;
    }

    /**
     * A leaf in a Tree is signified if the originalLocation is a system path.
     *
     * @return True if this node is a leaf.
     */
    public boolean isLeaf() {
        return (leaf);
    }

    private void setLeaf(boolean b) {
        leaf = b;
    }

    /**
     * The standard hashcode override.
     *
     * @return An int value.
     */
    public int hashCode() {
        return (toString().hashCode());
    }

    /**
     * The equals override method.
     *
     * @param o A gven object to check.
     * @return True if the objects are equal.
     */
    public boolean equals(Object o) {

        boolean result = false;

        if (o == this) {

            result = true;

        } else if (!(o instanceof Entry)) {

            result = false;

        } else {

            Entry e = (Entry) o;
            String s = toString();
            if (s != null) {

                result = s.equals(e.toString());
            }
        }

        return (result);
    }

    /**
     * The comparable interface.
     *
     * @param e The given Entry instance to compare.
     * @throws ClassCastException on the input argument.
     * @return An int representing their "equality".
     */
    public int compareTo(Entry e) throws ClassCastException {

        int result = 0;

        if (e == null) {

            throw new NullPointerException("Given a null Entry to Compare");
        }

        if (e == this) {

            result = 0;

        } else {

            String str0 = toString();
            String str1 = e.toString();
            if ((str0 != null) && (str1 != null)) {

                result = str1.compareTo(str0);
            }
        }

        return (result);
    }

    /**
     * Override by returning the location and name properties.
     *
     * @return The location and name properties.
     */
    public String toString() {
        return (getOriginalLocation() + "/" + getName());
    }

}
