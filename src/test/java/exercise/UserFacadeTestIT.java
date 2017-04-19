/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exercise;

import exercise.fakedatabase.UserFacadeFake;
import exercise.realdatabase.UserFacadeRealDB;
import exercise.IUserFacade;
import exercise.LoginStatus;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
/**
 *
 * @author Christoffer
 */
public class UserFacadeTestIT extends UserFacadeTest {
  @Override
  public IUserFacade makeUserFacade() {
    return new UserFacadeRealDB("pu_localDB");
  }
}
