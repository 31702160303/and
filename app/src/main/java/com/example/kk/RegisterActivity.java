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

public class RegisterActivity extends AppCompatActivity {
      private EditText user;
      private EditText username;
      private  EditText password;
      private Button register;
    private String getuser;
    private String getpassword;
    private String getusername;

    private BufferedReader bufferedReader;
    private StringBuilder stringBuilder;
    private HttpURLConnection connection;
    private InputStream inputStream;
    TextView already;
    Handler Phandler=new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            try{
                String registerstate=msg.getData().getString("data");
                JSONObject jsonObject=new JSONObject(registerstate);
                status = jsonObject.getString("status");

                if(status.equals("注册成功")){

                    sendToMainAcivity();

                        SharedPreferences sp=getSharedPreferences("config",0);
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putString("user",getuser);
                        editor.putString("password",getpassword);
                        editor.commit();
                }

                else{
                    Toast.makeText(RegisterActivity.this,status,Toast.LENGTH_LONG).show();
                    user.setText("");
                    password.setText("");
                    username.setText("");

                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return false;
        }
    });
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initui();
        setup();
    }

    private void setup() {

        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendTologinActivity();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getuser = user.getText().toString();
                getpassword = password.getText().toString();
                getusername = username.getText().toString();

                if ((TextUtils.isEmpty(getuser) && (TextUtils.isEmpty(getusername)))&&(TextUtils.isEmpty(getpassword)))  {
                    Toast.makeText(RegisterActivity.this, "please", Toast.LENGTH_LONG).show();
                } else {




                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String registering = registinit(getuser, getpassword,getusername);
                            //packing up Message
                            Message message = Message.obtain();
                            Bundle bundle = new Bundle();
                            bundle.putString("data", registering);
                            message.setData(bundle);
                            Phandler.sendMessage(message);
                        }
                    }).start();

                }
            }
        });
    }

    private void sendTologinActivity() {
        Intent loginIntent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }

    private void initui() {
        user=findViewById(R.id.user);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        register=findViewById(R.id.register_button);

       already=findViewById(R.id.login);
    }

    public String registinit(String user,String password,String username){
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://123.207.85.214/chat/register.php");
            String para = new String("user="+user+"&password="+password+"&name="+username);
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
    private void sendToMainAcivity() {
        Intent registerIntent=new Intent(RegisterActivity.this,MainActivity.class);
        registerIntent.putExtra("name",getusername);
        startActivity(registerIntent);
    }

}
