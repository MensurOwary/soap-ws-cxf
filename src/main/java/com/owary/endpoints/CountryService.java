package com.owary.endpoints;

import com.owary.model.Country;
import com.owary.model.Response;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface CountryService {

    @WebMethod
    Country getCountry(String countryCode);

    @WebMethod
    List<Country> retrieveAll();

    @WebMethod
    Response addCountry(Country country);

    @WebMethod
    Response updateCountry(Country country);

    @WebMethod
    Response deleteCountry(String country);
}
