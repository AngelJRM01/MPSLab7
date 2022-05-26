package org.mps.authentication;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTestIT {

    private CredentialStore credentialStore;
    private Date date;
    private PasswordString passwordString;
    private UserRegistration userRegistration;

    @BeforeEach
    public void setUp(){
        userRegistration = new UserRegistration();
    }

    @AfterEach
    public void finish(){
        credentialStore = null;
        date = null;
        passwordString = null;
        userRegistration = null;
    }

    @Test
    public void birthDateIncorrect(){
        date = new Date(0,1,2000);

        CredentialValidator.ValidationStatus obtainedStatus = userRegistration.register(date, null, null);

        CredentialValidator.ValidationStatus expectedStatus  = CredentialValidator.ValidationStatus.BIRTHDAY_INVALID;

        assertEquals(expectedStatus, obtainedStatus);
    }

    @Test
    public void birthDateCorrectAndPasswordIncorrect(){
        date = new Date(1,1,2000);
        passwordString = new PasswordString("aaa");

        CredentialValidator.ValidationStatus obtainedStatus = userRegistration.register(date, passwordString, null);

        CredentialValidator.ValidationStatus expectedStatus  = CredentialValidator.ValidationStatus.PASSWORD_INVALID;

        assertEquals(expectedStatus, obtainedStatus);
    }

}
