package nju.software.sjjh.bank.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接收是否成功反馈
 * Created by Nekonekod on 2017/4/17.
 */
@Data
@XStreamAlias("result")
public class Result {

    private Integer value;
    private String message;

    public Result() {
    }

    public Result(Integer value) {
        this.value = value;
    }

    public Result(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    /**
     * 接收成功 value=1 message=""
     */
    public static final Result defaultOK = new Result(1,"");

}
