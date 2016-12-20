package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xerdp.demo_listview.MainActivity;
import com.example.xerdp.demo_listview.R;

import java.util.List;

import bean.Bean;

/**
 * Created by xerdp on 2016/12/19.
 */

public class Listview_adapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Bean> list;

    public Listview_adapter(List<Bean> list, MainActivity mainActivity) {
        this.list = list;
        this.context = mainActivity;
        layoutInflater = LayoutInflater.from(context);
    }

    public void onDatechange(List<Bean> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item, null);
            viewHold = new ViewHold();
            viewHold.textView = (TextView) convertView.findViewById(R.id.item_id);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        viewHold.textView.setText(list.get(position).getString());
        return convertView;
    }

    public class ViewHold {
        TextView textView;
    }
}
