/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cake.io.test;

import org.cake.game.io.Settings;

/**
 *
 * @author Aaron
 */
public class INITest {
    
    public static void main(String[] args) {
        
        
        
        Settings s = new Settings("resources/settings.ini");
        
        //s.merge(s.copy(), "newnode"); 
        
        System.out.println(s);
     
        s.save("resources/settings2.ini");
        s.save("resources/settings2.properties");
        
    }
    
}
