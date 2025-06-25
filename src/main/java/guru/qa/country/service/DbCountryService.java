package guru.qa.country.service;

import guru.qa.country.data.CountryEntity;
import guru.qa.country.data.CountryRepository;
import guru.qa.country.ex.CountryNotFoundException;
import guru.qa.country.model.CountryJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DbCountryService implements CountryService {

  private final CountryRepository countryRepository;

  @Autowired
  public DbCountryService(CountryRepository countryRepository) {
    this.countryRepository = countryRepository;
  }

  @Override
  public List<CountryJson> allCountries() {
    return countryRepository.findAll().stream()
        .map(ce -> new CountryJson(
            ce.getCountryName(),
            ce.getCountryCode()
        )).toList();
  }

  @Override
  public CountryJson addCountry(String countryName, String countryCode) {
    return countryRepository.save(
        new CountryEntity(
            null,
            countryName,
            countryCode
        )
    ).toJson();
  }

  @Override
  public CountryJson editCountryName(String countryCode, String newName) {
    CountryEntity country = countryRepository.findByCountryCode(countryCode)
        .orElseThrow(CountryNotFoundException::new);
    country.setCountryName(newName);
    return countryRepository.save(country).toJson();
  }
}
