package conncat.conncat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;


public class OneFragment extends Fragment{
    View view;

    private EventAdapter eventAdapter;
    private ListView listView;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_one, container, false);
        listView = (ListView) view.findViewById(R.id.eventList);
        bindlistview();


        return view;

    }

    public void bindlistview(){
        String stringUrlXML = "http://events.ucmerced.edu:7070/feeder/main/eventsFeed.do?f=y&sort=dtstart.utc:asc&fexpr=(categories.href!=%22/public/.bedework/categories/sys/Ongoing%22)%20and%20(entity_type=%22event%22%7Centity_type=%22todo%22)&skinName=list-xml&count=200";

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new Downloadlist(getActivity(), listView).execute(stringUrlXML);
        } else {
            //textView.setText("No network connection available.");
        }


    }

    class Downloadlist extends AsyncTask<String, Void, String>{

        ListView listView;
        Activity mContex;
        public  Downloadlist(Activity contex,ListView lview)
        {
            this.listView=lview;
            this.mContex=contex;
        }

        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                downloadXML dl = new downloadXML();
                return dl.downloadXML(urls[0]);
            } catch (IOException e) {
                String error = "Unable to retrieve web page. URL may be invalid.";
                return error;
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            List<EventData> events = xmlParser.getEvents(new ByteArrayInputStream(result.getBytes()));
            eventAdapter = new EventAdapter(getActivity(), -1, events);

            listView.setAdapter(eventAdapter);

            //textView.setText(result);
            //webView.loadData(result, "text/html", "utf-8");
        }


    }

}