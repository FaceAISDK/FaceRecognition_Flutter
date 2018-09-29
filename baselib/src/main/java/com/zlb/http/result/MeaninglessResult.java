package com.zlb.http.result;

/**
 * 有些API result 字段返回的没有任何的意义，就传这个吧
 *
 * 比如检查手机是否存在，返回的code 就可以明确的表示意义所在，不需要Result 来额外的表示什么！
 *
 * Created by anylife.zlb@gmail.com on 2016/7/11.
 */
public class MeaninglessResult {
    private String helloWord="惊艳了岁月，温暖了时光";

    public String getHelloWord() {
        return helloWord;
    }

    public void setHelloWord(String helloWord) {
        this.helloWord = helloWord;
    }

    @Override
    public String toString() {
        return "EasyResponse{" +
                "helloWord='" + helloWord + '\'' +
                '}';
    }
}
