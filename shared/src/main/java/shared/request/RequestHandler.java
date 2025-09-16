package shared.request;

import shared.response.Response;

public interface RequestHandler {
    Response handleHiRequest(HiRequest hiRequest);
    Response handleLoginRequest(LoginRequest loginRequest);
    Response handleRegisterRequest(RegisterRequest registerRequest);
    Response handleCreateBoardRequest(CreateBoardRequest createBoardRequest);
    Response handleListBoardsRequest(ListBoardsRequest listBoardsRequest);
    Response handleAddUserToBoardRequest(AddUserToBoardRequest request);
    Response handleViewBoardRequest(ViewBoardRequest request);
    Response handleAddTaskRequest(AddTaskRequest request);
    Response handleListTasksRequest(ListTasksRequest request);
    Response handleUpdateTaskStatusRequest(UpdateTaskStatusRequest request);
    Response handleDeleteTaskRequest(DeleteTaskRequest request);
}
