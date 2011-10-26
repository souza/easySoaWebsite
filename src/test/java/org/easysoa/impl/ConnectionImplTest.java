
package org.easysoa.impl;

import org.junit.Test;
import org.ow2.frascati.examples.test.FraSCAtiTestCase;

/**
 *
 * @author dirix
 */
public class ConnectionImplTest extends FraSCAtiTestCase{

    private static final String COMPOSITE_NAME = "easysoa";

    public ConnectionImplTest() {
    }

 
    /**
     * Test of connect method, of class ConnectionImpl.
     */
   /**@Test
    public void testSearchUser() throws FrascatiException {
        System.out.println("connect");
        User user = getService(Users.class,"test").searchUser(new Long(2));
        Assert.assertNotNull(user);
    }*/

    /**
     * Test of createAccount method, of class ConnectionImpl.
     */
    /**@Test
    public void testCreateAccount() throws FrascatiException{
        try{
        System.out.println("createAccount");
        String login = "test";
        String password = "test";
        String confirmPassword = "test"; 
        String mail = "test";
        String name = "test";
        String surname = "test";
        String civility = "Mr";
        String town = "Aniche";
        String country = "fr";
        String birthday="03/14/1987";
        User result = getService(Connection.class,"test2").createAccount(login, password, confirmPassword, mail, name, surname, civility,town,country,birthday);
        Assert.assertNotNull(result);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }*/

    @Test
    public void test(){
        //List<User> result = getService(Friends.class,"test").searchFriends("michel","dirix","aniche");
    }

    @Override 
    public String getComposite() {
        return COMPOSITE_NAME;
    }

}