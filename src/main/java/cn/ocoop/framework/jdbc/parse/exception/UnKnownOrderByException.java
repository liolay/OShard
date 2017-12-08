package cn.ocoop.framework.jdbc.parse.exception;

/**
 * Created by liolay on 2017/12/8.
 */
public class UnKnownOrderByException extends RuntimeException {
    public UnKnownOrderByException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
