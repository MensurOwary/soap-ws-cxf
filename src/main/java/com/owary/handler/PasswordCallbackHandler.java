package com.owary.handler;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PasswordCallbackHandler implements CallbackHandler {

    private Map<String, String> credentials;

    public PasswordCallbackHandler() {
        this.credentials = new HashMap<>();
        this.credentials.put("admin", "admin");
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for(Callback cb : callbacks){
            WSPasswordCallback wspc = (WSPasswordCallback) cb;
            // getIdentifier brings in the username supplied
            String pass = credentials.get(wspc.getIdentifier());
            if (pass != null) {
                wspc.setPassword(pass);
                return;
            }
        }
    }
}
