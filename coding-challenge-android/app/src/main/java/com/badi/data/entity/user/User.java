/*
 * File: User.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.user;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.badi.R;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * {@link User} used for mapping the JSON Request to the server for tracking the user meta at the endpoint /me.
 */
@AutoValue
public abstract class User implements Parcelable {

    public static final int[] LANGUAGE_TYPE_TEXT = {R.string.profile_other, R.string.profile_language_arabic,
            R.string.profile_language_indonesian, R.string.profile_language_chinese, R.string.profile_language_czech,
            R.string.profile_language_danish, R.string.profile_language_german, R.string.profile_language_greek,
            R.string.profile_language_english, R.string.profile_language_esperanto, R.string.profile_language_spanish,
            R.string.profile_language_french, R.string.profile_language_hindi, R.string.profile_language_italian,
            R.string.profile_language_japanese, R.string.profile_language_korean, R.string.profile_language_hungarian,
            R.string.profile_language_dutch, R.string.profile_language_norwegian, R.string.profile_language_polish,
            R.string.profile_language_portuguese, R.string.profile_language_romanian, R.string.profile_language_russian,
            R.string.profile_language_finnish, R.string.profile_language_swahili, R.string.profile_language_swedish,
            R.string.profile_language_thai, R.string.profile_language_turkish, R.string.profile_language_vietnamese,
            R.string.profile_language_catalan, R.string.profile_language_basque, R.string.profile_language_galician};

    public static final int USER_SEEKER = 0;
    public static final int USER_LISTER = 1;

    public static final int USER_LOGIN_TYPE_FACEBOOK = 1;
    public static final int USER_LOGIN_TYPE_GOOGLE = 2;
    public static final int USER_LOGIN_TYPE_EMAIL = 3;

    public static final int MAX_VALUE_PROGRESS_BAR = 10;
    public static final int TYPE_PARTIER = 1;
    public static final int TYPE_ATHLETE = 2;
    public static final int TYPE_ORGANIZED = 3;
    public static final int TYPE_ACTIVE = 4;
    public static final int TYPE_GEEK = 5;
    public static final int TYPE_SOCIABLE = 6;

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("login_type")
    public abstract Integer loginType();

    @SerializedName("nationality")
    @Nullable public abstract Integer nationality();

    @SerializedName("first_name")
    @Nullable public abstract String firstName();

    @SerializedName("last_name")
    @Nullable public abstract String lastName();

    @SerializedName("email")
    @Nullable public abstract String email();

    @SerializedName("birth_date")
    @Nullable public abstract Date birthDate();

    @SerializedName("biological_sex")
    @Nullable public abstract Integer biologicalSex();

    @SerializedName("occupation")
    @Nullable public abstract Integer occupation();

    @SerializedName("is_smoker")
    @Nullable public abstract Boolean isSmoker();

    @SerializedName("city_of_residence")
    @Nullable public abstract String cityOfResidence();

    @SerializedName("country_of_residence")
    @Nullable public abstract String countryOfResidence();

    @SerializedName("small_bio")
    @Nullable public abstract String smallBio();

    @SerializedName("verified_account")
    public abstract Boolean verifiedAccount();

    @SerializedName("confirmations")
    public abstract Confirmations confirmations();

    @SerializedName("languages_attributes")
    public abstract List<LanguagesAttribute> languagesAttributes();

    @SerializedName("personality_traits_attributes")
    public abstract List<PersonalityTraitsAttribute> personalityTraitsAttributes();

    @SerializedName("pictures")
    public abstract List<Picture> pictures();

    @SerializedName("social")
    @Nullable public abstract Social social();

