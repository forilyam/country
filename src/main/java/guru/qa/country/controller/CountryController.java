package guru.qa.country.controller;

import guru.qa.country.model.CountryJson;
import guru.qa.country.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/country")
public class CountryController {
  private final CountryService countryService;

  @Autowired
  public CountryController(CountryService countryService) {
    this.countryService = countryService;
  }

  @GetMapping("/all")
  public List<CountryJson> allCountries() {
    return countryService.allCountries();
  }

  @PostMapping("/add")
  public CountryJson addCountry(@RequestBody CountryJson countryJson) {
    return countryService.addCountry(
        countryJson.countryName(),
        countryJson.countryCode()
    );
  }

  @PatchMapping("/edit/{code}")
  public CountryJson editCountryName(@PathVariable("code") String code,
                                     @RequestBody CountryJson countryJson) {
    return countryService.editCountryName(code, countryJson.countryName());
  }
}
