package json_toolkit;

public enum requestType {
    GET_ALL_BOOKS("GET_ALL_BOOKS"),
    SUCCESS("SUCCESS"),
    LOG_IN("LOG_IN"),
    LOG_OUT("LOG_OUT"),
    SELL_BOOK("SELL_BOOK"),
    ADD_NEW_USER("ADD_NEW_USER"),
    GET_USER_BOOKS("GET_USER_BOOKS"),
    USERNAME_NOT_FOUND("USERNAME_NOT_FOUND"),
    PASSWORD_INCORRECT("PASSWORD_INCORRECT"),
    GET_USER("GET_USER"),
    BUY_BOOKS("BUY_BOOKS"),
    NOT_SUCCESS("NOT_SUCCESS");

    private String name;

    private requestType(String name) {
        this.name = name;
    }

    public String getString() {
        return name;
    }
}
