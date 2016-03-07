package com.samsunguet.sev_user.mycloud.object;

/**
 * Created by sev_user on 3/5/2016.
 */
public class User {
    String username;
    String password;
    Token  token;
    Tenant tenant;
    String storageUrl;

    public User(String username, String password, Token token, Tenant tenant){
        this.username = username;
        this.password = password;
        this.token    = token;
        this.tenant   = tenant;
    }

    public User(String username, String password, Tenant tenant){
        this.username = username;
        this.password = password;
        this.tenant   = tenant;
    }

    public String getUsername(){return username!=null ? username:"";}
    public String getPassword(){return password!=null ? password:"";}
    public Token getToken(){return token;}
    public Tenant getTenant() {return tenant;}
    public String getStorageUrl(){return storageUrl!=null ? storageUrl:"";}

    public void setStorageUrl(String url){this.storageUrl = url;}
    public void setToken(Token token){this.token = token;}
    public String toString(){
        return "name: "+ username + "\npass: "+password+"\ntokenid: "+token.getId()+
                "\n tenant: "+tenant.getName()+"\nstorageurl: "+storageUrl;
    }
}
