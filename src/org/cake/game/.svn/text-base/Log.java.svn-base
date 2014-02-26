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

package org.cake.game;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.cake.game.io.iResource;

/**
 * A basic log implementation using various levels of importance.
 * @author Aaron Cake
 */
public class Log {
    
    private static Log defaultLog = null;
    private static SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
    
    public static final int IGNORE_NONE = 0;
    public static final int IGNORE_DEBUG_VERBOSE = 1;
    public static final int IGNORE_DEBUG = 2;
    public static final int IGNORE_INFO = 3;
    public static final int IGNORE_WARNING = 4;
    public static final int IGNORE_ERROR_STACK_TRACE = 5;
    public static final int IGNORE_ALL = 6;
    
    public static Log getDefault() {
        if (defaultLog == null)
            defaultLog = new Log(System.out, System.err);
        return defaultLog;
    }
    
    private PrintStream out, err;
    private int ignoreLevel;
    
    public Log(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
        ignoreLevel = IGNORE_NONE;
    }
    
    public Log(PrintStream ps) {
        this.out = ps;
        this.err = ps;
        ignoreLevel = IGNORE_NONE;
    }
    
    public Log(iResource r) {
        PrintStream ps = new PrintStream(r.openWrite());
        this.out = ps;
        this.err = ps;
        ignoreLevel = IGNORE_NONE;
    }
    
    public void setIgnoreLevel(int level) {
        ignoreLevel = level;
    }
    
    public int getIgnoreLevel() {
        return ignoreLevel;
    }
    
    public void verbose(Object msg) {
        if (ignoreLevel < IGNORE_DEBUG_VERBOSE) {
            out.println("(" + getTimeString() + ")[DEBUG]:" + msg);
        }
    }
    
    public void debug(Object msg) {
        if (ignoreLevel < IGNORE_DEBUG) {
            out.println("(" + getTimeString() + ")[DEBUG]:" + msg);
        }
    }
    
    public void info(Object msg) {
        if (ignoreLevel < IGNORE_INFO) {
            out.println("(" + getTimeString() + ")[INFO]:" + msg);
        }
    }
    
    public void warning(Object msg) {
        if (ignoreLevel < IGNORE_WARNING) {
            out.println("(" + getTimeString() + ")[WARNING]:" + msg);
        }
    }
    
    public void error(Exception e) {
        if (ignoreLevel < IGNORE_ALL) {
            out.println("(" + getTimeString() + ")[ERROR]:" + e.getMessage());
            if (ignoreLevel < IGNORE_ERROR_STACK_TRACE) {
                e.printStackTrace(err);
            }
        }
        if (ignoreLevel >= IGNORE_ERROR_STACK_TRACE || err != System.err) {
            e.printStackTrace(System.err);
        }
    }
    
    private String getTimeString() {
        return fmt.format(new Date());
    }
    
}
