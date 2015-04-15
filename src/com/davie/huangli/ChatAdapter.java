package com.davie.huangli;

import android.content.Context;
import android.nfc.Tag;
import android.os.ConditionVariable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.davie.message.ChetMessage;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.Inflater;

/**
 * User: davie
 * Date: 15-4-15
 */
public class ChatAdapter extends BaseAdapter {

    private List<ChetMessage> messages;
    private Context context;
    private final LayoutInflater inflater;

    public ChatAdapter(Context context, List<ChetMessage> messages) {
        this.context = context;
        this.messages = messages;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int ret = 0;
        ret = messages.size();
        return ret;
    }

    @Override
    public Object getItem(int i) {
        Object ret = null;
        if (messages != null) {
            ret = messages.get(i);
        }
        return ret;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 返回当前ListView 显示的布局有几种类型
     * @return
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     * 该方法,返回0->类型个数-1
     * 必须是该种方式.
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        ChetMessage message = messages.get(position);
        int ret = 0;
        String to = message.getTo();
        if(to.equals("GaGa")){
            ret = 0;//代表左侧
        }else{
            ret = 1;//代表右侧
        }
        //
        Log.i("ChatAdapter",position+",type -> "+ret);
        return ret;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;

        //TODO 需要展现界面, 实现两个布局共存的ListView

        Log.i("ChatAdapter","getView "+position+",cv : "+convertView);

        ChetMessage message = messages.get(position);
        String to = message.getTo();
        if(to.equals("GaGa")){
            //左侧
            if(convertView !=null){
                //如果复用的布局有,那么直接使用
                ret = convertView;

//                if(convertView instanceof LinearLayout){
//                    Log.i("ChatAdapter","Left convertView is LinearLayout");
//                }else {
//                    throw new RuntimeException("Type Error");
//                }
            }else {
                //没有复用,创建
                ret = inflater.inflate(R.layout.item_chat_left, parent,false);

            }

            //TODO 处理左侧
            LeftHolder holder = (LeftHolder)ret.getTag();
            if(holder==null){
                holder = new LeftHolder();
                holder.txtLeft  = (TextView) ret.findViewById(R.id.txt_chat_left);
                ret.setTag(holder);
            }

            //TODO 显示内容

        }else {
            //右侧
            if(convertView !=null){
//                如果复用的布局有,则直接复用
                ret = convertView;

//                if(convertView instanceof RelativeLayout){
//                    Log.i("ChatAdapter","Right convertView is RelativeLayout");
//                }else {
//                    throw new RuntimeException("Type Error");
//                }
            }else {
                //没有复用,创建
                ret = inflater.inflate(R.layout.item_chat_right, parent,false);
            }

            //TODO处理右侧
            //右侧就用右侧的 RightHolder
            RightHolder holder = (RightHolder)ret.getTag();
            if (holder == null) {
                holder = new RightHolder();
                holder.txtRight = (TextView) ret.findViewById(R.id.txt_chat_right);
                ret.setTag(holder);
            }

            //TODO 显示内容


        }
        return ret;
    }


    private static class LeftHolder{
        public TextView txtLeft;
    }

    public static class RightHolder{
        public TextView txtRight;
    }
}
