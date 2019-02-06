package com.owary.handler;

import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.message.token.UsernameToken;
import org.apache.wss4j.dom.validate.UsernameTokenValidator;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

import static com.owary.utils.DBHelper.*;


public class UsernameTokenDBValidator extends UsernameTokenValidator {

    @Override
    protected void verifyPlaintextPassword(UsernameToken usernameToken, RequestData data) throws WSSecurityException {
        // the credentials user supplied in request
        String passwordSupplied = usernameToken.getPassword();
        String usernameSupplied = usernameToken.getName();
        // password type
        String pwType = usernameToken.getPasswordType();
        // this instance is handled to the PasswordCallbackHandler we created.
        WSPasswordCallback pwCb = new WSPasswordCallback(usernameSupplied, null, pwType, 2);
        try {
            // call the handle method of our PasswordCallbackHandler
            // it will set the actual password
            data.getCallbackHandler().handle(new Callback[]{pwCb});
        } catch (UnsupportedCallbackException | IOException ex) {
            // any error occurs, throw validation error
            throw new WSSecurityException(WSSecurityException.ErrorCode.FAILED_AUTHENTICATION, ex);
        }
        // the actual password
        String passwordOriginal = pwCb.getPassword();

        // check if they don't match throw validation error, otherwise let it pass
        if (!passwordMatches(passwordSupplied, passwordOriginal)) {
            throw new WSSecurityException(WSSecurityException.ErrorCode.FAILED_AUTHENTICATION);
        }
    }
}
