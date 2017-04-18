package Utils;

/**
 * 
 * Class used for the exercise to simulate sending mails
 */
public class Mailer {

  private final String MAIL = "admin@aaa.dk"; //mail address to send warnings (hardcoded in this ex)
  
  /**
   * Call this method to simulate sending a mail
   * @param user The user to report about
   */
  public void sendMail(String user) {
    System.out.println("####################################################################");
    System.out.println("This simulates sending a mail to:" + MAIL + "\nFailed logins for user:  " + user);
    System.out.println("####################################################################");
  }  
}
