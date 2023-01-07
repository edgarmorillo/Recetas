import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.viewmodel.CreationExtras.Empty.map
import com.example.pruebakotlinedgar.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val repository = RestaurantRepository()
        repository.getRestaurants { restaurants ->
            // Añadimos los marcadores de los restaurantes al mapa
        }
    }
}