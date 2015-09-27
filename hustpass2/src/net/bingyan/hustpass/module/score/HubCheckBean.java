package net.bingyan.hustpass.module.score;

import com.google.gson.annotations.Expose;

/**
 * Created by ant on 14-12-2.
 */
public class HubCheckBean {

    @Expose
    private Integer code;
    @Expose
    private String msg;
    @Expose
    private Object data;

    /**
     * @return The code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code The code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return The msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg The msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return The data
     */
    public Object getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(Object data) {
        this.data = data;
    }


}
