/*
 * File: ErrorMessageFactory.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.exception;

import android.content.Context;

import com.badi.R;
import com.badi.data.entity.APIError;
import com.badi.data.repository.remote.APIService;

import java.io.IOException;

import retrofit2.HttpException;

/**
 * Factory used to create error messages from an Exception as a condition.
 */
public class ErrorMessageFactory {

    private static final String ERROR_INVALID_TOKEN = "invalid_token";
    private static final String ERROR_INVALID_CONTEXT = "invalid_context";
    private static final String ERROR_INVALID_OPERATION = "invalid_operation";
    private static final String ERROR_INVALID_CLIENT = "invalid_client";
    private static final String ERROR_INVALID_GRANT = "invalid_grant";
    private static final String ERROR_INVALID_REQUEST = "invalid_request";
    private static final String ERROR_INVALID_REDIRECT_URI = "invalid_redirect_uri";
    private static final String ERROR_UNAUTHORIZED_CLIENT = "unauthorized_client";
    private static final String ERROR_ACCESS_DENIED = "access_denied";
    private static final String ERROR_INVALID_SCOPE = "invalid_scope";
    private static final String ERROR_SERVER_ERROR = "server_error";
    private static final String ERROR_TEMPORARILY_UNAVAILABLE = "temporarily_unavailable";
    private static final String ERROR_UNSUPPORTED_GRANT_TYPE = "unsupported_grant_type";
    private static final String ERROR_RESOURCE_NOT_FOUND = "resource_not_found";
    private static final String ERROR_RESOURCE_ALREADY_CONFIRMED_BY_ANOTHER_USER = "resource_already_confirmed_by_another_user";
    private static final String ERROR_NON_AVAILABLE = "non_available";
    private static final String ERROR_FORBIDDEN_OPERATION = "forbidden_operation";
    private static final String ERROR_INVALID_PARAMS = "invalid_params";
    private static final String ERROR_INVALID_RECORD = "invalid_record";
    private static final String ERROR_INVALID_FACEBOOK_TOKEN = "invalid_facebook_token";
    private static final String ERROR_INVALID_SMS_REQUEST = "invalid_sms_request";
    private static final String ERROR_CONCURRENT_VERIFICATION = "concurrent_verification";
    private static final String ERROR_UNKNOWN_SMS_EXCEPTION = "unknown_sms_exception";
    private static final String ERROR_NO_MATCH_LOGIN_TYPE = "no_match_login_type";
    private static final String ERROR_EMAIL_NOT_FOUND = "email_not_found";
    private static final String ERROR_EMAIL_EXISTS = "email_exists";
    private static final String ERROR_FACEBOOK_LOGIN_EXISTS = "facebook_login_exists";
    private static final String ERROR_GOOGLE_LOGIN_EXISTS = "google_login_exists";
    private static final String ERROR_INCORRECT_PASSWORD = "incorrect_password";
    private static final String ERROR_INVALID_POSTAL_CODE = "invalid_postal_code";
    private static final String ERROR_INVALID_ACCOUNT_NUMBER = "invalid_account_number";
    private static final String ERROR_INVALID_ACCOUNT_COUNTRY = "invalid_account_country";
    private static final String ERROR_UNKNOWN_EXCEPTION = "unknown_exception";
    private static final String ERROR_NO_MATCH_PASSWORD_CONFIRMATION = "no_match_password_confirmation";
    private static final String ERROR_REQUEST_ALREADY_ANSWERED = "request_already_answered";
    private static final String ERROR_REQUEST_NON_ARCHIVABLE = "request_non_archivable";
    private static final String ERROR_ROOM_IS_FROZEN = "room_is_frozen";
    private static final String ERROR_USER_WITH_NO_PAYMENT_DATA = "user_with_no_payment_data";
    private static final String ERROR_USER_WITH_NO_PAYOUT_DATA = "user_with_no_payout_data";
    private static final String ERROR_BOOKING_PROPOSAL_IN_PROGRESS = "booking_proposal_in_progress";
    private static final String ERROR_CANT_LEAVE_CHAT = "cant_leave_chat";
    private static final String ERROR_CANT_BLOCK_CHAT = "cant_block_chat";

    /**
     * Stripe error codes
     */

