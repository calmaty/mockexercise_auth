package exercise.fakedatabase;

import exercise.LoginStatus;
import java.util.HashMap;
import java.util.Map;
import exercise.IUserFacade;

public class UserFacadeFake implements IUserFacade {

  private Map<String, String> users = new HashMap();

  public UserFacadeFake() {
    users.put("Ole", "secret");
    users.put("Jan", "abcde");
    users.put("Richard", "fido");
  }

  @Override
  public LoginStatus verifyUser(String user, String pw) {
    if (!users.containsKey(user)) {
      return LoginStatus.UNKNOWN_USER;
    }
    return users.get(user).equals(pw) ? LoginStatus.OK : LoginStatus.INVALID_PASSWORD;
  }
}
