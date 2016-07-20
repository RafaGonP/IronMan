package rafagonp.com.marvelcomics.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rafagonp.com.marvelcomics.R;
import rafagonp.com.marvelcomics.adapters.ComicsAdapter;
import rafagonp.com.marvelcomics.helpers.AsyncResponse;
import rafagonp.com.marvelcomics.model.Comic;
import rafagonp.com.marvelcomics.model.ComicsList;
import rafagonp.com.marvelcomics.tasks.ComicsListTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    ListView comicListView;
    List<Comic> comics;
    boolean loading = false;
    int comicCount = 0;
    ListAdapter adapter;
    Toolbar toolbar;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        toolbar = (Toolbar)v.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        verifyStoragePermissions(getActivity());

        if(comics == null)
            comics = new ArrayList<Comic>();

        comicListView = (ListView)v.findViewById(R.id.comic_list);
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener();
        comicListView.setOnScrollListener(endlessScrollListener);

        HashMap<String, String> params = new HashMap<>();
        params.put("Offset", "" + comicCount);

        initActionbar();

        return v;
    }

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void initActionbar() {
        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(false);
        bar.setDisplayShowHomeEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("SOMETHING HERE?", ""+comics.size());
    }

    class EndlessScrollListener implements ListView.OnScrollListener {

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
            if (!(loading) && (totalItemCount - visibleItemCount) <= (firstVisibleItem)) {
                Log.d("LIST:", "SCROLLING");
                HashMap<String, String> params = new HashMap<>();
                params.put("Offset", ""+comicCount);
                new ComicsListTask(new AsyncResponse() {
                    @Override
                    public void processFinish(Object result) {
                        comicCount += 20;
                        ComicsList list = (ComicsList) result;
                        if(list != null && list.getComics() !=null) {
                            Log.d("RESULT: ", list.getComics().size() + "");
                            if (comics != null) {
                                comics.addAll(list.getComics());
                                Log.d("SIZE", ""+comics.size());
                            } else {
                                comics = list.getComics();
                            }
                            loading = false;
                            if (adapter != null && comicListView.getAdapter() != null) {
                                ((ComicsAdapter) comicListView.getAdapter()).setComics(comics);
                            } else {
                                adapter = new ComicsAdapter(getActivity(), comics);
                                comicListView.setAdapter(adapter);
                            }
                        }

                        comicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.addToBackStack("");
                                ft.setCustomAnimations(R.anim.enter_anim_left, R.anim.exit_anim_right, R.anim.enter_anim_right, R.anim.exit_anim_left);
                                DetailFragment detailFragment = new DetailFragment();
                                Bundle b = new Bundle();
                                Gson gson = new Gson();
                                b.putString("comicSelected", gson.toJson(comics.get(position)));
                                detailFragment.setArguments(b);
                                ft.replace(R.id.container, detailFragment).commit();
                            }
                        });

                    }
                }).execute(params);
                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {}

        public boolean isLoading() {
            return loading;
        }

        public void setLoading(boolean loading) {
            loading = loading;
        }

    }


}
