package exercise;

import Utils.Mailer;
import exercise.fakedatabase.UserFacadeFake;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;



public class AuthenticatorTest {

  Authenticator auth;
  
  //Override in a derived test to test with alternative Facades/Mailers
  public Authenticator makeAuthenticator(){
     Mailer mailer = mock(Mailer.class);
     return new Authenticator(new UserFacadeFake(), mailer);
  }
  
    
  @Before
  public void setUp() {
    auth = makeAuthenticator();
  }

  @Test
  public void existingUserOK_PW() {
    //Given (in setup)
    //When
    boolean result = auth.authenticateUser("Ole", "secret", 0);    
    //Then
    assertThat(result, is(true));
  }
  
  @Test
  public void existingUserNOT_OK_PW() {
    //Given (in setup)
    //When
    boolean result = auth.authenticateUser("Ole", "fido", 0);    
    //Then
    assertThat(result, is(false));
  }
  
  @Test
  public void nonExistingUser() {
    //Given (in setup)
    //When
    boolean result = auth.authenticateUser("Spiderman", "fido", 0);    
    //Then
    assertThat(result, is(false));
  }
  
  @Test
  public void existingUserThreeTriesNotAllowed() {
    //Given (in setup)
    Mailer mailer = mock(Mailer.class);
    auth = new Authenticator(new UserFacadeFake(), mailer);
    
    long startTime = System.currentTimeMillis();
    //When
    boolean result1 = auth.authenticateUser("Ole", "fido", startTime);    
    boolean result2 = auth.authenticateUser("Ole", "fido", startTime+60*1000*10);    
    boolean result3 = auth.authenticateUser("Ole", "fido", startTime+60*1000*10);    

    //Then
    assertThat(result1, is(false));
    assertThat(result2, is(false));
    assertThat(result3, is(false));
    
    verify(mailer, times(1)).sendMail("Ole");
    
  }
  
   @Test
  public void existingUserThreeTries_Allowed_BecauseOfTimeBetweenTries()  {
   //Given (in setup)
    Mailer mailer = mock(Mailer.class);
    auth = new Authenticator(new UserFacadeFake(), mailer);
    
    long timeForFirstTry = System.currentTimeMillis();

    //When
    auth.authenticateUser("Jan", "fido", timeForFirstTry);
    auth.authenticateUser("Jan", "fido", timeForFirstTry + 1000 * 60 * 10);
    
    //Wait more than 30 min before next failing try
    auth.authenticateUser("Jan", "fido", timeForFirstTry + 1000 * 60 * 35);

    //Then
    verify(mailer, times(0)).sendMail("Jan");
  }
  
}
