package guru.qa.country.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CountryJson(
    @JsonProperty("countryName")
    String countryName,

    @JsonProperty("countryCode")
    String countryCode) {
}