    private static final String ERROR_STRIPE_INVALID_NUMBER = "invalid_number";
    private static final String ERROR_STRIPE_INVALID_EXPIRY_MONTH = "invalid_expiry_month";
    private static final String ERROR_STRIPE_INVALID_EXPIRY_YEAR = "invalid_expiry_year";
    private static final String ERROR_STRIPE_INVALID_CVC = "invalid_cvc";
    private static final String ERROR_STRIPE_INVALID_SWIPE_DATA = "invalid_swipe_data";
    private static final String ERROR_STRIPE_INCORRECT_NUMBER = "incorrect_number";
    private static final String ERROR_STRIPE_EXPIRED_CARD = "expired_card";
    private static final String ERROR_STRIPE_INCORRECT_CVC = "incorrect_cvc";
    private static final String ERROR_STRIPE_INCORRECT_ZIP = "incorrect_zip";
    private static final String ERROR_STRIPE_CARD_DECLINED = "card_declined";
    private static final String ERROR_STRIPE_MISSING = "missing";
    private static final String ERROR_STRIPE_PROCESSING_ERROR = "processing_error";

    private ErrorMessageFactory() {
        //empty
    }

    /**
     * Creates a String representing an error message.
     *
     * @param context Context needed to retrieve string resources.
     * @param exception An exception used as a condition to retrieve the correct error message.
     * @return {@link String} an error message.
     */
    public static String create(Context context, Exception exception) {
        String message;

        if (exception instanceof HttpException) {
            HttpException error = (HttpException) exception;
            try {
                APIError apiError = APIService.Creator.gson.fromJson(error.response().errorBody().string(), APIError.class);
                switch (apiError.error().code()) {
                    case ERROR_INVALID_TOKEN:
                        message = context.getString(R.string.error_authentication);
                        break;
                    case ERROR_INVALID_CONTEXT:
                        message = context.getString(R.string.error_invalid_context);
                        break;
                    case ERROR_INVALID_OPERATION:
                        message = context.getString(R.string.error_no_access);
                        break;
                    case ERROR_INVALID_CLIENT:
                        message = context.getString(R.string.error_authentication);
                        break;
                    case ERROR_INVALID_GRANT:
                        message = context.getString(R.string.error_authentication);
                        break;
                    case ERROR_INVALID_REQUEST:
                        message = context.getString(R.string.error_authentication);
                        break;
                    case ERROR_INVALID_REDIRECT_URI:
                        message = context.getString(R.string.error_authentication);
                        break;
                    case ERROR_UNAUTHORIZED_CLIENT:
                        message = context.getString(R.string.error_authentication);
                        break;
                    case ERROR_ACCESS_DENIED:
                        message = context.getString(R.string.error_authentication);
                        break;
                    case ERROR_INVALID_SCOPE:
                        message = context.getString(R.string.error_authentication);
                        break;
                    case ERROR_SERVER_ERROR:
                        message = context.getString(R.string.error_authentication);
                        break;
                    case ERROR_TEMPORARILY_UNAVAILABLE:
                        message = context.getString(R.string.error_authentication);
                        break;
                    case ERROR_UNSUPPORTED_GRANT_TYPE:
                        message = context.getString(R.string.error_authentication);
                        break;
                    case ERROR_RESOURCE_NOT_FOUND:
                        message = context.getString(R.string.error_resource_not_found);
                        break;
                    case ERROR_RESOURCE_ALREADY_CONFIRMED_BY_ANOTHER_USER:
                        message = context.getString(R.string.error_resource_already_confirmed_by_another_user);
                        break;
                    case ERROR_NON_AVAILABLE:
                        message = context.getString(R.string.error_non_available);
                        break;
                    case ERROR_FORBIDDEN_OPERATION:
                        message = context.getString(R.string.error_forbidden_operation);
                        break;
                    case ERROR_INVALID_PARAMS:
                        message = context.getString(R.string.error_invalid_params);
                        break;
                    case ERROR_INVALID_RECORD:
                        message = context.getString(R.string.error_invalid_record);
                        break;
                    case ERROR_INVALID_FACEBOOK_TOKEN:
                        message = context.getString(R.string.error_invalid_facebook_token);
                        break;
                    case ERROR_INVALID_SMS_REQUEST:
                        message = context.getString(R.string.error_invalid_sms_request);
                        break;
                    case ERROR_CONCURRENT_VERIFICATION:
                        message = context.getString(R.string.error_concurrent_verification_sms);
                        break;
                    case ERROR_UNKNOWN_SMS_EXCEPTION:
                        message = context.getString(R.string.error_unknown_sms_verification);
                        break;
                    case ERROR_NO_MATCH_LOGIN_TYPE:
                        message = context.getString(R.string.error_no_match_login_type);
                        break;
                    case ERROR_EMAIL_NOT_FOUND:
                        message = context.getString(R.string.error_email_not_found);
                        break;
                    case ERROR_EMAIL_EXISTS:
                        message = context.getString(R.string.error_email_already_exists);
                        break;
                    case ERROR_FACEBOOK_LOGIN_EXISTS:
                        message = context.getString(R.string.error_facebook_login_exists);
                        break;
                    case ERROR_GOOGLE_LOGIN_EXISTS:
                        message = context.getString(R.string.error_google_login_exists);
                        break;
                    case ERROR_INCORRECT_PASSWORD:
                        message = context.getString(R.string.error_incorrect_password);
                        break;
                    case ERROR_INVALID_POSTAL_CODE:
                        message = context.getString(R.string.error_invalid_postal_code);
                        break;
                    case ERROR_INVALID_ACCOUNT_NUMBER:
                        message = context.getString(R.string.error_invalid_account_number);
                        break;
                    case ERROR_INVALID_ACCOUNT_COUNTRY:
                        message = context.getString(R.string.error_invalid_account_country);
                        break;
                    case ERROR_UNKNOWN_EXCEPTION:
                        message = context.getString(R.string.error_unknown_exception);
                        break;
                    case ERROR_NO_MATCH_PASSWORD_CONFIRMATION:
                        message = context.getString(R.string.error_no_match_password_confirmation);
                        break;
                    case ERROR_REQUEST_ALREADY_ANSWERED:
                        message = context.getString(R.string.error_request_already_answered);
                        break;
                    case ERROR_REQUEST_NON_ARCHIVABLE:
                        message = context.getString(R.string.error_request_non_archivable);
                        break;
                    case ERROR_ROOM_IS_FROZEN:
                        message = context.getString(R.string.error_room_is_frozen);
                        break;
                    case ERROR_USER_WITH_NO_PAYMENT_DATA:
                        message = context.getString(R.string.error_user_with_no_payment_data);
                        break;
                    case ERROR_USER_WITH_NO_PAYOUT_DATA:
                        message = context.getString(R.string.error_user_with_no_payout_data);
                        break;
                    case ERROR_BOOKING_PROPOSAL_IN_PROGRESS:
                        message = context.getString(R.string.error_booking_proposal_in_progress);
                        break;
                    case ERROR_CANT_LEAVE_CHAT:
                        message = context.getString(R.string.error_cant_leave_chat);
                        break;
                    case ERROR_CANT_BLOCK_CHAT:
                        message = context.getString(R.string.error_cant_block_chat);
                        break;
                    case ERROR_STRIPE_INVALID_NUMBER:
                        message = context.getString(R.string.error_stripe_invalid_number);
                        break;
                    case ERROR_STRIPE_INVALID_EXPIRY_MONTH:
                        message = context.getString(R.string.error_stripe_invalid_expiry_month);
                        break;
                    case ERROR_STRIPE_INVALID_EXPIRY_YEAR:
                        message = context.getString(R.string.error_stripe_invalid_expiry_year);
                        break;
                    case ERROR_STRIPE_INVALID_CVC:
                        message = context.getString(R.string.error_stripe_invalid_cvc);
                        break;
                    case ERROR_STRIPE_INVALID_SWIPE_DATA:
                        message = context.getString(R.string.error_stripe_invalid_swipe_data);
                        break;
                    case ERROR_STRIPE_INCORRECT_NUMBER:
                        message = context.getString(R.string.error_stripe_incorrect_number);
                        break;
                    case ERROR_STRIPE_EXPIRED_CARD:
                        message = context.getString(R.string.error_stripe_expired_card);
                        break;
                    case ERROR_STRIPE_INCORRECT_CVC:
                        message = context.getString(R.string.error_stripe_incorrect_cvc);
                        break;
                    case ERROR_STRIPE_INCORRECT_ZIP:
                        message = context.getString(R.string.error_stripe_incorrect_zip);
                        break;
                    case ERROR_STRIPE_CARD_DECLINED:
                        message = context.getString(R.string.error_stripe_card_declined);
                        break;
                    case ERROR_STRIPE_MISSING:
                        message = context.getString(R.string.error_stripe_missing);
                        break;
                    case ERROR_STRIPE_PROCESSING_ERROR:
                        message = context.getString(R.string.error_stripe_processing_error);
                        break;
                    default:
                        message = context.getString(R.string.exception_message_generic);
                }
            } catch (Exception e) {
                message = context.getString(R.string.exception_message_unexpected_error);
            }
        } else if (exception instanceof IOException) {
            //handle network error
            message = context.getString(R.string.exception_message_no_connection);
        } else if (exception instanceof IllegalStateException) {
            // java.lang.IllegalStateException: closed when trying to access response in onResponse(Response response)
            // https://github.com/square/okhttp/issues/1240
            // You can only call string() once.
            IllegalStateException error = (IllegalStateException) exception;
            if (error.getMessage().equals("closed"))
                message = context.getString(R.string.error_incorrect_password);
            else
                message = context.getString(R.string.exception_message_unexpected_error);
        } else {
            message = context.getString(R.string.exception_message_unexpected_error);
        }

        return message;
    }
}
