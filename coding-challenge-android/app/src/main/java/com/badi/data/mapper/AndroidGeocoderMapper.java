package com.badi.data.mapper;

import android.location.Address;
import android.text.TextUtils;

import com.badi.data.entity.PlaceAddress;
import com.google.android.gms.location.places.Place;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class AndroidGeocoderMapper {

    @Inject
    public AndroidGeocoderMapper() {

    }

    public List<PlaceAddress> transformIntoPlaceAddress(List<Address> addresses) {
        List<PlaceAddress> placeAddresses = new ArrayList<>();
        for (Address address : addresses) {
            placeAddresses.add(transformAddressIntoPlaceAddress(address));
        }
        return placeAddresses;
    }

    private PlaceAddress transformAddressIntoPlaceAddress(Address address) {
        if (address == null) {
            return null;
        }

        ArrayList<String> addressFragments = new ArrayList<>();
        // Fetch the address lines using getAddressLine,
        // join them, and send them to the thread.
        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            addressFragments.add(address.getAddressLine(i));
        }

        String currencyCode;
        try {
            currencyCode = Currency.getInstance(new Locale("", address.getCountryCode())).getCurrencyCode();
        } catch (Exception e) {
            currencyCode = null;
        }

        return PlaceAddress.create(
                null,
                address.getAddressLine(0),
                TextUtils.join(", ", addressFragments),
                address.getThoroughfare(),
                address.getSubThoroughfare(),
                (address.getLocality() != null ? address.getLocality() : address.getSubAdminArea()),
                address.getCountryName(),
                address.getPostalCode(),
                currencyCode,
                address.getLatitude(),
                address.getLongitude(),
                null);
    }

    public List<PlaceAddress> transformIntoPlaceAddress(List<Address> addresses, Place place) {
        ArrayList<PlaceAddress> placeAddresses = new ArrayList<>();
        for (Address address : addresses) {
            placeAddresses.add(transformAddressIntoPlaceAddress(address, place));
        }
        return placeAddresses;
    }

    private PlaceAddress transformAddressIntoPlaceAddress(Address address, Place place) {
        String currencyCode = null;
        if (address != null) {
            try {
                currencyCode = Currency.getInstance(new Locale("", address.getCountryCode())).getCurrencyCode();
            } catch (Exception e) {
                currencyCode = null;
            }
        }

        return PlaceAddress.create(
                place.getId(),
                place.getName().toString(),
                place.getAddress().toString(),
                address != null ? address.getThoroughfare() : null,
                address != null ? address.getSubThoroughfare() : null,
                (address != null && address.getLocality() != null) ? address.getLocality() :
                        (address != null && address.getSubAdminArea() != null ? address.getSubAdminArea() : null),
                (address != null && address.getCountryName() != null) ? address.getCountryName() :
                        (place.getLocale().getDisplayCountry() != null ? place.getLocale().getDisplayCountry() : null),
                address != null ? address.getPostalCode() : null,
                currencyCode,
                place.getLatLng().latitude,
                place.getLatLng().longitude,
                null);
    }
}
