package cn.ocoop.framework.parse.exception;

/**
 * Created by liolay on 2017/12/8.
 */
public class UnknownOrderByException extends RuntimeException {
    public UnknownOrderByException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
