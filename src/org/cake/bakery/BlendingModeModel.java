/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.bakery;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import org.cake.game.Graphics;
import org.cake.game.Graphics.BlendingMode;

/**
 *
 * @author Aaron
 */
public class BlendingModeModel extends AbstractListModel<String> implements ComboBoxModel<String> {

    private BlendingMode[] opts = BlendingMode.values();
    private Object selected = getElementAt(0);

    @Override
    public int getSize() {
        return opts.length;
    }

    @Override
    public String getElementAt(int index) {
        return opts[index].name();
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selected = anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selected;
    }

}
