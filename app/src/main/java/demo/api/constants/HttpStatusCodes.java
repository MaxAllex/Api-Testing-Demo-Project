package demo.api.constants;

public enum HttpStatusCodes {
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NO_CONTENT(204),
    PARTIAL_CONTENT(206),
    MULTI_STATUS(207),
    ALREADY_REPORTED(208),
    IM_USED(226),
    MULTIPLE_CHOICES(300),
    MOVED_PERMANENTLY(301),
    FOUND(302),
    SEE_OTHER(303),
    NOT_MODIFIED(304),
    USE_PROXY(305),
    TEMPORARY_REDIRECT(307),
    PERMANENT_REDIRECT(308),

    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    CONFLICT(409),
    UNPROCESSABLE_ENTITY(422),

    INTERNAL_SERVER_ERROR(500),
    BAD_GATEWAY(502),
    SERVICE_UNAVAILABLE(503),
    GATEWAY_TIMEOUT(504);

    private final int value;

    HttpStatusCodes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int value() {
        return value;
    }

    public static HttpStatusCodes fromCode(int code) {
        for (HttpStatusCodes statusCode : values()) {
            if (statusCode.value == code) {
                return statusCode;
            }
        }
        return null;
    }
}
