package com.herald.ezherald.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.herald.ezherald.R;

public class APIAccountActivity extends Activity {

    private Context context;
    private boolean needUpdateInfo;
    private String number ;//学号
    private String card ;//一卡通
    private String pass ;//统一认证密码
    private String pePass;//体育系密码
    private String libuser;
    private String libPass;
    private ProgressDialog proDialog;
    private String readTextFromView(int id){
        View view = findViewById(id);
        if(view == null)
            return "";
        return ((EditText)view).getText().toString().trim();
    }
    private void showMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apiaccount);
        this.context = this;


        View submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = readTextFromView(R.id.number);//学号
                card = readTextFromView(R.id.card);//一卡通
                pass = readTextFromView(R.id.pass);//统一认证密码
                pePass = readTextFromView(R.id.pe_pass);//体育系密码
                libuser = readTextFromView(R.id.lib_user);
                libPass = readTextFromView(R.id.lib_pass);

                if(card.isEmpty()){
                    showMessage("一卡通不能为空");
                }else if(pass.isEmpty()){
                    showMessage("统一认证密码不能为空");
                }else {
                    proDialog = ProgressDialog.show(context, "请稍候","正在进行", true, true);
                    final APIAccount apiAccount = new APIAccount(context);
                    if(!apiAccount.isUUIDValid()){//还没登陆先登录
                        Login();
                    }
                    if( !pePass.isEmpty() || !libuser.isEmpty() || !libPass.isEmpty()){
                        needUpdateInfo = true;
                    }

                }
            }
        });

    }

    private void Login() {
        final APIAccount apiAccount = new APIAccount(context);
        APIClient client = APIFactory.getAPIClient(context,"uc/auth",new SuccessHandler(){

            @Override
            public void onSuccess(String data) {
                apiAccount.saveUUID(data);
                if(needUpdateInfo){
                    updateInfo();
                }else{
                    proDialog.dismiss();
                    showMessage("验证成功");
                }
            }
        },new FailHandler(){

            @Override
            public void onFail(Status status, String message) {
                proDialog.dismiss();
                if(status.equals(Status.TIMEOUT)){
                    showMessage("连接超时，请重试");
                }else if(status.equals(Status.IO_EXCEPTION)){
                    showMessage("网络错误，请重试");
                }else{
                    showMessage("验证失败，请检查一卡通和密码");
                }
            }
        });

        client.addArg("user",card);
        client.addArg("password",pass);
        client.addAPPIDToArg();
        client.doRequest();
    }

    private void updateInfo() {
        final APIAccount apiAccount = new APIAccount(context);
        APIClient client = APIFactory.getAPIClient(context,"uc/update",new SuccessHandler(){

            @Override
            public void onSuccess(String data) {
                proDialog.dismiss();
                showMessage("更新信息成功");
            }
        },new FailHandler(){

            @Override
            public void onFail(Status status, String message) {
                proDialog.dismiss();
                if(status.equals(Status.TIMEOUT)){
                    showMessage("连接超时，请重试");
                }else if(status.equals(Status.IO_EXCEPTION)){
                    showMessage("网络错误，请重试");
                }else{
                    showMessage("验证失败，请检查一卡通和密码");
                }
            }
        });
        client.addArg("user",card);
        client.addArg("password",pass);
        client.addArg("number",number);
        if(pePass!="")
            client.addArg("pe_password",pePass);
        if(libuser!="")
            client.addArg("lib_username",libuser);
        if(libPass!="")
            client.addArg("lib_password",libPass);

        client.doRequest();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_apiaccount, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
