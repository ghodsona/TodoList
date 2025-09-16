package shared.response;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "subclassType"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = HiResponse.class, name = "hiResponse"),
        @JsonSubTypes.Type(value = ActionResponse.class, name = "actionResponse"),
        @JsonSubTypes.Type(value = ListBoardsResponse.class, name = "listBoardsResponse"),
        @JsonSubTypes.Type(value = ViewBoardResponse.class, name = "viewBoardResponse")
})
public interface Response {
    void run(ResponseHandler responseHandler);
}
