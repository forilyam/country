package guru.qa.country.service;

import com.google.protobuf.Empty;
import guru.qa.country.model.CountryJson;
import guru.qa.grpc.country.*;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GrpcCountryService extends CountryServiceGrpc.CountryServiceImplBase {

  private final CountryService countryService;

  public GrpcCountryService(CountryService countryService) {
    this.countryService = countryService;
  }

  @Override
  public void allCountries(Empty request, StreamObserver<CountriesResponse> responseObserver) {
    List<CountryJson> countries = countryService.allCountries();
    List<CountryResponse> response = countries.stream()
        .map(country -> CountryResponse.newBuilder()
            .setCountryName(country.countryName())
            .setCountryCode(country.countryCode())
            .build())
        .toList();

    CountriesResponse countriesResponse = CountriesResponse
        .newBuilder()
        .addAllCountries(response)
        .build();
    responseObserver.onNext(countriesResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void addCountry(CountryRequest request, StreamObserver<CountryResponse> responseObserver) {
    CountryJson country = countryService.addCountry(
        request.getCountryName(),
        request.getCountryCode()
    );

    responseObserver.onNext(CountryResponse.newBuilder()
        .setCountryCode(country.countryCode())
        .setCountryName(country.countryName())
        .build());
    responseObserver.onCompleted();
  }

  @Override
  public void editCountryName(CountryRequest request, StreamObserver<CountryResponse> responseObserver) {
    CountryJson updatedCountry = countryService.editCountryName(
        request.getCountryCode(),
        request.getCountryName());

    CountryResponse response = CountryResponse.newBuilder()
        .setCountryName(updatedCountry.countryName())
        .setCountryCode(updatedCountry.countryCode())
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public StreamObserver<CountryRequest> addCountries(StreamObserver<CountResponse> responseObserver) {
    AtomicInteger count = new AtomicInteger();
    return new StreamObserver<>() {
      @Override
      public void onNext(CountryRequest countryRequest) {
        countryService.addCountry(
            countryRequest.getCountryCode(),
            countryRequest.getCountryName()
        );

        count.incrementAndGet();
      }

      @Override
      public void onError(Throwable throwable) {
        responseObserver.onError(throwable);
      }

      @Override
      public void onCompleted() {
        CountResponse response = CountResponse.newBuilder()
            .setCount(count.get())
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }
    };
  }
}