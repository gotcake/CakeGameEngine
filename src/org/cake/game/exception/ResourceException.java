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

package org.cake.game.exception;

import org.cake.game.io.iResource;

/**
 * An exception for resources (files etc.)
 * @author Aaron Cake
 */
public class ResourceException extends GameException {
    
    /**
     * Creates a ResourceException for when a resource cannot be written to.
     * @param resource the resource
     * @return a ResourceException describing the problem.
     */
    public static ResourceException writeFail(iResource resource) {
        String msg = "Failed to write to the " + resource.toString();
        if (!resource.canWrite()) {
            msg += " The specified resource is not writeable.";
        }
        return new ResourceException(msg);
    }
    
    /**
     * Creates a ResourceException for when a resource cannot be read.
     * @param resource the resource
     * @return a ResourceException describing the problem.
     */
    public static ResourceException readFail(iResource resource) {
        String msg = "Failed to read from the " + resource.toString();
        if (!resource.exists()) {
            msg += " The specified resource does not exist.";
        }
        if (!resource.canRead()) {
            msg += " The specified resource is not readable.";
        }
        return new ResourceException(msg);
    }
    
    /**
     * Creates a ResourceException for when a resource cannot be deleted.
     * @param resource the resource
     * @return a ResourceException describing the problem.
     */
    public static ResourceException deleteFail(iResource resource) {
        String msg = "Failed to delete the " + resource.getClass().getSimpleName() + " resource at '" + resource.getLocation()+"'";
        if (!resource.exists()) {
            msg += " The specified resource does not exist.";
        }
        if (!resource.canWrite()) {
            msg += " The specified resource is not deleteable.";
        }
        return new ResourceException(msg);
    }
    
    /**
     * Creates a ResourceException for when a resource cannot be found.
     * @param ref the name of the resource
     * @return a ResourceException describing the problem.
     */
    public static ResourceException notFound(String ref) {
        return new ResourceException("The specified resource '" + ref + "' could not be resolved.");
    }
    
    /**
     * Creates a ResourceException for when a string with the invalid format is used for a resource.
     * @param ref the name of the resource
     * @return a ResourceException describing the problem.
     */
    public static ResourceException invalidFormat(String ref) {
        return new ResourceException("The specified resource string '" + ref + "' is an invlid format.");
    }
    
    /**
     * Creates a ResourceException with the basic text "A ResourceException occurred."
     */
    public ResourceException() {
        super("A ResourceException occurred.");
    }
    
    /**
     * Creates a ResourceException with your message
     */
    public ResourceException(String msg) {
        super(msg);
    }
    
    /**
     * Creates a ResourceException with your message and cause
     */
    public ResourceException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    /**
     * Creates a ResourceException with your cause
     */
    public ResourceException(Throwable cause) {
        super(cause);
    }
    
}
