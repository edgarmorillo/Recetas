import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebakotlinedgar.R

class RestaurantsActivity : AppCompatActivity(), RestaurantsView {
    private lateinit var presenter: RestaurantsPresenter
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button

    private lateinit var adapter: RestaurantsAdapter
    private lateinit var restaurantsRecyclerView: RecyclerView
    private val restaurants = mutableListOf<Restaurant>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants)
        // Inicialización del adaptador
        adapter = RestaurantsAdapter(listOf())
        val repository = RestaurantRepository()
        presenter = RestaurantsPresenter(this, repository)

        adapter = RestaurantsAdapter(restaurants)
        restaurantsRecyclerView.adapter = adapter

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            presenter.searchRestaurants(query)
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.loadRestaurants()
    }

    override fun showRestaurants(restaurants: List<Restaurant>) {
        this.restaurants.clear()
        this.restaurants.addAll(restaurants)
        adapter.notifyDataSetChanged()
    }
}