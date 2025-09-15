package shared.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import shared.response.Response;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "subclassType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = HiRequest.class, name = "hiRequest"),
        @JsonSubTypes.Type(value = LoginRequest.class, name = "loginRequest"),
        @JsonSubTypes.Type(value = RegisterRequest.class, name = "registerRequest"),
        @JsonSubTypes.Type(value = CreateBoardRequest.class, name = "createBoardRequest"),
        @JsonSubTypes.Type(value = ListBoardsRequest.class, name = "listBoardsRequest")
})
public interface Request {
    Response run(RequestHandler requestHandler);
}
