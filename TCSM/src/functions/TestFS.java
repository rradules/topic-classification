/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class TestFS {

    public static void main(String[] args) throws Throwable {
        FeatureSelection fs = new FeatureSelection();
        String input = "ana are multe mere, pere, fructe? dar! oare.";
        System.out.println(fs.performFeatureSelection(input));
    }
}