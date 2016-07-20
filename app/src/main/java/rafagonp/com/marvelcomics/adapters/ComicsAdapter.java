package rafagonp.com.marvelcomics.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import rafagonp.com.marvelcomics.R;
import rafagonp.com.marvelcomics.helpers.ImageLoader;
import rafagonp.com.marvelcomics.model.Comic;
import rafagonp.com.marvelcomics.model.ComicsList;

/**
 * Created by Rafa on 17/07/2016.
 */
public class ComicsAdapter extends BaseAdapter {

    List<Comic> list;
    Context context;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader;

    public ComicsAdapter(Context context, List<Comic> list){
        this.list = list;
        this.context = context;
        this.imageLoader = new ImageLoader(context);
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

    static class ViewHolder{
        ImageView thumb;
        TextView title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comic comic = list.get(position);
        View view = convertView;
        ViewHolder viewHolder;
        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view != null){
            viewHolder = (ViewHolder) view.getTag();
        }else{

            view = layoutInflater.inflate(R.layout.comic_list_cell, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.thumb = (ImageView)view.findViewById(R.id.thumb);
            viewHolder.title = (TextView)view.findViewById(R.id.title);

            view.setTag(viewHolder);
        }
        if(position % 2 == 1){
            view.setBackground(ContextCompat.getDrawable(context, R.drawable.red_card_black_rounded_border));
        }else{
            view.setBackground(ContextCompat.getDrawable(context, R.drawable.yellow_card_black_rounded_border));
        }

        imageLoader.DisplayImage(comic.getThumbnailPath() + "." + comic.getThumbnailExt(), viewHolder.thumb, R.mipmap.iron_man_thumb);

        viewHolder.title.setText(comic.getTitle());

        return view;
    }

    public List<Comic> getComics() {
        return list;
    }

    public void setComics(List<Comic> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
