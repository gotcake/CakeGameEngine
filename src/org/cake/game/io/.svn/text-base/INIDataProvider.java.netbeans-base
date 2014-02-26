/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.io;

import java.io.PrintStream;
import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An INI file data provider
 * @author Aaron Cake
 */
public class INIDataProvider implements iDataProvider{

    public static Map<String, String> parse(iResource res) {
        return parse(res, null);
    }

    public static Map<String, String> parse(iResource res, Map<String, String> data) {
        Pattern sectionPattern = Pattern.compile("\\s*\\[([\\w\\d_\\.#%]+)\\]\\s*?(;.*)?");
        Pattern iniPattern = Pattern.compile("\\s*([\\w\\d_\\.]+?[\\w\\d_\\.\\[\\]]*)\\s*=\\s*(.*?)\\s*?(;.*)?");
        if (data == null)
            data = new HashMap<>();
        Scanner in = new Scanner(res.openRead());
        String section = "";
        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.isEmpty() || line.matches("\\s*;.*")) {
                // ignore
            } else {
                Matcher m = sectionPattern.matcher(line);
                if (m.matches()) {
                    if (m.group(1).equals("__default__")) {
                        section = "";
                    } else {
                        section = m.group(1) + ".";
                    }

                } else {
                    m = iniPattern.matcher(line);
                    if (m.matches()) {
                        data.put(section + m.group(1), m.group(2));
                    }
                }
            }
        }
        return data;
    }

    public static void save(iResource res, Map<String, String> data) {
        PrintStream out = new PrintStream(res.openWrite());
        out.println("; " + DateFormat.getDateTimeInstance().format(new Date()));
        List<Map.Entry<String, String>> entries = new ArrayList<>();
        entries.addAll(data.entrySet());
        List<String> prefixes = new ArrayList<>();
        for (Map.Entry<String, String> e: entries) {
            int i = e.getKey().indexOf(".");
            if (i == -1) continue;
            String prefix = e.getKey().substring(0, i);
            if (!prefixes.contains(prefix) && !prefix.contains("[") && !prefix.contains("]"))
                prefixes.add(prefix);
        }
        List<Map.Entry<String, String>> toRemove;
        while (!prefixes.isEmpty() && !entries.isEmpty()) {
            String prefix = prefixes.remove(0);
            out.println("[" + prefix + "]");
            toRemove = new ArrayList<>();
            for (Map.Entry<String, String> e: entries) {
                if (e.getKey().startsWith(prefix + ".")) {
                    toRemove.add(e);
                    String name = e.getKey().substring(prefix.length() + 1);
                    out.println(name + "=" + e.getValue());
                }
            }
            entries.removeAll(toRemove);
        }
        if (!entries.isEmpty()) {
            out.println("[__default__]");
        }
        for (Map.Entry<String, String> e: entries) {
            out.println(e.getKey() + "=" + e.getValue());
        }
    }


    @Override
    public Map<String, String> read(iResource res, Map<String, String> data) {
        return parse(res, data);
    }


    @Override
    public void write(iResource res, Map<String, String> data) {
        save(res, data);
    }

    @Override
    public String[] getExtensions() {
        return new String[] {"ini"};
    }

}
