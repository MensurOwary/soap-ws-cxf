package com.owary.endpoints;

import com.owary.model.Country;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface CountryService {

    @WebMethod
    Country getCountry(String countryCode);

}
