package com.badi.data.mapper;

import com.badi.data.entity.PlaceAddress;
import com.badi.data.entity.googlemaps.GoogleMapsGeocoderResponse;
import com.badi.data.entity.googlemaps.GoogleMapsGeocoderResult;
import com.badi.data.entity.googlemaps.GoogleMapsGeocoderResultAddressComponent;
import com.badi.data.entity.googlemaps.GoogleMapsGeocoderResultGeometry;
import com.badi.data.entity.googlemaps.GoogleMapsGeocoderResultLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class GoogleMapsGeocoderMapper {

    @Inject
    public GoogleMapsGeocoderMapper() {

    }

    public List<PlaceAddress> transformIntoPlaceAddresses(GoogleMapsGeocoderResponse googleMapsGeocoderResponse) {
        if (googleMapsGeocoderResponse == null || googleMapsGeocoderResponse.getResults() == null || googleMapsGeocoderResponse
                .getResults().size() <= 0) {
            return Collections.emptyList();
        }

        List<PlaceAddress> placeAddresses = new ArrayList<>();
        List<GoogleMapsGeocoderResult> results = googleMapsGeocoderResponse.getResults();
        for (GoogleMapsGeocoderResult googleMapsGeocoderResult : results) {
            PlaceAddress placeAddress = transformGoogleMapsGeocoderResultIntoPlaceAddress(googleMapsGeocoderResult);
            placeAddresses.add(placeAddress);
        }
        return placeAddresses;
    }

    private PlaceAddress transformGoogleMapsGeocoderResultIntoPlaceAddress(GoogleMapsGeocoderResult googleMapsGeocoderResult) {
        if (googleMapsGeocoderResult == null) {
            return null;
        }

        PlaceAddress.Builder placeAddressBuilder = PlaceAddress.builder();

        placeAddressBuilder.setId(googleMapsGeocoderResult.getPlaceId());
        String placeAddressName = null;
        for (GoogleMapsGeocoderResultAddressComponent addressComponent : googleMapsGeocoderResult.getAddressComponents()) {
            List<String> addressComponentTypes = addressComponent.getTypes();
            if (addressComponentTypes != null && !addressComponentTypes.isEmpty()) {
                String addressComponentType = addressComponentTypes.get(0);
                if (addressComponentType != null && addressComponentType.equalsIgnoreCase("route")) {
                    placeAddressName = addressComponent.getLongName();
                    break;
                }
            }
        }
        if (placeAddressName != null && placeAddressName.length() > 0) {
            placeAddressBuilder.setName(placeAddressName);
        } else {
            placeAddressBuilder.setName(googleMapsGeocoderResult.getFormattedAddress());
        }

        placeAddressBuilder.setAddress(googleMapsGeocoderResult.getFormattedAddress());
        GoogleMapsGeocoderResultGeometry geometry = googleMapsGeocoderResult.getGeometry();
        GoogleMapsGeocoderResultLocation location = geometry.getLocation();
        placeAddressBuilder.setLatitude(location.getLatitude());
        placeAddressBuilder.setLongitude(location.getLongitude());


        for (GoogleMapsGeocoderResultAddressComponent addressComponent : googleMapsGeocoderResult.getAddressComponents()) {
            if (addressComponent.getTypes() != null && addressComponent.getTypes().size()
                    >= 0) {
                String componentType = addressComponent.getTypes().get(0);
                String componentLongName = addressComponent.getLongName();
                String componentShortName = addressComponent.getShortName();
                switch (componentType) {
                    case "street_number":
                        placeAddressBuilder.setStreetNumber(componentLongName);
                        break;
                    case "route":
                        placeAddressBuilder.setStreet(componentLongName);
                        break;
                    case "locality":
                        placeAddressBuilder.setCity(componentLongName);
                        break;
                    case "country":
                        placeAddressBuilder.setCountry(componentLongName);
                        String currencyCode = null;
                        try {
                            currencyCode = Currency.getInstance(new Locale("", componentShortName)).getCurrencyCode();
                        } catch (IllegalArgumentException e) {
                            currencyCode = null;
                        }
                        placeAddressBuilder.setCurrencyCode(currencyCode);
                        break;
                    case "postal_code":
                        placeAddressBuilder.setPostalCode(componentLongName);
                        break;
                }
            }
        }

        PlaceAddress placeAddress = placeAddressBuilder.build();
        if (placeAddress.city() == null) {
            for (GoogleMapsGeocoderResultAddressComponent addressComponent : googleMapsGeocoderResult.getAddressComponents()) {
                for (String type : addressComponent.getTypes()) {
                    if (type.equalsIgnoreCase("administrative_area_level_1")) {
                        placeAddress = placeAddress.withCity(addressComponent.getLongName());
                        break;
                    }
                }
            }
        }

        if (placeAddress.city() == null) {
            for (GoogleMapsGeocoderResultAddressComponent addressComponent : googleMapsGeocoderResult.getAddressComponents()) {
                for (String type : addressComponent.getTypes()) {
                    if (type.equalsIgnoreCase("postal_town")) {
                        placeAddress = placeAddress.withCity(addressComponent.getLongName());
                        break;
                    }
                }
            }
        }

        return placeAddress;
    }
}
