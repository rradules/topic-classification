/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class ComputeCRC {

    public ComputeCRC() {
    }

    public long computeChecksum(String string) {

        //Convert string to bytes
        byte bytes[] = string.getBytes();

        Checksum checksum = new CRC32();

        /*
         * To compute the CRC32 checksum for byte array, use
         *
         * void update(bytes[] b, int start, int length)
         * method of CRC32 class.
         */

        checksum.update(bytes, 0, bytes.length);

        /*
         * Get the generated checksum using
         * getValue method of CRC32 class.
         */
        long lngChecksum = checksum.getValue();

        System.out.println("CRC32 checksum for byte array is :" + lngChecksum);
        
        return lngChecksum;
    }
}
