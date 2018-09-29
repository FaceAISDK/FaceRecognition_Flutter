package com.zenglb.framework.modulea.http.result;

import java.util.List;

/**
 * Created by zenglb on 2017/6/27.
 */

public class CustomWeatherResult {

    private DataBean data;
    private int status;
    private String message;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {

        private YesterdayBean yesterday;
        private String city;
        private String aqi;
        private String ganmao;
        private String wendu;
        private List<ForecastBean> forecast;

        public YesterdayBean getYesterday() {
            return yesterday;
        }

        public void setYesterday(YesterdayBean yesterday) {
            this.yesterday = yesterday;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getGanmao() {
            return ganmao;
        }

        public void setGanmao(String ganmao) {
            this.ganmao = ganmao;
        }

        public String getWendu() {
            return wendu;
        }

        public void setWendu(String wendu) {
            this.wendu = wendu;
        }

        public List<ForecastBean> getForecast() {
            return forecast;
        }

        public void setForecast(List<ForecastBean> forecast) {
            this.forecast = forecast;
        }


        public static class YesterdayBean {
            /**
             * date : 26日星期一
             * high : 高温 34℃
             * fx : 南风
             * low : 低温 21℃
             * fl : 微风
             * type : 多云
             */

            private String date;
            private String high;
            private String fx;
            private String low;
            private String fl;
            private String type;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getFx() {
                return fx;
            }

            public void setFx(String fx) {
                this.fx = fx;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getFl() {
                return fl;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }


            @Override
            public String toString() {
                return "YesterdayBean{" +
                        "date='" + date + '\'' +
                        ", high='" + high + '\'' +
                        ", fx='" + fx + '\'' +
                        ", low='" + low + '\'' +
                        ", fl='" + fl + '\'' +
                        ", type='" + type + '\'' +
                        '}';
            }
        }

        public static class ForecastBean {
            /**
             * date : 27日星期二
             * high : 高温 34℃
             * fengli : 微风级
             * low : 低温 22℃
             * fengxiang : 南风
             * type : 多云
             */

            private String date;
            private String high;
            private String fengli;
            private String low;
            private String fengxiang;
            private String type;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getFengli() {
                return fengli;
            }

            public void setFengli(String fengli) {
                this.fengli = fengli;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getFengxiang() {
                return fengxiang;
            }

            public void setFengxiang(String fengxiang) {
                this.fengxiang = fengxiang;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            @Override
            public String toString() {
                return "ForecastBean{" +
                        "date='" + date + '\'' +
                        ", high='" + high + '\'' +
                        ", fengli='" + fengli + '\'' +
                        ", low='" + low + '\'' +
                        ", fengxiang='" + fengxiang + '\'' +
                        ", type='" + type + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "yesterday=" + yesterday +
                    ", city='" + city + '\'' +
                    ", aqi='" + aqi + '\'' +
                    ", ganmao='" + ganmao + '\'' +
                    ", wendu='" + wendu + '\'' +
                    ", forecast=" + forecast +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CustomWeatherResult{" +
                "data=" + data +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
