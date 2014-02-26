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
package org.cake.game.input;

import org.cake.game.Game;

/**
 * A listener for key events in game.
 * @author Aaron Cake
 */
public interface iGameKeyListener {

    public void keyTyped(Game g, GameKeyEvent e);

    public void keyPressed(Game g, GameKeyEvent e);

    public void keyReleased(Game g, GameKeyEvent e);

    //public void keyToggled(Game g, GameKeyEvent e);

}
