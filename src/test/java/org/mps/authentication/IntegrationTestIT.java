package org.mps.authentication;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

        passwordString = Mockito.mock(PasswordString.class);
        Mockito.when(passwordString.validate()).thenReturn(true);

        credentialStore = Mockito.mock(CredentialStore.class);
        Mockito.when(credentialStore.credentialExists(Mockito.any(), Mockito.any())).thenReturn(false);

        CredentialValidator.ValidationStatus obtainedStatus = userRegistration.register(date, passwordString, credentialStore);

        CredentialValidator.ValidationStatus expectedStatus  = CredentialValidator.ValidationStatus.BIRTHDAY_INVALID;

        assertEquals(expectedStatus, obtainedStatus);
    }

    @Test
    public void birthDateCorrectAndPasswordIncorrect(){
        date = new Date(1,1,2000);
        passwordString = new PasswordString("aaa");

        credentialStore = Mockito.mock(CredentialStore.class);
        Mockito.when(credentialStore.credentialExists(Mockito.any(), Mockito.any())).thenReturn(false);

        CredentialValidator.ValidationStatus obtainedStatus = userRegistration.register(date, passwordString, credentialStore);

        CredentialValidator.ValidationStatus expectedStatus  = CredentialValidator.ValidationStatus.PASSWORD_INVALID;

        assertEquals(expectedStatus, obtainedStatus);
    }

    @Test
    public void birthDateCorrectAndPasswordCorrectAndExistingCredentials(){
        date = new Date(1,1,2000);
        passwordString = new PasswordString("aaa8.aaa");

        credentialStore = new CredentialStoreSet();

        userRegistration.register(date, passwordString, credentialStore);

        int sizeCredentialsStoreBefore = credentialStore.size();

        CredentialValidator.ValidationStatus obtainedStatus = userRegistration.register(date, passwordString, credentialStore);

        CredentialValidator.ValidationStatus expectedStatus  = CredentialValidator.ValidationStatus.EXISTING_CREDENTIAL;

        int sizeCredentialsStoreAfter = credentialStore.size();

        assertEquals(expectedStatus, obtainedStatus);
        assertEquals(sizeCredentialsStoreAfter, sizeCredentialsStoreBefore);
    }


    @Test
    public void birthDateCorrectAndPasswordCorrectAndCredentialNotExists(){
        date = new Date(1,1,2000);
        passwordString = new PasswordString("sk?9n.sdi=8");
        credentialStore = new CredentialStoreSet();

        int sizeCredentialsStoreBefore = credentialStore.size();

        CredentialValidator.ValidationStatus obtainedStatus = userRegistration.register(date, passwordString, credentialStore);

        CredentialValidator.ValidationStatus expectedStatus  = CredentialValidator.ValidationStatus.VALIDATION_OK;

        int sizeCredentialsStoreAfter = credentialStore.size();

        assertEquals(expectedStatus, obtainedStatus);
        assertEquals(sizeCredentialsStoreAfter, sizeCredentialsStoreBefore + 1);
    }
}
