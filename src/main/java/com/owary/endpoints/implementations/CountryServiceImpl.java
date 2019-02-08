package com.owary.endpoints.implementations;

import com.owary.endpoints.CountryService;
import com.owary.model.Country;
import com.owary.model.Response;
import com.owary.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebService;
import java.util.List;

@WebService(serviceName = "CountryService",
            endpointInterface = "com.owary.endpoints.CountryService")
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository repository;

    @Override
    public Country getCountry(String country) {
        return repository.getCountry(country);
    }

    @Override
    public List<Country> retrieveAll() {
        addCountry(new Country("AA", "CC", 12D, 32D));
        addCountry(new Country("ADA", "CCD", 12D, 32D));
        return repository.getCountries();
    }

    @Override
    public Response addCountry(Country country) {
        try {
            repository.addCountry(country);
            return Response.created();
        }catch (Exception e){
            return Response.failure();
        }
    }

    @Override
    public Response updateCountry(Country country) {
        try {
            repository.updateCountry(country);
            return Response.success();
        }catch (Exception e){
            return Response.failure();
        }
    }

    @Override
    public Response deleteCountry(String country) {
        try {
            repository.deleteCountry(country);
            return Response.success();
        }catch (Exception e){
            return Response.failure();
        }
    }
}
