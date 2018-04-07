/*
 * File: UserUpdate.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.user;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * {@link UserUpdate} used for mapping the JSON Request to the server for tracking the user meta at the endpoint /me.
 */
@AutoValue
public abstract class UserUpdate implements Parcelable {

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
    public abstract List<Integer> pictures();

    @SerializedName("social")
    public abstract Social social();

    public static UserUpdate create(Integer id, Integer loginType, Integer nationality, String firstName, String lastName,
                                    String email, Date birthDate, Integer biologicalSex, Integer occupation, Boolean isSmoker,
                                    String cityOfResidence, String countryOfResidence, String smallBio, Boolean verifiedAccount,
                                    Confirmations confirmations, List<LanguagesAttribute> languagesAttributes,
                                    List<PersonalityTraitsAttribute> personalityTraitsAttributes,
                                    List<Integer> pictures, Social social) {
        return new AutoValue_UserUpdate(id, loginType, nationality, firstName, lastName, email, birthDate, biologicalSex,
                occupation, isSmoker, cityOfResidence, countryOfResidence, smallBio, verifiedAccount, confirmations,
                languagesAttributes, personalityTraitsAttributes, pictures, social);
    }

    public static TypeAdapter<UserUpdate> typeAdapter(Gson gson) {
        return new AutoValue_UserUpdate.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_UserUpdate.Builder();
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
        public abstract Builder setPictures(List<Integer> pictures);
        public abstract Builder setSocial(Social social);
        public abstract UserUpdate build();
    }

}
