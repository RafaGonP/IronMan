package rafagonp.com.marvelcomics.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import rafagonp.com.marvelcomics.R;
import rafagonp.com.marvelcomics.helpers.ImageLoader;
import rafagonp.com.marvelcomics.model.Comic;

/**
 * Created by Rafa on 20/07/2016.
 */
public class DetailFragment extends Fragment {

    String comicSelected;
    Gson gson;
    Comic comic;
    TextView title, description;
    ImageView image;
    ImageLoader imageLoader;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);

        if(getArguments() != null) {
            comicSelected = getArguments().getString("comicSelected");
            gson = new Gson();
            Type type = new TypeToken<Comic>() {}.getType();
            comic = gson.fromJson(comicSelected, type);
        }

        toolbar = (Toolbar)v.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        imageLoader = new ImageLoader(getContext());

        title = (TextView)v.findViewById(R.id.title_comic);
        description = (TextView)v.findViewById(R.id.description);
        image = (ImageView)v.findViewById(R.id.image);

        title.setText(comic.getTitle());

        description.setText(stripHtml(comic.getDescription()));

        imageLoader.DisplayImage(comic.getImagePath() + "." + comic.getImageExt(), image, R.mipmap.iron_man_thumb);

        initActionbar();

        return v;
    }

    public String stripHtml(String html) {
        return Html.fromHtml(html).toString();
    }

    private void initActionbar() {
        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;
        }
        return false;
    }

    public void goBack(){
        getFragmentManager().popBackStackImmediate();
    }


}
