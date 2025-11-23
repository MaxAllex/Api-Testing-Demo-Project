package demo.api.dto.Users.response;

public record LogInUser(
    User user,
    String token
) {

}
