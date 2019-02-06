package com.owary.repository;

import com.owary.model.Country;
import com.owary.utils.DBHelper;
import com.owary.utils.ExceptionThrowingConsumer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.owary.utils.DBHelper.*;

@Repository
public class CountryRepositoryImpl implements CountryRepository{

    private static final String TABLES = "CREATE TABLE COUNTRY(name varchar(30), capital varchar(30), area float, population float);";
    private static final String TABLE = "COUNTRY";

    // Queries
    private static final String SELECT_ONE = "SELECT name, capital, area, population FROM country WHERE name = ?";
    private static final String SELECT_ALL = "SELECT name, capital, area, population FROM country";
    private static final String INSERT = "INSERT INTO country VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE country SET name = ?, capital = ?, area = ?, population = ? WHERE name = ?";
    private static final String DELETE = "DELETE FROM country WHERE name = ?";

    @Override
    public Country getCountry(String name) {
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = preparedStatement(connection, SELECT_ONE, ps -> ps.setString(1, name));
            ResultSet resultSet = preparedStatement.executeQuery()){

            if (resultSet.next()){
                return Country.builder()
                            .name(resultSet.getString("name"))
                            .capital(resultSet.getString("capital"))
                            .area(resultSet.getDouble("area"))
                            .population(resultSet.getDouble("population"))
                        .build();
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Country> getCountries() {
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL);
            ResultSet resultSet = preparedStatement.executeQuery()){

            Country country;
            List<Country> countries = new ArrayList<>();

            while (resultSet.next()){
                country = Country.builder()
                                .name(resultSet.getString("name"))
                                .capital(resultSet.getString("capital"))
                                .area(resultSet.getDouble("area"))
                                .population(resultSet.getDouble("population"))
                            .build();
                countries.add(country);
            }
            return countries;
        }catch (SQLException ex){
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public void addCountry(Country country) {

        ExceptionThrowingConsumer<PreparedStatement> lambda = ps -> {
            ps.setString(1, country.getName());
            ps.setString(2, country.getCapital());
            ps.setDouble(3, country.getArea());
            ps.setDouble(4, country.getPopulation());
        };

        try(Connection connection = getConnection();
            PreparedStatement ps = preparedStatement(connection, INSERT, lambda)){

            ps.execute();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void updateCountry(Country country) {

        ExceptionThrowingConsumer<PreparedStatement> lambda = ps -> {
            ps.setString(1, country.getName());
            ps.setString(2, country.getCapital());
            ps.setDouble(3, country.getArea());
            ps.setDouble(4, country.getPopulation());
            ps.setString(5, country.getName());
        };

        try(Connection connection = getConnection();
            PreparedStatement ps = preparedStatement(connection, UPDATE, lambda)){

            ps.executeUpdate();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteCountry(String country) {
        try(Connection connection = getConnection();
            PreparedStatement ps = preparedStatement(connection, DELETE, prep -> prep.setString(1, country))){

            ps.executeUpdate();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @PostConstruct
    public void preprocess() {

        try(Connection connection = DBHelper.getConnection();
            Statement st = connection.createStatement()) {
            // if table exists
            if (doesTableExists(connection, TABLE)) {
                // remove it
                destroyTable(connection, TABLE);
                // re-create it
                st.execute(TABLES);
                if (doesTableExists(connection, TABLE)) {
                    System.out.println("Country table has been created");
                }else{
                    System.out.println("Country table could not been created");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
