/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


/**
 *
 * @author kira
 */
@ManagedBean(name = "bean1")
@SessionScoped
public class Bean implements Serializable {

    private String name;
    private String pass;
    public boolean isLogged = false;
    private boolean showErrorMessage = false;

    public void setShowErrorMessage(boolean showErrorMessage) {
        this.showErrorMessage = showErrorMessage;
    }

    public boolean isShowErrorMessage() {
//        if (!"".equals(name) || !"".equals(pass)) showErrorMessage = false;
        return showErrorMessage;
    }

 

    /**
     * Creates a new instance of Bean
     */
    public Bean() {
       
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String action() throws NoSuchAlgorithmException {
        System.out.println("name is: " + name);
        System.out.println("pass is: " + pass);
//        if (!"".equals(name) || !"".equals(pass)) {
//            if (user.getUname().equals(name) && checkPass(pass)) {
//                isLogged = true;
//                showErrorMessage = false;
//                return "secure/index.xhtml?faces-redirect=true";
//            } else {
//                showErrorMessage = true;
//                return null;
//            }
////        } else {
////            showErrorMessage = false;
////            return null;
////        }
return "secure/index.xhtml?faces-redirect=true";

    }

    public boolean checkPass(String password) throws NoSuchAlgorithmException {
//        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
//        byte[] result = mDigest.digest(password.getBytes());
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < result.length; i++) {
//            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
//        }
//        return user.getPassHash().equals(sb.toString());
return false;
    }

}
