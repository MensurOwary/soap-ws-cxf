package com.owary.endpoints.implementations;

import com.owary.endpoints.CountryService;
import com.owary.model.Country;

import javax.jws.WebService;

@WebService(serviceName = "CountryService",
            endpointInterface = "com.owary.endpoints.CountryService")
public class CountryServiceImpl implements CountryService {

    @Override
    public Country getCountry(String countryCode) {
        return new Country("Benin", "Belmopan", 12.3, 12.3);
    }
}
