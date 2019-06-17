package com.zlb.http.param;

import com.zlb.jniInterface.JniInvokeInterface;

/**
 * 登录需要提交的参数，并不是所有的http 请求都需要一个XXXParams,@Body 提交json str 才需要
 * Created by zenglb on 2016/7/4.
 */
public class LoginParams {
    // FBI WARMING !
    private String grant_type="password";
    private String client_id=JniInvokeInterface.getJniHashMap().get("clientId");
    private String client_secret= JniInvokeInterface.getJniHashMap().get("clientSecret");
    private String username;
    private String refresh_token;
    private String password;
    private String scope;

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
