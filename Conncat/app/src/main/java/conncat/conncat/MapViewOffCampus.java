package conncat.conncat;


import android.database.SQLException;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class MapViewOffCampus extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private MapView mapView;

    private List<EventData> events;

    View view;

    private EventAdapter eventAdapter;

    public void MapsActivity(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_maps);
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar doesn't work

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //        .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment, sets the listView declared in the layout file, calls the bindlistView()
        view = inflater.inflate(R.layout.activity_maps, container, false);

        EventDBHelper db = new EventDBHelper(getContext());
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            db.openDataBase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        events = db.getOffCampusEvents();
        db.close();

        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);

        return view;

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //Geocoder geocoder = new Geocoder(getContext());
        //String ucmerced = " merced, ca";

        for(int i = 0; i < events.size(); i++){
            mMap.addMarker(new MarkerOptions().position(events.get(i).getLatLng()).title(events.get(i).getName()));
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(events.get(0).getLatLng(), 13.5f));

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
