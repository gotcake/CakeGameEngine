/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.io;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.cake.game.exception.GameException;

/**
 * A class containing a basic external data storage method useful for settings and properties.
 * Currently supports the ini and properties file types.
 * @author Aaron Cake
 */
public class Settings {

    private static Map<String, iDataProvider> dataProviders = new HashMap<>();

    public static iDataProvider getDataProvider(String ext) {
        iDataProvider dp = dataProviders.get(ext);
        if (dp == null)
            throw new GameException("iDataProvider not defined for extension: '" + ext + "'.");
        return dp;
    }

    public static void registerDataProvider(iDataProvider dp) {
        for (String ext: dp.getExtensions()) {
            dataProviders.put(ext, dp);
        }
    }

    static {
        registerDataProvider(new INIDataProvider());
        registerDataProvider(new PropertiesDataProvider());
    }

    private String home;
    private Map<String, String> values;
    private iResource resource;

    public Settings(String ref) {
        values = new HashMap<>();
        load(ref);
    }

    public Settings(iResource resource) {
        values = new HashMap<>();
        load(resource);
    }

    public Settings() {
        values = new HashMap<>();
    }

    public Settings(Map<String, String> data) {
        values = data;
    }

    public void load(String ref) {
        this.home = "";
        resource = ResourceManager.getDefault().getResource(ref);
        if (values != null) {
            values.clear();
        }
        if (resource.exists()) {
            values = getDataProvider(resource.getExtension()).read(resource, values);
        }
    }

    public void load(iResource resource) {
        this.home = "";
        this.resource = resource;
        if (values != null) {
            values.clear();
        }
        if (resource.exists()) {
            values = getDataProvider(resource.getExtension()).read(resource, values);
        }
    }

    public void setMap(Map<String, String> data) {
        this.values = data;
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public Map<String, String> getMap() {
        return values;
    }

    private Settings(String home, Map<String, String> values, iResource resource) {
        this.home = home;
        this.values = values;
    }

    public String get(String name) {
        return values.get(home + name);
    }

    public String get(String name, int index) {
        return values.get(home + name + "[" + index + "]");
    }

    public double getNumber(String name, int index) {
        return Double.parseDouble(values.get(home + name + "[" + index + "]"));
    }

    public double getNumber(String name) {
        return Double.parseDouble(values.get(home + name));
    }

    public String get(String name, String def) {
        String s = values.get(home + name);
        if (s == null)
            return def;
        return s;
    }

    public double getNumber(String name, double def) {
        String s = values.get(home + name);
        if (s == null)
            return def;
        return Double.parseDouble(s);
    }

    public String get(String name, int index, String def) {
        String s = values.get(home + name + "[" + index + "]");
        if (s == null)
            return def;
        return s;
    }

    public double getNumber(String name, int index, double def) {
        String s = values.get(home + name + "[" + index + "]");
        if (s == null)
            return def;
        return Double.parseDouble(s);
    }

    public Settings go(String name) {
        return new Settings(home + name + ".", values, resource);
    }

    public void set(String name, String value) {
        values.put(home + name, value);
    }

    public void set(String name, long value) {
        values.put(home + name, String.valueOf(value));
    }

    public void set(String name, double value) {
        values.put(home + name, String.valueOf(value));
    }

    public void set(String name, int index, String value) {
        values.put(home + name + "[" + index + "]", value);
    }

    public void set(String name, int index, long value) {
        values.put(home + name + "[" + index + "]", String.valueOf(value));
    }

    public void set(String name, int index, double value) {
        values.put(home + name + "[" + index + "]", String.valueOf(value));
    }

    public void merge(Settings other, String node) {
        for (Entry<String, String> s: other.getMap().entrySet()) {
            values.put(node + "." + s.getKey(), s.getValue());
        }
    }

    public Settings copy() {
        Map<String, String> map = new HashMap<>();
        map.putAll(values);
        return new Settings(home, map, resource);
    }

    public void merge(Settings other, boolean override) {
        for (Entry<String, String> s: other.getMap().entrySet()) {
            if (override || !values.containsKey(s.getKey()))
                values.put(s.getKey(), s.getValue());
        }
    }

    public boolean has(String name) {
        return values.containsKey(name);
    }

    public boolean has(String name, int index) {
        return values.containsKey(name + "[" + index + "]");
    }

    public void save() {
        getDataProvider(resource.getExtension()).write(resource, values);
    }

    public void save(String ref) {
        iResource r = ResourceManager.getDefault().getResource(ref);
        getDataProvider(r.getExtension()).write(r, values);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Settings[ home=\"").append(home).append("\"\n");
        for (Entry<String, String> entry: values.entrySet()) {
            sb.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"\n");
        }
        sb.append(']');
        return sb.toString();
    }
}
