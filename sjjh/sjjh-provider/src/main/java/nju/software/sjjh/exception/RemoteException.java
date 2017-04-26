package nju.software.sjjh.exception;

/**
 * 调用第三方发生异常
 * Created by Nekonekod on 2017/4/26.
 */
public class RemoteException extends BaseAppException{


    public RemoteException(String message) {
        super(message);
    }

    public RemoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteException(Throwable cause) {
        super(cause);
    }

}
