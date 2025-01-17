package wakeb.example.microservice.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a problem detail response adhering to the RFC 7807 standard.
 * It provides information about errors in a machine-readable format.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ProblemDetail {

    @JsonProperty("type")
    private String type;

    @JsonProperty("title")
    private String title;

    @JsonProperty("status")
    private int status;

    @JsonProperty("detail")
    private String detail;

    @JsonProperty("instance")
    private String instance;

    @JsonProperty("errors")
    private List<String> errors;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("errorCode")
    private String errorCode;

    /**
     * Default constructor that initializes the timestamp to the current time.
     */
    public ProblemDetail() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructs a ProblemDetail with the provided details.
     *
     * @param type     the URI reference that identifies the problem type.
     * @param title    a short, human-readable summary of the problem type.
     * @param status   the HTTP status represented as an {@link HttpStatus}.
     * @param detail   a human-readable explanation specific to this occurrence of the problem.
     * @param instance a URI reference that identifies the specific occurrence of the problem.
     * @param errors   a list of error messages or details.
     */
    public ProblemDetail(String type, String title, HttpStatus status,
                         String detail, String instance, List<String> errors) {
        this.type = type;
        this.title = title;
        this.status = status.value();
        this.detail = detail;
        this.instance = instance;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructs a ProblemDetail with the provided details, including a machine-readable error code.
     *
     * @param type      the URI reference that identifies the problem type.
     * @param title     a short, human-readable summary of the problem type.
     * @param status    the HTTP status represented as an {@link HttpStatus}.
     * @param detail    a human-readable explanation specific to this occurrence of the problem.
     * @param instance  a URI reference that identifies the specific occurrence of the problem.
     * @param errors    a list of error messages or details.
     * @param errorCode a machine-readable error code.
     */
    public ProblemDetail(String type, String title, HttpStatus status,
                         String detail, String instance, List<String> errors,
                         String errorCode) {
        this(type, title, status, detail, instance, errors);
        this.errorCode = errorCode;
    }
}