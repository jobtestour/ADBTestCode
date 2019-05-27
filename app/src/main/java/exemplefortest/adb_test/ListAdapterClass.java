package exemplefortest.adb_test;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;



public class ListAdapterClass extends BaseAdapter {


    Context context;
    ArrayList<String> UserInfoArrayList;


    public ListAdapterClass(Context context, ArrayList<String> UserInfoArrayList) {

        this.context = context;
        this.UserInfoArrayList = UserInfoArrayList;
    }

    @Override
    public int getCount()
    {
        return this.UserInfoArrayList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return this.UserInfoArrayList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewItem viewItem = null;

        if(convertView == null)
        {
            viewItem = new ViewItem();

            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInfiater.inflate(R.layout.layout_items, null);

            viewItem.TextViewSubjectName = (TextView)convertView.findViewById(R.id.textView1);

            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItem) convertView.getTag();
        }

       viewItem.TextViewSubjectName.setText(UserInfoArrayList.get(position));

        return convertView;
    }
}

class ViewItem
{
    TextView TextViewSubjectName;

}

