package org.accmanager.service.integration.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class PasswordEncodingTest {

    public static final String PASSWORD = "9k7nEa45dPzVuG64";

    @Test
    public void testBcrypt() {
        PasswordEncoder bcrypt = new BCryptPasswordEncoder();
        assertThat(bcrypt.encode(PASSWORD), notNullValue());
    }

    @Test
    public void testSha256() {
        PasswordEncoder sha256 = new StandardPasswordEncoder();
        assertThat(sha256.encode(PASSWORD), notNullValue());
    }

    @Test
    public void testLdap() {
        PasswordEncoder ldap = new LdapShaPasswordEncoder();
        String encodedPassword = ldap.encode(PASSWORD);
        assertThat(encodedPassword, notNullValue());
        assertThat(ldap.matches(PASSWORD, encodedPassword), is(true));
    }

    @Test
    public void testNoOp() {
        PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();
        assertThat(noOp.encode(PASSWORD), notNullValue());
    }

    @Test
    public void testMd5() {
        assertThat(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()), notNullValue());
    }
}
