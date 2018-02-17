package org.jflicks.slint;

import java.util.ArrayList;

import org.pmw.tinylog.Logger;

/**
 * This class captures the information from an otool -L OBJ call.
 *
 * @author Doug Barnum
 * @version 1.0
 */
public class OtoolInfo {

    private String id;
    private ArrayList<Entry> entryList;

    private OtoolInfo() {

        setEntryList(new ArrayList<Entry>());
    }

    public static OtoolInfo parse(String s) {

        OtoolInfo result = null;

        if (s != null) {

            String[] lines = s.split("\n");
            if ((lines != null) && (lines.length > 0)) {

                result = new OtoolInfo();
                ArrayList<Entry> l = result.getEntryList();
                for (int i = 0; i < lines.length; i++) {

                    if (i == 0) {

                        result.setId(lines[i]);

                    } else {

                        Entry entry = Entry.parse(lines[i]);
                        if (entry != null) {

                            l.add(entry);

                        } else {

                            Logger.debug("Got a null Entry, thats bad.");
                        }
                    }
                }
            }
        }

        return (result);
    }

    public String getId() {
        return (id);
    }

    private void setId(String s) {
        id = s;
    }

    public Entry[] getEntries() {

        Entry[] result = null;

        ArrayList<Entry> l = getEntryList();
        if ((l != null) && (l.size() > 0)) {

            result = l.toArray(new Entry[l.size()]);
        }

        return (result);
    }

    private ArrayList<Entry> getEntryList() {
        return (entryList);
    }

    private void setEntryList(ArrayList<Entry> l) {
        entryList = l;
    }

}
