package guru.qa.country.service;

import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.domain.graphql.CountryInputGql;
import guru.qa.country.model.CountryJson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CountryService {

  List<CountryJson> allCountries();

  Page<CountryGql> allGqlCountries(Pageable pageable);

  CountryJson addCountry(String countryName, String countryCode);

  CountryGql addGqlCountry(CountryInputGql input);

  CountryJson editCountryName(String countryCode, String countryName);

  CountryGql editGqlCountryName(String countryCode, String countryName);
}
