package cn.iyuboi.yubotuling;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONObject;

import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity implements HttpGetDataListener,OnClickListener{

    private HttpData httpData;
    private List<ListData> lists;
    private ListView lv;
    private EditText sendText;
    private Button send_btn;
    private String content_str;
    private TextAdapter adapter;
    private String []welcome_array;
    private double currentTime,oldTime=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeView.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_blue_bright, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
     //   swipeView.setOnRefreshListener(this);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*
                new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                   initView();
                }
            }, 3000);
                */
            initView();
            }
        });
    }

    private void initView(){
        lv=(ListView)findViewById(R.id.lv);
        sendText=(EditText)findViewById(R.id.sendText);
        send_btn=(Button)findViewById(R.id.send_btn);
        lists = new ArrayList<ListData>();
        send_btn.setOnClickListener(this);
        adapter =new TextAdapter(lists,this);
        lv.setAdapter(adapter);
        ListData listData;
        listData=new ListData(getRandWelcomeTips(),ListData.RECEIVE,getTime());
        lists.add(listData);
    }

    @Override
    public void getDataUrl(String data) {
      //  System.out.println(data);
        parseText(data);
    }


    private String getRandWelcomeTips(){
        String welcome_tip=null;
        welcome_array=this.getResources().getStringArray(R.array.welcome_tips);
        int index=(int)(Math.random()*(welcome_array.length)-1);
        welcome_tip=welcome_array[index];
        return welcome_tip;
    }
    public void parseText(String str){

        try {
            JSONObject jb=new JSONObject(str);
      //      System.out.println(jb.getString("code"));
            //     System.out.println(jb.getString("text"));
            ListData listData;
            listData=new ListData(jb.getString("text"),ListData.RECEIVE,getTime());
            lists.add(listData);
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v){
        getTime();
        content_str=sendText.getText().toString();
        sendText.setText("");
        String dropk = content_str.replace(" ", "");
        String droph = dropk.replace("\n", "");
        ListData listData;
        listData=new ListData(content_str,ListData.SEND,getTime());
        lists.add(listData);

        if(lists.size()>30){
            for(int i=0;i<lists.size();i++){
                lists.remove(i);
            }
        }
        adapter.notifyDataSetChanged();
        httpData=(HttpData)new HttpData(
                "http://www.tuling123.com/openapi/api?key=f2f6478fee7cd633a538ed07e849dc05&info="
                +droph,this).execute();
    }
    public String getTime(){
        currentTime=System.currentTimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
        Date curDate=new Date();
        String str=format.format(curDate);
        if(currentTime-oldTime>=10*1000){
            oldTime=currentTime;
            return str;
        }else{
            return "";
        }
    }
}


