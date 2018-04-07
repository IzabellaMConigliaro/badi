/*
 * File: RoomRequest.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.room;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * {@link RoomRequest} model used for mapping the response of a room request acceptance/rejection with the server.
 */
@AutoValue
public abstract class RoomRequest {

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("room_id")
    public abstract Integer roomId();

    @SerializedName("user_id")
    public abstract Integer userId();

    @SerializedName("request_date")
    public abstract Date requestDate();

    @SerializedName("response_date")
    public abstract Date responseDate();

    @SerializedName("is_accepted")
    public abstract Boolean isAccepted();

    @SerializedName("created_at")
    public abstract Date createdAt();

    @SerializedName("updated_at")
    public abstract Date updatedAt();

    @SerializedName("deleted_at")
    public abstract Date deletedAt();

    public static RoomRequest create(Integer id, Integer roomId, Integer userId, Date requestDate, Date responseDate,
                                     Boolean isAccepted, Date createdAt, Date updatedAt, Date deletedAt) {
        return new AutoValue_RoomRequest(id, roomId, userId, requestDate, responseDate, isAccepted, createdAt, updatedAt,
                deletedAt);
    }

    public static TypeAdapter<RoomRequest> typeAdapter(Gson gson) {
        return new AutoValue_RoomRequest.GsonTypeAdapter(gson);
    }
}
