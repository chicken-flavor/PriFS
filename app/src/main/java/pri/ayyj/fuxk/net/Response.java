package pri.ayyj.fuxk.net;

public class Response<T> {

    private T Result;
    private String Message;
    private boolean Success;

    public T getResult() {
        return Result;
    }

    public void setResult(T result) {
        Result = result;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }
}
