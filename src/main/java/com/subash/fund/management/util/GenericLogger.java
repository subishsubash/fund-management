package com.subash.fund.management.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Utility component for structured and conditional logging of API requests and responses.
 * <p>
 * This class uses Jackson's {@link ObjectMapper} to serialize objects and Log4j for logging.
 * Logging is controlled via application properties:
 * <ul>
 *     <li>{@code print.log.enable.request=true} to enable request logging</li>
 *     <li>{@code print.log.enable.response=true} to enable response logging</li>
 * </ul>
 *
 */
@Component
public class GenericLogger {

    private final static String COMMA = ", ";

    @Value("${print.log.enable.request}")
    private boolean logRequest;
    @Value("${print.log.enable.response}")
    private boolean logResponse;

    /**
     * Logs incoming API requests with UUID, operation ID, HTTP method, and request body.
     *
     * @param logger       the {@link Logger} to log into
     * @param UUID         the unique identifier for this request flow
     * @param operationId  the operation name (e.g., "createUser")
     * @param method       the HTTP method used (e.g., "POST")
     * @param requestBody  the actual request body object
     */
    public void logRequest(Logger logger, String UUID, String operationId, String method, Object requestBody) {
        if (logRequest) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String requestBodyString = mapper.writeValueAsString(requestBody);
                StringBuffer logMsg = new StringBuffer(UUID);
                logMsg.append(COMMA).append(Constants.LOG_APP).append(Constants.LOG_APP_NAME).append(COMMA).append(Constants.LOG_OPERATION_ID)
                        .append(operationId).append(COMMA).append(Constants.LOG_METHOD).append(method)
                        .append(COMMA).append(Constants.LOG_REQUEST)
                        .append(requestBodyString);
                logger.info(logMsg.toString());
            } catch (Exception e) {
                logger.info(UUID + COMMA + Constants.LOG_FAILURE_MSG + e.getMessage());
            }
        }
    }

    /**
     * Logs outgoing API responses with UUID, status, and response object.
     *
     * @param logger          the {@link Logger} to log into
     * @param UUID            the unique identifier for this request flow
     * @param status          the result status (e.g., "SUCCESS", "ERROR")
     * @param responseObject  the actual response object to be logged
     */
    public void logResponse(Logger logger, String UUID, String status, Object responseObject) {
        if (logResponse) {
            try {
                StringBuffer logMsg = new StringBuffer(UUID);
                ObjectMapper mapper = new ObjectMapper();
                String responseObjectString = mapper.writeValueAsString(responseObject);
                logMsg.append(COMMA).append(Constants.LOG_APP).append(Constants.LOG_APP_NAME).append(COMMA).append(Constants.LOG_STATUS)
                        .append(status).append(COMMA).append(Constants.LOG_RESPONSE).append(responseObjectString);
                logger.info(logMsg.toString());
            } catch (Exception e) {
                logger.info(UUID + COMMA + Constants.LOG_FAILURE_MSG + e.getMessage());
            }
        }
    }

    /**
     * Generates a formatted UUID string for tracing API requests across logs.
     *
     * @return a formatted string like {@code [UUID] : 123e4567-e89b-12d3-a456-426614174000}
     */
    public static String getUUID() {
        StringBuffer UUIDString = new StringBuffer(Constants.LOG_UUID);
        UUID uuid = UUID.randomUUID();
        UUIDString.append(uuid);
        return UUIDString.toString();
    }
}
