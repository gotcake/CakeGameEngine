/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.game.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.cake.game.exception.ResourceException;

/**
 *
 * @author Aaron
 */
public class PropertiesDataProvider implements iDataProvider {

    public static Map<String, String> parse(iResource res) {
        return parse(res, null);
    }

    public static Map<String, String> parse(iResource res, Map<String, String> data) {
        try {
            if (data == null)
                data = new HashMap<>();
            Properties p = new Properties();
            p.load(res.openRead());
            for (String s: p.stringPropertyNames()) {
                data.put(s, p.getProperty(s));
            }
            return data;
        } catch (IOException ex) {
            throw new ResourceException(ex);
        }
    }

    public static void save(iResource res, Map<String, String> data) {
        Properties p = new Properties();
        for (Map.Entry<String, String> e: data.entrySet()) {
            p.put(e.getKey(), e.getValue());
        }
        try {
            p.store(res.openWrite(), "Generated Property List");
        } catch (IOException ex) {
            throw new ResourceException(ex);
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
        return new String[] {
            "properties", "prop", "props"
        };
    }
}
