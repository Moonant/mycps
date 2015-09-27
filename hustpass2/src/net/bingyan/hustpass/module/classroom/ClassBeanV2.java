package net.bingyan.hustpass.module.classroom;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ant on 14-12-3.
 */
public class ClassBeanV2 {

    @Expose
    private String state;
    @Expose
    private Data data;

    /**
     * @return The state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state The state
     */
    public void setState(String state) {
        this.state = state;
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
        private List<List<String>> class4 = new ArrayList<List<String>>();
        @Expose
        private List<List<String>> class5 = new ArrayList<List<String>>();
        @Expose
        private List<List<String>> class2 = new ArrayList<List<String>>();
        @Expose
        private List<List<String>> class3 = new ArrayList<List<String>>();
        @Expose
        private List<List<String>> class1 = new ArrayList<List<String>>();


        public List<List<String>> getClass(int i) {
            if (i == 1) {
                return class1;
            } else if (i == 2) {
                return class2;
            } else if (i == 3) {
                return class3;
            } else if (i == 4) {
                return class4;
            } else {
                return class5;
            }
        }

        public List<String> getfreeRoom(int i) {
            List<String> data = new ArrayList<String>();
            List<List<String>> lists = getClass(i);
            for(List<String> list : lists){
                data.addAll(list);
            }
            return data;
        }

        /**
         * @return The class4
         */
        public List<List<String>> getClass4() {
            return class4;
        }

        /**
         * @param class4 The class4
         */
        public void setClass4(List<List<String>> class4) {
            this.class4 = class4;
        }

        /**
         * @return The class5
         */
        public List<List<String>> getClass5() {
            return class5;
        }

        /**
         * @param class5 The class5
         */
        public void setClass5(List<List<String>> class5) {
            this.class5 = class5;
        }

        /**
         * @return The class2
         */
        public List<List<String>> getClass2() {
            return class2;
        }

        /**
         * @param class2 The class2
         */
        public void setClass2(List<List<String>> class2) {
            this.class2 = class2;
        }

        /**
         * @return The class3
         */
        public List<List<String>> getClass3() {
            return class3;
        }

        /**
         * @param class3 The class3
         */
        public void setClass3(List<List<String>> class3) {
            this.class3 = class3;
        }

        /**
         * @return The class1
         */
        public List<List<String>> getClass1() {
            return class1;
        }

        /**
         * @param class1 The class1
         */
        public void setClass1(List<List<String>> class1) {
            this.class1 = class1;
        }

    }
}
