package com.example.musicplayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.musicplayer.bean.Mp3Info;
import com.example.musicplayer.utility.MediaUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BpmLibrary extends AppCompatActivity
{



    ProgressBar progressBar;
    private List<Mp3Info> mBpmList = new ArrayList<>();
    private ListView BpmListView;
    private int mPosition;
    public String tempSongName;
    public String tempSongArtist;
    static final String API_KEY = "e325699e517ecfe9167e0b3397e0a646";
    static final String API_URL = "https://api.getsongbpm.com/search/?";
    String bpm;
    String mbpm;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bpm_library);
        BpmListView=(ListView)findViewById(R.id.BpmLibrary);
        getSongInfo();


    }

    public void getSongInfo()
    {
        mBpmList = MediaUtil.getMp3Infos(this);
        BpmListView.setAdapter(new MediaListAdapter());
    }




    private class MediaListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mBpmList.size();
        }

        @Override
        public Object getItem(int position) {
            return mBpmList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           BpmLibrary.ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(BpmLibrary.this, R.layout.bpm_list, null);
                holder.mImgAlbum = (ImageView) convertView.findViewById(R.id.img_albumB);
                holder.mTvTitle = (TextView) convertView.findViewById(R.id.SongTitleBpm);
                holder.mTvArtist = (TextView) convertView.findViewById(R.id.SongArtistBpm);
                holder.mBpm=(TextView)convertView.findViewById(R.id.SongBpm);


                convertView.setTag(holder);
            } else {
                holder = (BpmLibrary.ViewHolder) convertView.getTag();
            }
            holder.mImgAlbum.setImageBitmap(MediaUtil.getArtwork(BpmLibrary.this, mBpmList.get(position).getId(), mBpmList.get(position).getAlbumId(), true, true));
            holder.mTvTitle.setText(mBpmList.get(position).getTitle());
            holder.mTvArtist.setText(mBpmList.get(position).getArtist());

            tempSongName=mBpmList.get(position).getArtist();
            tempSongArtist=mBpmList.get(position).getArtist();

            new RetrieveFeedTask().execute();

            holder.mBpm.setText(mbpm);



            if (mPosition == position) {
                holder.mTvTitle.setTextColor(getResources().getColor(R.color.colorAccent));
            } else {
                holder.mTvTitle.setTextColor(getResources().getColor(R.color.colorNormal));
            }
            holder.mTvTitle.setTag(position);

            return convertView;
        }
    }

   private static class ViewHolder {
        ImageView mImgAlbum;
        TextView mTvTitle;
        TextView mTvArtist;
       TextView mBpm;

    }


    class RetrieveFeedTask extends AsyncTask<Void, Void, String>
    {


        protected String doInBackground(Void... urls)
        {


            try {
                URL url = new URL(API_URL + "api_key=" + API_KEY + "&type=both&lookup=song:" + tempSongName + "%20artist:" + tempSongArtist);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    Log.d("json object", stringBuilder.toString());
//
                    JSONObject jObject = new JSONObject(stringBuilder.toString());
                    JSONArray jArray = jObject.getJSONArray("search");

                    for (int i=0; i < jArray.length(); i++)
                    {
                        try {
                            JSONObject oneObject = jArray.getJSONObject(i);
                            // Pulling items from the array
                            bpm = oneObject.getString("tempo");
                            // String oneObjectsItem2 = oneObject.getString("anotherSTRINGNAMEINtheARRAY");
                            Log.d("BPM", bpm);
                        } catch (JSONException e) {
                        }
                    }

//                    return stringBuilder.toString();
                    return ("BPM: " + bpm).toString();
//                    return bpm;
                }
                finally
                {
                    urlConnection.disconnect();
                }
            }
            catch(Exception e)
            {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }
        protected void onPostExecute(String response) {
            if(response == null)
            {
                response = "THERE WAS AN ERROR";
            }
                mbpm = response;
        }

    }
}
