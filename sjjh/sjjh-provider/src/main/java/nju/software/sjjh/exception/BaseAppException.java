package nju.software.sjjh.exception;

/**
 * 系统异常
 * Created by Nekonekod on 2017/4/26.
 */
public abstract class BaseAppException extends RuntimeException {

    public BaseAppException(String message) {
        super(message);
    }

    public BaseAppException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseAppException(Throwable cause) {
        super(cause);
    }

}
