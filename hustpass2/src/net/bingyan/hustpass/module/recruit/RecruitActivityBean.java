package net.bingyan.hustpass.module.recruit;

import java.util.List;

/**
 * Created by jinge on 14-9-10.
 */
public class RecruitActivityBean {
    int code;
    String msg;
    List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getFirst() {
        if(data == null || data.get(0) == null) {
            return 0;
        }
        data.get(0).getId();
        return data.get(0).getId();
    }

    public int getSecond() {
        if(data == null || data.size() <= 1 || data.get(1) == null) {
            return 0;
        }
        return data.get(1).getId();
    }

    class Data {
        int id;
        String adate;
        String acontent;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAdate() {
            return adate;
        }

        public void setAdate(String adate) {
            this.adate = adate;
        }

        public String getAcontent() {
            return acontent;
        }

        public void setAcontent(String acontent) {
            this.acontent = acontent;
        }
    }
}
