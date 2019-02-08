package com.owary.endpoints;

import com.owary.model.Country;
import com.owary.model.Response;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface CountryService {

    @WebMethod
    @WebResult(name = "Country")
    Country getCountry(String countryCode);

    @WebMethod
    @WebResult(name = "Country")
    List<Country> retrieveAll();

    @WebMethod
    @WebResult(name = "Response")
    Response addCountry(Country country);

    @WebMethod
    @WebResult(name = "Response")
    Response updateCountry(Country country);

    @WebMethod
    @WebResult(name = "Response")
    Response deleteCountry(String country);
}
