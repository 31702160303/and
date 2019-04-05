package com.example.kk.wedontchat;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class chatFragment extends Fragment {
    View chatFragmentview;
    private Button button;
    private EditText editText;
    private BufferedReader bufferedReader;
    private StringBuilder stringBuilder;
    private HttpURLConnection connection;
    private InputStream inputStream;
    TextView textView;
    private String getchat;
    private String user;
    private String password;

    Handler Phandler=new Handler(new Handler.Callback() {


        @Override
        public boolean handleMessage(Message msg) {
            try{
               String temp= msg.getData().getString("data");
               parse(temp);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return false;
        }
    });


    public chatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chatFragmentview = inflater.inflate(R.layout.fragment_chat, container, false);
        initui();
        user = getArguments().getString("user");
        password = getArguments().getString("password");


        return chatFragmentview;
    }

    void initui() {
        button = chatFragmentview.findViewById(R.id.send_message_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                getchat = editText.getText().toString();
                chatProgress(getchat);

            }
        });
        editText = chatFragmentview.findViewById(R.id.input_group_message);
        textView = chatFragmentview.findViewById(R.id.group_chat_text_display);
    }

    private void chatProgress(String chat) {
        if ((!TextUtils.isEmpty(chat))){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String chat = getchatinit(user,getchat,password);
                    Log.i("getSstringed",chat);
                    //packing up Message
                    Message message = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("data", chat);
                    message.setData(bundle);
                    Phandler.sendMessage(message);
                }
            }).start();
        }
    }

    public String getchatinit(String user, String chat, String password) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://123.207.85.214/chat/chat1.php");
            Log.i("user",user);
            String para = new String("user=" + user + "&chat=" + chat + "&password=" + password);
            //1.得到HttpURLConnection实例化对象
            conn = (HttpURLConnection) url.openConnection();
            //2.设置请求方式
            conn.setRequestMethod("POST");
            //3.设置post提交内容的类型和长度
            /*
             * 只有设置contentType为application/x-www-form-urlencoded，
             * servlet就可以直接使用request.getParameter("username");直接得到所需要信息
             */
            conn.setRequestProperty("contentType", "application/x-www-form-urlencoded");
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
                for (String line = ""; (line = bufferedReader.readLine()) != null; ) {
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


    void parse(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String getchat1=jsonObject.getString("chat");
                String getdate1=jsonObject.getString("time");
                String getname1=jsonObject.getString("name");
                building(getchat1,getdate1,getname1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    void building(String getchat,String getdate,String getname){
        textView.append(getname+"\n"+getchat+"\n"+getdate+"\n");
        textView.append("\n");
    }
}
