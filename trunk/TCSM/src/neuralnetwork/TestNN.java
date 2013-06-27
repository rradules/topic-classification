/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetwork;

import controller.MainController;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class TestNN {

    public static void main(String[] args) {
        // TODO code application logic here

//        InputSetBuilder isb = new InputSetBuilder();
//        isb.buildInput();
//        System.out.println("docNo: " + isb.getDocNo());
//        System.out.println("Input set size: " + isb.getFinal_posts().size());

        TrainingLayer tl = new TrainingLayer();
        tl.trainNetwork();
        //  MainController.getInstance().emptyTempTable();
   

    }
}
