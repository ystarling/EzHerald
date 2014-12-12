package com.herald.ezherald.api;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xie on 12/12/2014.
 */
public class APIAccount {
    public String uuid;
    public String ccardnum,password,number,pePassword,libUsername,libPassword,cardQueryPassword;
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
    private void saveUUID(String uuid){
        SharedPreferences preferences = context.getSharedPreferences(UUID_PREF_NAME,0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UUID",encrypt(uuid));
    }

    private String encrypt(String data){
        return  data;
    }
    private String decrypt(String data){
        return data;
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
            public void onFail(int errCode,String message) {
                //just pass
            }
        });

        client.addArg("appid",new APPID().getAPPID());
        client.addArg("user",ccardnum);
        client.addArg("password",password);
        client.doRequest();
    }

}
