package com.subash.fund.management.util;

/**
 * Utility class that holds constant values used across the Fund Management application.
 * <p>
 * This includes:
 * <ul>
 *   <li>Logging tags</li>
 *   <li>Operation identifiers</li>
 *   <li>Standard API response messages and codes</li>
 *   <li>HTTP method labels</li>
 * </ul>
 *
 */
public class Constants {

    // Constants for logger
    public final static String LOG_APP_NAME = "Fund Management";
    public final static String LOG_OPERATION_ID = "[OPERATION ID] : ";
    public final static String LOG_METHOD = "[HTTP METHOD] : ";
    public final static String LOG_REQUEST = "[REQUEST BODY] : ";
    public final static String LOG_RESPONSE = "[RESPONSE BODY] : ";
    public final static String LOG_FAILURE_MSG = "[FAILED TO LOG] : ";
    public final static String LOG_UUID = "[UUID] : ";
    public final static String LOG_STATUS = "[STATUS] : ";
    public final static String LOG_APP = "[APPLICATION] : ";

    public final static String LOG_MESSAGE = "[MESSAGE] : ";
    public final static String COMMA = ", ";

    // Operation Id
    public final static String CREATE_FUND = "createFund";
    public final static String UPDATE_FUND = "updateFund";
    public final static String GET_FUND = "getFund";

    public final static String GET_ALL_FUND = "getAllFund";
    public final static String REMOVE_FUND = "removeFund";

    // API response
    public static final String CREATE_RECORD_SUCCESS = "Fund created successfully";
    public static final Integer CREATE_RECORD_SUCCESS_CODE = 5001;
    public static final String RECORD_EXIST = "Fund exist already with FundId";
    public static final Integer RECORD_EXIST_CODE = 5002;
    public static final String UPDATE_RECORD_SUCCESS = "Update the funds nav";
    public static final Integer UPDATE_RECORD_SUCCESS_CODE = 5003;
    public static final String RECORD_FOUND = "Fund details found";
    public static final Integer RECORD_FOUND_CODE = 5004;

    public static final String RECORD_NOT_FOUND = "Fund details not found";
    public static final Integer RECORD_NOT_FOUND_CODE = 5006;


    public static final String RECORD_REMOVED = "Fund details removed";
    public static final Integer RECORD_REMOVED_CODE = 5005;

    public static final String ACCESS_DENIED = "Access denied";
    public static final Integer ACCESS_DENIED_CODE = 403;


    public final static String API_PROCESSED_FAILURE = "Error while processing the request";

    // Method
    public static final String POST_METHOD = "POST";
    public static final String GET_METHOD = "GET";
    public static final String DELETE_METHOD = "GET";
    public static final String PUT_METHOD = "PUT";

    //Response
    public static final String BAD_REQUEST = "[BAD REQUEST] : ";
    public static final String UNEXPECTED_ERROR = "[UNEXPECTED ERROR] : ";
    public static final String MALFORMED_JSON = "[MALFORMED JSON] : ";


}
