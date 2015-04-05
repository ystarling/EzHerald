package com.herald.ezherald.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;

/**
 * Created by xie on 12/12/2014.
 */
public class APIAccount {
    public String uuid;
    public String password,number;
    private Context context;
    private static String UUID_PREF_NAME = "UUID_PREFERENCE";

    public APIAccount(Context context){
        this.context = context;
        uuid = readUUID();
    }

    //TODO get UUID on main activity start is uuid is empty

    private String readUUID(){
        SharedPreferences preferences = context.getSharedPreferences(UUID_PREF_NAME,0);
        String uuid = preferences.getString("UUID","");
        return decrypt(uuid);
    }
    public void saveUUID(String uuid){
        SharedPreferences preferences = context.getSharedPreferences(UUID_PREF_NAME,0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UUID",encrypt(uuid));
        editor.commit();
    }
    public boolean isUUIDValid(){
        uuid = readUUID();
        return uuid!=null && !uuid.isEmpty();
    }
    private String encrypt(String data){
        return Crypto.encrypt(data);
    }
    private String decrypt(String data){
        return Crypto.decrypt(data);
    }
    /*
    *
    * This function is just for those who just update the application
     */
    public void autoLogin(){

        APIClient client = APIFactory.getAPIClient(context,"uc/auth",new SuccessHandler() {
            @Override
            public void onSuccess(String data) {
                saveUUID(data);
            }
        },
        new FailHandler() {
            @Override
            public void onFail(Status err,String message) {
                //just pass
            }
        });
        UserAccount user = Authenticate.getIDcardUser(context);

        if(user!=null){
            client.addArg("user",user.getUsername());
            client.addArg("password",user.getPassword());
        }

        client.addAPPIDToArg();

        client.requestWithoutCache();
    }

}
