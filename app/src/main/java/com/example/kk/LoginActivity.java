package com.example.kk.wedontchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private TextView show;
    private BufferedReader bufferedReader;
    private StringBuilder stringBuilder;
    private HttpURLConnection connection;
    private InputStream inputStream;


    Handler Khandler=new Handler(new Handler.Callback() {
        @Override
            public boolean handleMessage(Message msg) {

String words=msg.getData().getString("data");
 show.setText(words);
            return false;
        }
    });

    Handler Phandler=new Handler(new Handler.Callback() {

        private String tempuser;

        @Override
        public boolean handleMessage(Message msg) {
try{
    String loginstate=msg.getData().getString("data");
    JSONObject jsonObject=new JSONObject(loginstate);
    status = jsonObject.getString("status");
    tempname = jsonObject.getString("name");
    tempuser = jsonObject.getString("user");

    if(status.equals("登陆成功")){
        SharedPreferences sp=getSharedPreferences("config",0);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("user",tempuser);
        editor.putString("name",tempname);
        editor.putString("password",password);
        editor.commit();

        sendToMainAcivity();
    }
    else{
        Toast.makeText(LoginActivity.this,status,Toast.LENGTH_LONG).show();
        username.setText("");
        userpassword.setText("");

    }
}
catch(Exception e){
    e.printStackTrace();
            }
            return false;
        }
    });
    private Button loginButton;
    private EditText username;
    private EditText userpassword;
    private TextView register;
    private String user;
    private String password;
    private String status;
    private String tempname;
    private String getpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences=getSharedPreferences("config",0);
        String st=sharedPreferences.getString("user","");
        if(!st.isEmpty()){
            sendToMainAcivity();
        }
        else{
        setContentView(R.layout.activity_login);
            initui();
            setup();
    }}

    private void setup() {
   initdata();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgress();
            }
        });


   register.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           sendToRegisterActivity();
       }
   });




    }

    public String logininit(String user,String password){
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://123.207.85.214/chat/login.php");
            String para = new String("user="+user+"&password="+password);
            getpassword = password;
            //1.得到HttpURLConnection实例化对象
            conn = (HttpURLConnection) url.openConnection();
            //2.设置请求方式
            conn.setRequestMethod("POST");
            //3.设置post提交内容的类型和长度
            /*
             * 只有设置contentType为application/x-www-form-urlencoded，
             * servlet就可以直接使用request.getParameter("username");直接得到所需要信息
             */
            conn.setRequestProperty("contentType","application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(para.getBytes().length));
            //默认为false
            conn.setDoOutput(true);
            //4.向服务器写入数据
            conn.getOutputStream().write(para.getBytes());
            //5.得到服务器相应
            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();//get Streams
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));//read Streams
                stringBuilder = new StringBuilder();
                for (String line ="";(line= bufferedReader.readLine())!=null;){
                    stringBuilder.append(line); //write own streams
                }
                return stringBuilder.toString();

            } else {
                System.out.println("请求失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //6.释放资源
            if (conn != null) {
                //关闭连接 即设置 http.keepAlive = false;
                conn.disconnect();
            }
        }
        return stringBuilder.toString();
    }

    private void loginProgress() {
        user = username.getText().toString();
        password = userpassword.getText().toString();
        if ((TextUtils.isEmpty(user) && TextUtils.isEmpty(user))) {
            Toast.makeText(LoginActivity.this, "please", Toast.LENGTH_LONG).show();
        } else {


            new Thread(new Runnable() {
                @Override
                public void run() {
                    String logining = logininit(user, password);
                    Log.i("tag", logining);
                    //packing up Message
                    Message message = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("data", logining);
                    message.setData(bundle);
                    Phandler.sendMessage(message);

                }
            }).start();
        }
    }

    private void sendToRegisterActivity(){

        Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);

    }
    private void initdata() {


            new Thread(new Runnable() {
               @Override
                public void run() {
                    String datageted= getdata();
                    //packing up Message
                    Message message=Message.obtain();
                    Bundle bundle=new Bundle();
                    bundle.putString("data",datageted);
                    message.setData(bundle);
                    Khandler.sendMessage(message);
                }
            }).start();
        }

    private String getdata() {
        try{
            URL url= new URL("https://v1.hitokoto.cn?c=f&encode=text");

            connection = (HttpURLConnection) url.openConnection();
            if(connection.getResponseCode()==200){
                inputStream = connection.getInputStream();//get Streams
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));//read Streams
                stringBuilder = new StringBuilder();
                for (String line ="";(line= bufferedReader.readLine())!=null;){
                    stringBuilder.append(line); //write own streams
                }
                return stringBuilder.toString();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                if (bufferedReader!=null){
                    bufferedReader.close();
                }
                if (inputStream!=null){
                    inputStream.close();
                }
                if (connection!=null){
                    connection.disconnect();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        return "";
    }




    void initui(){

        loginButton = findViewById(R.id.login_button);
        username = findViewById(R.id.username);
        userpassword = findViewById(R.id.password);
        show = findViewById(R.id.words);
        register = findViewById(R.id.register);

    }

    private void sendToMainAcivity() {
        Intent loginIntent=new Intent(LoginActivity.this,MainActivity.class);
        loginIntent.putExtra("name",tempname);
        startActivity(loginIntent);
    }


}
