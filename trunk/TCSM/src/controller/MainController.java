/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class MainController {
    
    private static MainController singleton=null;
    
    private MainController(){       
    }
    
    public static MainController getInstance(){
        if(singleton==null){
            singleton = new MainController();
        }
        return singleton;
    }
    
    
    
}