    public static User create(Integer id, Integer loginType, Integer nationality, String firstName, String lastName, String email,
                              Date birthDate, Integer biologicalSex, Integer occupation, Boolean isSmoker, String cityOfResidence,
                              String countryOfResidence, String smallBio, Boolean verifiedAccount, Confirmations confirmations,
                              List<LanguagesAttribute> languagesAttributes,
                              List<PersonalityTraitsAttribute> personalityTraitsAttributes,
                              List<Picture> pictures, Social social) {
        return new AutoValue_User(id, loginType, nationality, firstName, lastName, email, birthDate, biologicalSex, occupation,
                isSmoker, cityOfResidence, countryOfResidence, smallBio, verifiedAccount, confirmations, languagesAttributes,
                personalityTraitsAttributes, pictures, social);
    }

    public static TypeAdapter<User> typeAdapter(Gson gson) {
        return new AutoValue_User.GsonTypeAdapter(gson);
    }

    public abstract Builder toBuilder();

    public User withNationality(Integer nationality) {
        return toBuilder().setNationality(nationality).build();
    }

    public User withFirstName(String firstName) {
        return toBuilder().setFirstName(firstName).build();
    }

    public User withLastName(String lastName) {
        return toBuilder().setLastName(lastName).build();
    }

    public User withEmail(String email) {
        return toBuilder().setEmail(email).build();
    }

    public User withBirthDate(Date birthDate) {
        return toBuilder().setBirthDate(birthDate).build();
    }

    public User withBiologicalSex(Integer biologicalSex) {
        return toBuilder().setBiologicalSex(biologicalSex).build();
    }

    public User withOccupation(Integer withOccupation) {
        return toBuilder().setOccupation(withOccupation).build();
    }

    public User withIsSmoker(Boolean isSmoker) {
        return toBuilder().setIsSmoker(isSmoker).build();
    }

    public User withCityOfResidence(String cityOfResidence) {
        return toBuilder().setCityOfResidence(cityOfResidence).build();
    }

    public User withCountryOfResidence(String countryOfResidence) {
        return toBuilder().setCountryOfResidence(countryOfResidence).build();
    }

    public User withSmallBio(String smallBio) {
        return toBuilder().setSmallBio(smallBio).build();
    }

    public User withConfirmations(Confirmations confirmations) {
        return toBuilder().setConfirmations(confirmations).build();
    }

    public User withLanguagesAttributes(List<LanguagesAttribute> languagesAttributes) {
        return toBuilder().setLanguagesAttributes(languagesAttributes).build();
    }

    public User withPersonalityTraitsAttributes(List<PersonalityTraitsAttribute> personalityTraitsAttributes) {
        return toBuilder().setPersonalityTraitsAttributes(personalityTraitsAttributes).build();
    }

    public User withPictures(List<Picture> pictures) {
        return toBuilder().setPictures(pictures).build();
    }

    public User withSocial(Social social) {
        return toBuilder().setSocial(social).build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setId(Integer id);
        public abstract Builder setLoginType(Integer loginType);
        public abstract Builder setNationality(Integer nationality);
        public abstract Builder setFirstName(String firstName);
        public abstract Builder setLastName(String lastName);
        public abstract Builder setEmail(String email);
        public abstract Builder setBirthDate(Date birthDate);
        public abstract Builder setBiologicalSex(Integer biologicalSex);
        public abstract Builder setOccupation(Integer occupation);
        public abstract Builder setIsSmoker(Boolean isSmoker);
        public abstract Builder setCityOfResidence(String cityOfResidence);
        public abstract Builder setCountryOfResidence(String countryOfResidence);
        public abstract Builder setSmallBio(String smallBio);
        public abstract Builder setVerifiedAccount(Boolean verifiedAccount);
        public abstract Builder setConfirmations(Confirmations confirmations);
        public abstract Builder setLanguagesAttributes(List<LanguagesAttribute> languagesAttributes);
        public abstract Builder setPersonalityTraitsAttributes(List<PersonalityTraitsAttribute> personalityTraitsAttributes);
        public abstract Builder setPictures(List<Picture> pictures);
        public abstract Builder setSocial(Social social);
        public abstract User build();
    }

}
