package com.samsunguet.sev_user.mycloud.object;

import com.samsunguet.sev_user.mycloud.log.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sev_user on 3/5/2016.
 */
public class Token {
    static final String ISSUED      = "issued_at";
    static final String EXPIRES     = "expires";
    static final String ID          = "id";


    String id;
    String expires;
    String issued;

    public Token(String tokenId, String expires, String issued){
        this.id = tokenId;
        this.expires = expires;
        this.issued = issued;
    }

    public Token(String jsonstr){
        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            id          = jsonObject.getString(ID);
            issued      = jsonObject.getString(ISSUED);
            expires     = jsonObject.getString(EXPIRES);

        } catch (JSONException e) {
            //e.printStackTrace();
            MyLog.log(e.toString());
        }
    }

    public String getId(){return id!=null ? id:"";}
    public String getExpires(){return expires!=null ? expires:"";}
    public String getIssued(){return issued!=null ? issued:"";}
}
