package exercise;

public class Emailer {
  private String email;
  
  public void setReceiver(String email){
    this.email = email;
  }
  
  /**
   * Sends an email
   * Note: You should interpret this class as a REAL Email handler and that the output below represents a REAL mail being sent
   * @param text Text for the email
   */
  public void sendMail(String text) {
    System.out.println("########################################################################");
    System.out.println("This simulates sending a mail to:" + email +": "+  text);
    System.out.println("#########################################################################");
  }
}
