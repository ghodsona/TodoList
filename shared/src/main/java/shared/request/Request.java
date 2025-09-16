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
        @JsonSubTypes.Type(value = ListBoardsRequest.class, name = "listBoardsRequest"),
        @JsonSubTypes.Type(value = AddUserToBoardRequest.class, name = "addUserToBoardRequest"),
        @JsonSubTypes.Type(value = ViewBoardRequest.class, name = "viewBoardRequest"),
        @JsonSubTypes.Type(value = AddTaskRequest.class, name = "addTaskRequest"),
        @JsonSubTypes.Type(value = DeleteTaskRequest.class, name = "deleteTaskRequest"),
        @JsonSubTypes.Type(value = ListTasksRequest.class, name = "listTasksRequest"),
        @JsonSubTypes.Type(value = UpdateTaskStatusRequest.class, name = "updateTaskStatusRequest"),
        @JsonSubTypes.Type(value = LogoutRequest.class, name = "logoutRequest")
})
public interface Request {
    Response run(RequestHandler requestHandler);
}
