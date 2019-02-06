package com.owary.handler;

import com.owary.model.User;
import com.owary.repository.UserRepository;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.springframework.beans.factory.annotation.Autowired;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

public class PasswordCallbackHandler implements CallbackHandler {

    @Autowired
    private UserRepository repository;

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for(Callback cb : callbacks){
            WSPasswordCallback wspc = (WSPasswordCallback) cb;
            // getIdentifier brings in the username supplied
            String identity = wspc.getIdentifier();
            User user = repository.getUserByUsername(identity);
            if (user != null && user.getUsername().equals(identity)) {
                wspc.setPassword(user.getPassword());
                return;
            }
        }
    }
}
