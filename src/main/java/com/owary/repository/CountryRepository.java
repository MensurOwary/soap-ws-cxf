package com.owary.repository;

import com.owary.model.Country;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CountryRepository {

    Country getCountry(String name);
    List<Country> getCountries();
    void addCountry(Country country);
    void updateCountry(Country country);
    void deleteCountry(String country);

}
