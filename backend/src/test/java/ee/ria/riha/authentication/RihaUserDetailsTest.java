package ee.ria.riha.authentication;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * @author Valentin Suhnjov
 */
public class RihaUserDetailsTest {

  private final String USERNAME = "john.doe";
  private final String PASSWORD = "strong";
  private final String PERSONAL_CODE = "12345";

  private final SimpleGrantedAuthority CUSTOM_ROLE = new SimpleGrantedAuthority("ROLE_TEST");

  private final List<SimpleGrantedAuthority> WRAPPED_ROLES = Collections.singletonList(CUSTOM_ROLE);

  private final User WRAPPED_USER_DETAILS = new User(USERNAME, PASSWORD, WRAPPED_ROLES);

  @Test
  public void failsWhenDelegateIsNull() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new RihaUserDetails(null, "123");
        });
  }

  @Test
  public void failsWhenPersonalCodeIsNull() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new RihaUserDetails(WRAPPED_USER_DETAILS, null);
        });
  }

  @Test
  public void failsWhenPersonalCodeIsEmpty() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new RihaUserDetails(WRAPPED_USER_DETAILS, "");
        });
  }

  @Test
  public void propagatesWrappedUserDetails() {
    RihaUserDetails rihaUserDetails = new RihaUserDetails(WRAPPED_USER_DETAILS, PERSONAL_CODE);

    assertThat(rihaUserDetails.getUsername(), is(equalTo("john.doe")));
    assertThat(rihaUserDetails.getPassword(), is(equalTo("strong")));
    assertThat(rihaUserDetails.isEnabled(), is(true));
    assertThat(rihaUserDetails.isAccountNonExpired(), is(true));
    assertThat(rihaUserDetails.isAccountNonLocked(), is(true));
    assertThat(rihaUserDetails.isCredentialsNonExpired(), is(true));
    assertThat(rihaUserDetails.getAuthorities(), contains(CUSTOM_ROLE));
  }

  @Test
  public void constructsFullNameFromFirstAndLastNames() {
    RihaUserDetails rihaUserDetails = new RihaUserDetails(WRAPPED_USER_DETAILS, PERSONAL_CODE);
    rihaUserDetails.setFirstName("John");
    rihaUserDetails.setLastName("Doe");

    assertThat(rihaUserDetails.getFullName(), is(equalTo("John Doe")));
  }

  @Test
  public void skipsFirstNameInFullNameWhenFirstNameNotDefined() {
    RihaUserDetails rihaUserDetails = new RihaUserDetails(WRAPPED_USER_DETAILS, PERSONAL_CODE);
    rihaUserDetails.setLastName("Doe");

    assertThat(rihaUserDetails.getFullName(), is(equalTo("Doe")));
  }

  @Test
  public void skipsLastNameInFullNameWhenLastNameNotDefined() {
    RihaUserDetails rihaUserDetails = new RihaUserDetails(WRAPPED_USER_DETAILS, PERSONAL_CODE);
    rihaUserDetails.setFirstName("John");

    assertThat(rihaUserDetails.getFullName(), is(equalTo("John")));
  }
}
