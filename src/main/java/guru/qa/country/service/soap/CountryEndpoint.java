package guru.qa.country.service.soap;

import guru.qa.country.config.AppConfig;
import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.model.CountryJson;
import guru.qa.country.service.CountryService;
import guru.qa.xml.country.*;
import org.springframework.data.domain.Page;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class CountryEndpoint {

  private final CountryService countryService;

  public CountryEndpoint(CountryService countryService) {
    this.countryService = countryService;
  }

  @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "addCountryRequest")
  @ResponsePayload
  public CountryResponse addCountry(@RequestPayload AddCountryRequest request) {
    CountryJson country = countryService.addCountry(
        request.getCountry().getCountryCode(),
        request.getCountry().getCountryName());
    CountryResponse response = new CountryResponse();
    Country xmlCountry = new Country();
    xmlCountry.setCountryName(country.countryName());
    xmlCountry.setCountryCode(country.countryCode());
    response.setCountry(xmlCountry);
    return response;
  }

  @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "pageRequest")
  @ResponsePayload
  public CountriesResponse allCountries(@RequestPayload PageRequest request) {
    Page<CountryGql> countries = countryService.allGqlCountries(org.springframework.data.domain.PageRequest.of(
            request.getPage(),
            request.getSize()
        )
    );
    CountriesResponse response = new CountriesResponse();
    response.setTotalPages(countries.getTotalPages());
    response.setTotalElements(countries.getTotalElements());
    response.getCountries().addAll(
        countries.getContent().stream().map(
            gqlCountry -> {
              Country xmlCountry = new Country();
              xmlCountry.setId(gqlCountry.id().toString());
              xmlCountry.setCountryCode(gqlCountry.countryCode());
              xmlCountry.setCountryName(gqlCountry.countryName());
              return xmlCountry;
            }
        ).toList()
    );
    return response;
  }

  @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "editCountryNameRequest")
  @ResponsePayload
  public CountryResponse editCountryName(@RequestPayload EditCountryNameRequest request) {
    CountryGql country = countryService.editGqlCountryName(
        request.getCountry().getCountryCode(),
        request.getCountry().getCountryName());

    Country xmlCountry = new Country();
    xmlCountry.setId(country.id().toString());
    xmlCountry.setCountryName(country.countryName());
    xmlCountry.setCountryCode(country.countryCode());

    CountryResponse response = new CountryResponse();
    response.setCountry(xmlCountry);
    return response;
  }
}
