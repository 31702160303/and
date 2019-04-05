package com.example.kk.wedontchat;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    private BufferedReader bufferedReader;
    private StringBuilder stringBuilder;
    private InputStream inputStream;
    private View groupFragmentView;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups=new ArrayList<>();

    Handler Phandler=new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            try {
                Set<String> set=new HashSet<>();
                String loginstate = msg.getData().getString("data");
                JSONArray jsonArray=new JSONArray(loginstate);
                int length=jsonArray.length();
                for(int i=0;i<length;i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    name = jsonObject.getString("name");
                    user = jsonObject.getString("user");
                    set.add(name);
                    set.add(user);
                }

                list_of_groups.clear();
                list_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return false;
        }
    });
    private String name;
    private String user;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        groupFragmentView = inflater.inflate(R.layout.fragment_contacts, container, false);
initui();
getcontacts();

    return groupFragmentView;
    }

    private void getcontacts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String contactinit=contactinit();
                Log.i("tag", contactinit);
                //packing up Message
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("data", contactinit);
                message.setData(bundle);
                Phandler.sendMessage(message);

            }
        }).start();
    }

    private void initui(){
        list_view=groupFragmentView.findViewById(R.id.list_view);
        arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list_of_groups);
        list_view.setAdapter(arrayAdapter);

   }

    public String contactinit(){
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://123.207.85.214/chat/member.php");
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
            //默认为false
            conn.setDoOutput(true);
            //4.向服务器写入数据
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


}
