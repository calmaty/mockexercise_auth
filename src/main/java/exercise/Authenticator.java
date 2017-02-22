package exercise;

import exercise.fakedatabase.UserFacadeFake;
import java.util.HashMap;
import java.util.Map;

public class Authenticator {

  private final int TIME_BETWEEN_FAILED_LOGIN = 30; //minutes  (hardcoded in this ex)
  IUserFacade users;
  Emailer emailer;
  Map<String, FailedLogin> usersWithFailingLogins = new HashMap();

  public Authenticator(IUserFacade users,Emailer emailer) {
    this.users = users;
    this.emailer = emailer;
  }
  public Authenticator() {}
  
  public void setUsers(IUserFacade users){
    this.users = users;
  }

  /**
   * 
   * @param user User name
   * @param pw Passord 
   * @param loginTime NOW, obtained from a call to System.currentTimeMillis()
   * @return true if user is authenticated, otherwise false
   */
  public boolean authenticateUser(String user, String pw, long loginTime) {
    LoginStatus status = users.verifyUser(user, pw);
    if (status == LoginStatus.UNKNOWN_USER) {
      return false;
    }
    if (status == LoginStatus.OK) {
      //If there were previous failed logins, remove them
      usersWithFailingLogins.remove(user);
      return true;
    }
    //Must be a Failed Login
    if (usersWithFailingLogins.containsKey(user)) {
      int failedLogins = usersWithFailingLogins.get(user).incrementFailedLogins(System.currentTimeMillis());
      if (failedLogins >= 3) {
        emailer.sendMail("Suspicious login attempts for user: " +user);
      }
    } else {
      usersWithFailingLogins.put(user, new FailedLogin(System.currentTimeMillis(), TIME_BETWEEN_FAILED_LOGIN));
    }
    return false;
  }
  
  public static void main(String[] args) {
    Emailer emailer = new Emailer();
    emailer.setReceiver("admin@aaa.dk");
    Authenticator authenticater = new Authenticator(new UserFacadeFake(),emailer);
    System.out.println(authenticater.authenticateUser("Jan", "abcde", System.currentTimeMillis()));
    System.out.println(authenticater.authenticateUser("Jan", "afdds", System.currentTimeMillis()));
    System.out.println(authenticater.authenticateUser("Jan", "abcfsdde", System.currentTimeMillis()));
    System.out.println(authenticater.authenticateUser("Jan", "abcfddde", System.currentTimeMillis()));
  }

}
