package com.owary.repository;

import com.owary.model.User;
import com.owary.utils.DBHelper;

import javax.annotation.PostConstruct;
import java.sql.*;

import static com.owary.utils.DBHelper.*;

public class UserRepositoryImpl implements UserRepository {

    private static final String TABLES = "CREATE TABLE USERS(id int, username varchar(30), password varchar(100));";
    private static final String INSERT = "INSERT INTO USERS VALUES (?, ?, ?)";
    private static final String TABLE = "USERS";

    @Override
    public User getUserByUsername(String username) {
        try(Connection connection = DBHelper.getConnection();
            PreparedStatement ps = DBHelper.preparedStatement(connection, "SELECT id, username, password FROM USERS WHERE username = ?", preparedStatement -> preparedStatement.setString(1, username));
            ResultSet rs = ps.executeQuery()){

            if (rs.next()) {
                Long idRetrieved = rs.getLong(1);
                String usernameRetrieved = rs.getString(2);
                String passwordRetrieved = rs.getString(3);
                return User.builder()
                        .id(idRetrieved)
                        .username(usernameRetrieved)
                        .password(passwordRetrieved)
                        .build();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostConstruct
    public void preprocess() {
        // preprocessing
        try(Connection connection = DBHelper.getConnection();
            Statement st = connection.createStatement()) {
            // if table does exist
            if (doesTableExists(connection, TABLE)) {
                // destroy the previous table
                destroyTable(connection, TABLE);
                // fire the create statement
                st.execute(TABLES);
                // if table is created
                if (doesTableExists(connection, TABLE)) {
                    // print the message
                    System.out.println("Users table created");
                    // insert the user
                    PreparedStatement ps = connection.prepareStatement(INSERT);
                    // set id
                    ps.setInt(1, 1);
                    // set username
                    ps.setString(2, "admin");
                    // set password
                    String admin = encode("admin");
                    ps.setString(3, admin);
                    // if it has been added, print
                    if (ps.executeUpdate() > 0){
                        System.out.println("User entry has been successfully added");
                    }else{System.out.println("Failure on User insertion");}
                }else{System.out.println("Users table could not been created");}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
