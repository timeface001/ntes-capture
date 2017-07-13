package com.fs.dreams;

import java.util.Map;

/**
 * @author fengsong
 * @description:一句话描述下类的功能
 * @time 2017-07-13 9:29
 **/
public class NtesApiRequest {
    /**
     * 请求方式
     */
    private int type;

    /**
     * 具体路径
     */
    private String url;

    /***
     * post请求参数
     */
    private Map<String, String> postParams;

    public NtesApiRequest(int type, String url, Map<String, String> postParams) {
        this.type = type;
        this.url = url;
        this.postParams = postParams;
    }

    public NtesApiRequest() {

    }
}
