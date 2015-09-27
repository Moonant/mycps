package net.bingyan.hustpass.module.classroom;

import com.google.gson.annotations.Expose;

import java.util.Arrays;
import java.util.List;

/**
 * Created by harry on 15-3-7.
 */
public class ClassBeanV3 {

    @Expose
    private String status;
    @Expose
    private Data data;

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
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
        private AM AM;
        @Expose
        private NIT NIT;
        @Expose
        private PM PM;

        public String getClass(int i) {
            if (i == 1) {
                return AM.class1;
            } else if (i == 2) {
                return AM.class2;
            } else if (i == 3) {
                return PM.class1;
            } else if (i == 4) {
                return PM.class2;
            } else {
                return NIT.class1;
            }
        }

        public List<String> getfreeRoom(int i) {
            String lists = getClass(i);
            String[] rooms = lists.split("    ");
            return Arrays.asList(rooms);
        }

        /**
         * @return The AM
         */
        public AM getAM() {
            return AM;
        }

        /**
         * @param AM The AM
         */
        public void setAM(AM AM) {
            this.AM = AM;
        }

        /**
         * @return The NIT
         */
        public NIT getNIT() {
            return NIT;
        }

        /**
         * @param NIT The NIT
         */
        public void setNIT(NIT NIT) {
            this.NIT = NIT;
        }
    }

    public class AM {

        @Expose
        private String class2;
        @Expose
        private String class1;

        /**
         * @return The class2
         */
        public String getClass2() {
            return class2;
        }

        /**
         * @param class2 The class2
         */
        public void setClass2(String class2) {
            this.class2 = class2;
        }

        /**
         * @return The class1
         */
        public String getClass1() {
            return class1;
        }

        /**
         * @param class1 The class1
         */
        public void setClass1(String class1) {
            this.class1 = class1;
        }

    }

    public class PM {

        @Expose
        private String class2;
        @Expose
        private String class1;

        /**
         * @return The class2
         */
        public String getClass2() {
            return class2;
        }

        /**
         * @param class2 The class2
         */
        public void setClass2(String class2) {
            this.class2 = class2;
        }

        /**
         * @return The class1
         */
        public String getClass1() {
            return class1;
        }

        /**
         * @param class1 The class1
         */
        public void setClass1(String class1) {
            this.class1 = class1;
        }

    }

    public class NIT {

        @Expose
        private String class1;

        /**
         * @return The class1
         */
        public String getClass1() {
            return class1;
        }

        /**
         * @param class1 The class1
         */
        public void setClass1(String class1) {
            this.class1 = class1;
        }

    }
}