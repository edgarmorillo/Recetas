import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pruebakotlinedgar.R

class RestaurantDetailsActivity : AppCompatActivity(), RestaurantDetailsView {
    private lateinit var presenter: RestaurantDetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)

        val repository = RestaurantRepository()
        presenter = RestaurantDetailsPresenter(this, repository)
    }

    override fun showRestaurant(restaurant: Restaurant) {
        // Mostramos los detalles del restaurante en la vista
    }
}