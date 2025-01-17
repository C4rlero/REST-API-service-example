package wakeb.example.microservice.dto.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;
import java.util.List;

/**
 * Utility class for mapping error details to a {@link ProblemDetail} instance
 * following the RFC 7807 standard.
 */
public class ProblemDetailMapper {

    /**
     * Creates a {@link ProblemDetail} instance with full error details.
     *
     * @param type      the URI reference that identifies the problem type.
     * @param title     a short, human-readable summary of the problem type.
     * @param status    the HTTP status of the error.
     * @param detail    a detailed description of the error.
     * @param request   the current web request, used to extract the request instance.
     * @param errors    a list of specific error messages.
     * @param errorCode a machine-readable error code.
     * @return a new instance of {@link ProblemDetail} populated with the provided information.
     */
    public static ProblemDetail toProblemDetail(String type,
                                                String title,
                                                HttpStatus status,
                                                String detail,
                                                WebRequest request,
                                                List<String> errors,
                                                String errorCode) {
        String instance = (request != null)
                ? request.getDescription(false).replace("uri=", "")
                : "";
        return new ProblemDetail(type, title, status, detail, instance, errors, errorCode);
    }

    /**
     * Creates a {@link ProblemDetail} instance with basic error details.
     *
     * @param type    the URI reference that identifies the problem type.
     * @param title   a short, human-readable summary of the problem type.
     * @param status  the HTTP status of the error.
     * @param detail  a detailed description of the error.
     * @param request the current web request, used to extract the request instance.
     * @return a new instance of {@link ProblemDetail} populated with the provided information.
     */
    public static ProblemDetail toProblemDetail(String type,
                                                String title,
                                                HttpStatus status,
                                                String detail,
                                                WebRequest request) {
        return toProblemDetail(type, title, status, detail, request, null, null);
    }
}