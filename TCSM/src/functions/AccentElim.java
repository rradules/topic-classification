/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

/**
 *
 * @author Student
 */
public class AccentElim {

    public AccentElim() {
    }

    //eliminare diacritice
    public String elliminateAccents(String content) {
        String newContent = "";
        if (content != null) {
            for (char ch : content.toCharArray()) {
                switch (ch) {
                    case 'ă':
                        newContent += 'a';
                        break;
                    case 'î':
                        newContent += 'i';
                        break;
                    case 'â':
                        newContent += 'a';
                        break;
                    case 'ș':
                        newContent += 's';
                        break;
                    case 'ț':
                        newContent += 't';
                        break;
                    default:
                        newContent += ch;
                        break;
                }
            }
        }
        return newContent;
    }
}
