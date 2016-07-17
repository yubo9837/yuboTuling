package cn.iyuboi.yubotuling;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yubo on 2016/7/17.
 */
public class TextAdapter extends BaseAdapter {

    private List<ListData> lists;
    private Context mContext;

    private RelativeLayout layout;
    public TextAdapter(List<ListData> lists,Context mContext){
        this.lists=lists;
        this.mContext=mContext;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(mContext);

        if (lists.get(i).getFlag() == ListData.RECEIVE) {

            layout=(RelativeLayout) inflater.inflate(R.layout.leftitem,null);
        }
        if(lists.get(i).getFlag()==ListData.SEND){
            layout=(RelativeLayout)inflater.inflate(R.layout.rightitem,null);
        }

        TextView tv=(TextView)layout.findViewById(R.id.tv);
        tv.setText(lists.get(i).getContent());
        TextView time=(TextView)layout.findViewById(R.id.time);
        time.setText(lists.get(i).getTime());
        return layout;
    }
}
