/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
public class RobotsParser {

    // Cache of robot disallow lists.
    private HashMap disallowListCache = new HashMap();

    // Check if robot is allowed to access the given URL.
    public boolean isRobotAllowed(URL urlToCheck) {
        String host = urlToCheck.getHost().toLowerCase();

        // Retrieve host's disallow list from cache.
        ArrayList disallowList =
                (ArrayList) disallowListCache.get(host);
        String currentAgent = null;


        // If list is not in the cache, download and cache it.
        if (disallowList == null) {
            disallowList = new ArrayList();

            try {
                URL robotsFileUrl =
                        new URL("http://" + host + "/robots.txt");

                // Open connection to robot file URL for reading.
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(
                        robotsFileUrl.openStream()));

                // Read robot file, creating list of disallowed paths.
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.indexOf("User-agent:") == 0) {
                        String agent = line.substring("User-agent:".length());
                        currentAgent = agent.trim();
                    }
                    if (line.indexOf("Disallow:") == 0) {
                        String disallowPath = line.substring("Disallow:".length());

                        // Check disallow path for comments and 
                        // remove if present.
                        int commentIndex = disallowPath.indexOf("#");
                        if (commentIndex != - 1) {
                            disallowPath =
                                    disallowPath.substring(0, commentIndex);
                        }

                        // Remove leading or trailing spaces from 
                        // disallow path.
                        disallowPath = disallowPath.trim();

                        // Add disallow path to list.
                        if (currentAgent.contains("*") || !disallowPath.equals("")) {
                            disallowList.add(disallowPath);
                        }
                    }
                }

                // Add new disallow list to cache.
                disallowListCache.put(host, disallowList);
            } catch (Exception e) {
                /* Assume robot is allowed since an exception
                 is thrown if the robot file doesn't exist. */
                return true;
            }
        }
        /* Loop through disallow list to see if the
         crawling is allowed for the given URL. */
        String file = urlToCheck.getFile();
        for (int i = 0; i < disallowList.size(); i++) {
            String disallow = (String) disallowList.get(i);
            if (file.startsWith(disallow)) {
                return false;
            }
        }

        return true;
    }
}
