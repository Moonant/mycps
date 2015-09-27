package net.bingyan.hustpass.module.score;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ant on 14-12-2.
 */


public class ScoreBeanV2 {

    @Expose
    private String msg;
    @Expose
    private Integer code;
    @Expose
    private Data data;

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
     * @return The data
     */
    public Data getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @Expose
        private Information information;
        @Expose
        private List<Score> score = new ArrayList<Score>();
        @Expose
        private String weightedscore;
        @Expose
        private String time;

        /**
         * @return The information
         */
        public Information getInformation() {
            return information;
        }

        /**
         * @param information The information
         */
        public void setInformation(Information information) {
            this.information = information;
        }

        /**
         * @return The score
         */
        public List<Score> getScore() {
            return score;
        }

        /**
         * @param score The score
         */
        public void setScore(List<Score> score) {
            this.score = score;
        }

        /**
         * @return The weightedscore
         */
        public String getWeightedscore() {
            return weightedscore;
        }

        /**
         * @param weightedscore The weightedscore
         */
        public void setWeightedscore(String weightedscore) {
            this.weightedscore = weightedscore;
        }

        /**
         * @return The time
         */
        public String getTime() {
            return time;
        }

        /**
         * @param time The time
         */
        public void setTime(String time) {
            this.time = time;
        }


        public class Information {

            @Expose
            private String id;
            @Expose
            private String college;
            @SerializedName("class")
            @Expose
            private String _class;
            @Expose
            private String name;

            /**
             * @return The id
             */
            public String getId() {
                return id;
            }

            /**
             * @param id The id
             */
            public void setId(String id) {
                this.id = id;
            }

            /**
             * @return The college
             */
            public String getCollege() {
                return college;
            }

            /**
             * @param college The college
             */
            public void setCollege(String college) {
                this.college = college;
            }

            /**
             * @return The _class
             */
            public String getClass_() {
                return _class;
            }

            /**
             * @param _class The class
             */
            public void setClass_(String _class) {
                this._class = _class;
            }

            /**
             * @return The name
             */
            public String getName() {
                return name;
            }

            /**
             * @param name The name
             */
            public void setName(String name) {
                this.name = name;
            }

        }

        public class Score {

            @Expose
            private String credit;
            @Expose
            private Object remark;
            @Expose
            private String score;
            @Expose
            private String name;

            /**
             * @return The credit
             */
            public String getCredit() {
                return credit;
            }

            /**
             * @param credit The credit
             */
            public void setCredit(String credit) {
                this.credit = credit;
            }

            /**
             * @return The remark
             */
            public Object getRemark() {
                return remark;
            }

            /**
             * @param remark The remark
             */
            public void setRemark(Object remark) {
                this.remark = remark;
            }

            /**
             * @return The score
             */
            public String getScore() {
                return score;
            }

            /**
             * @param score The score
             */
            public void setScore(String score) {
                this.score = score;
            }

            /**
             * @return The name
             */
            public String getName() {
                return name;
            }

            /**
             * @param name The name
             */
            public void setName(String name) {
                this.name = name;
            }


        }
    }
}
