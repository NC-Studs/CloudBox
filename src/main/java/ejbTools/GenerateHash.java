/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbTools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.ejb.Stateless;

/**
 *
 * @author Andrew
 */
@Stateless
public class GenerateHash {

    public GenerateHash() {
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method
    public String generateHash(String data) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(data.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public boolean checkPass(String pass, String hash) throws NoSuchAlgorithmException {
        return generateHash(pass).equals(hash);
    }

}
