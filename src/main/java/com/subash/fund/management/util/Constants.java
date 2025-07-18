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
    public final static String CREATE_ORDER = "createOrder";


    // API response
    public static final String CREATE_RECORD_SUCCESS = "Fund created successfully.";
    public static final Integer CREATE_RECORD_SUCCESS_CODE = 5001;
    public static final String RECORD_EXIST = "Fund already exists with the provided Fund ID.";
    public static final Integer RECORD_EXIST_CODE = 5002;
    public static final String UPDATE_RECORD_SUCCESS = "The fund's Net Asset Value (NAV) has been updated.";
    public static final Integer UPDATE_RECORD_SUCCESS_CODE = 5003;
    public static final String USER_RECORD_NOT_FOUND = "User not found for the given username.";
    public static final Integer USER_RECORD_NOT_FOUND_CODE = 5004;
    public static final String FUND_NAV_VALUE = "Fund NAV amount must correspond to today's date.";
    public static final Integer FUND_NAV_VALUE_CODE = 5005;
    public static final String RECORD_NOT_FOUND = "Requested fund details are unavailable.";
    public static final Integer RECORD_NOT_FOUND_CODE = 5006;
    public static final String ACCESS_DENIED = "Access denied: You are not authorized to create an order for another user.";
    public static final Integer ACCESS_DENIED_CODE = 403;

    public static final Integer INSUFFICIENT_UNITS_USER_CODE = 5008;
    public static final String INSUFFICIENT_UNITS_USER = "You do not have enough funds to place this sell order.";

    public static final Integer INSUFFICIENT_UNITS_FUNDS_CODE = 5009;
    public static final String INSUFFICIENT_UNITS_FUNDS = "Buy order failed: Insufficient funds.";

    public static final Integer ORDER_COMPLETED_CODE = 5010;
    public static final String ORDER_COMPLETED = "Order completed successfully";
    public final static String API_PROCESSED_FAILURE = "Error while processing the request";


    // Method
    public static final String POST_METHOD = "POST";
    public static final String PUT_METHOD = "PUT";

    //Response
    public static final String BAD_REQUEST = "[BAD REQUEST] : ";
    public static final String UNEXPECTED_ERROR = "[UNEXPECTED ERROR] : ";
    public static final String MALFORMED_JSON = "[MALFORMED JSON] : ";

    public enum OrderType {
        REDEEM,
        BUY
    }

}
