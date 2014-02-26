/*
    This file is part of CakeGame engine.

    CakeGame engine is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    CakeGame engine is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CakeGame engine.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.cake.game.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cake.game.exception.ResourceException;

/**
 * Handles loading of all external resources. Delegates to it's resource locations to produce the actual resources.
 * @author Aaron Cake
 */
public class ResourceManager {

    private static ResourceManager defaultt = null;

    public static ResourceManager getDefault() {
        if (defaultt == null) {
            defaultt = new ResourceManager();
            defaultt.addLocation(new FileSystemResourceLocator());
            defaultt.addLocation(new URLResourceLocator());
            defaultt.addLocation(new ClassLoaderResourceLocator());
        }
        return defaultt;
    }

    public static void setDefault(ResourceManager rm) {
        defaultt = rm;
    }

    private List<iResourceLocator> locations;
    private Map<String, String> nameMap;

    /**
     * Create a new ResourceManager with no resource locations. (will not find any resources)
     */
    public ResourceManager() {
        locations = new ArrayList<>();
        nameMap = new HashMap<>();
    }

    /**
     * Loads a map containing name to resource location strings.
     * @param nameMap
     */
    public void loadResourceMap(Map<String, String> resourceMap) {
        this.nameMap = resourceMap;
    }

    /**
     * Use a Settings to map strings to file names.
     * @param nameMap
     */
    public void loadResourceMap(Settings resourceMap) {
        this.nameMap = resourceMap.getMap();
    }

    /**
     * Load a resource map from a file.
     * @param nameMap
     */
    public void loadResourceMap(String location) {
        iResource res = getResource(location);
        this.nameMap = Settings.getDataProvider(res.getExtension()).read(res, null);
    }

    /**
     * Adds a single name to resource location mapping
     * @param name
     * @param location
     */
    public void addMapping(String name, String location) {
        nameMap.put(name, location);
    }

    /**
     * Adds a resource location under the given name
     * @param locationName
     * @param loc
     */
    public void addLocation(iResourceLocator loc) {
        locations.add(loc);
    }

    /**
     * Attempts to get a resource. Grabs the first existing resource, and if it fails, grabs the first resource than can be written to. If the world goes to shit throws an error.
     * @param name
     * @return
     */
    public iResource getResource(String name) {
        if (nameMap.containsKey(name)) {
            name = nameMap.get(name);
        }
        for (iResourceLocator rl : locations) {
            iResource r = rl.getResource(name);
            if (r != null && r.exists()) {
                return r;
            }
        }
        for (iResourceLocator rl : locations) {
            if (rl.isWriteable()) {
                iResource r = rl.getResource(name);
                if (r != null) {
                    return r;
                }
            }
        }
        throw ResourceException.notFound(name);
    }

    /**
     * Checks the name against the resource map and returns the
     * mapped string, or the name if it does not exist in the map.
     * @param name a resource reference string or name
     * @return the original name or mapped reference string
     */
    public String mapName(String name) {
        if (nameMap.containsKey(name)) {
            name = nameMap.get(name);
        }
        return name;
    }
}
