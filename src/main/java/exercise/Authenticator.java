package exercise;

import Utils.Mailer;
import exercise.realdatabase.UserFacadeRealDB;
import java.util.HashMap;
import java.util.Map;


public class Authenticator {

  private final int TIME_BETWEEN_FAILED_LOGIN = 30; //minutes  (hardcoded in this ex)
  IUserFacade users;
  Map<String, FailedLogin> usersWithFailingLogins = new HashMap();
  Mailer mailer;

  /**
   * Create a new Authenticator instance
   * @param f The IUserface to use
   * @param m Them Mailer to used to send mails
   */
  public Authenticator(IUserFacade f, Mailer m) {
    this.users = f;
    this.mailer = m;
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
      int failedLogins = usersWithFailingLogins.get(user).incrementFailedLogins(loginTime);
      if (failedLogins >= 3) {
        mailer.sendMail(user);
      }
    } else {
      usersWithFailingLogins.put(user, new FailedLogin(loginTime, TIME_BETWEEN_FAILED_LOGIN));
    }
    return false;
  }



  public static void main(String[] args) {
    Authenticator authenticater = new Authenticator(new UserFacadeRealDB("pu_localDB"),new Mailer());
    System.out.println(authenticater.authenticateUser("Jan", "abcde", System.currentTimeMillis()));
    System.out.println(authenticater.authenticateUser("Jan", "afdds", System.currentTimeMillis()));
    System.out.println(authenticater.authenticateUser("Jan", "abcfsdde", System.currentTimeMillis()));
    System.out.println(authenticater.authenticateUser("Jan", "abcfddde", System.currentTimeMillis()));
  }

}
