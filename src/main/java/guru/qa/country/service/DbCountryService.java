package guru.qa.country.service;

import guru.qa.country.data.CountryEntity;
import guru.qa.country.data.CountryRepository;
import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.domain.graphql.CountryInputGql;
import guru.qa.country.ex.CountryNotFoundException;
import guru.qa.country.model.CountryJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  @Override
  public Page<CountryGql> allGqlCountries(Pageable pageable) {
    return countryRepository.findAll(pageable)
        .map(fe -> new CountryGql(
            fe.getId(),
            fe.getCountryName(),
            fe.getCountryCode()
        ));
  }

  @Override
  public CountryGql addGqlCountry(CountryInputGql input) {
    CountryEntity ce = new CountryEntity();
    ce.setCountryCode(input.countryCode());
    ce.setCountryName(input.countryName());
    CountryEntity saved = countryRepository.save(ce);
    return new CountryGql(
        saved.getId(),
        saved.getCountryName(),
        saved.getCountryCode()
    );
  }

  @Override
  public CountryGql editGqlCountryName(String countryCode, String countryName) {
    CountryEntity ce = countryRepository.findByCountryCode(countryCode)
        .orElseThrow(CountryNotFoundException::new);
    ce.setCountryName(countryName);
    CountryEntity updated = countryRepository.save(ce);
    return new CountryGql(
        updated.getId(),
        updated.getCountryName(),
        updated.getCountryCode()
    );
  }
}
