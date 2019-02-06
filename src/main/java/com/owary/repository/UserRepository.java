package com.owary.repository;

import com.owary.model.User;

public interface UserRepository {

    User getUserByUsername(String username);

}
