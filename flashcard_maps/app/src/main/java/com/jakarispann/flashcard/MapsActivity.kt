package com.jakarispann.flashcard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Get the SupportMapFragment and request notification when the map is ready
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Called when the map is ready to be used.
     * This is where we add markers and move the camera.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Atlanta, GA — a favorite city location
        val atlanta = LatLng(33.7490, -84.3880)
        mMap.addMarker(
            MarkerOptions()
                .position(atlanta)
                .title("Atlanta, GA")
                .snippet("Home of Georgia Tech & Coca-Cola HQ")
        )

        // Georgia Tech campus — a second landmark marker
        val georgiaTech = LatLng(33.7756, -84.3963)
        mMap.addMarker(
            MarkerOptions()
                .position(georgiaTech)
                .title("Georgia Tech")
                .snippet("A top engineering university in Atlanta")
        )

        // Move and zoom the camera to Atlanta at city-level zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(atlanta, 12f))
    }
}
