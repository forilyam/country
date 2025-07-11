package guru.qa.country.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {
  Optional<CountryEntity> findByCountryCode(String countryCode);
}
