package shared.request;


import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;
@JsonTypeName("loginRequest")
public class LoginRequest implements Request{

    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleLoginRequest(this);
    }
}
