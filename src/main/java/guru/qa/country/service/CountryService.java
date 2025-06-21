package guru.qa.country.service;

import guru.qa.country.model.CountryJson;

import java.util.List;

public interface CountryService {

  List<CountryJson> allCountries();

  CountryJson addCountry(String countryName, String countryCode);

  CountryJson editCountryName(String countryCode, String countryName);
}
