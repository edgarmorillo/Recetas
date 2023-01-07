import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebakotlinedgar.R

class RestaurantDetailsAdapter(private val details: List<Pair<Int, String>>) :
    RecyclerView.Adapter<RestaurantDetailsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Enlazamos los elementos de la vista con variables
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Mostramos los detalles del restaurante en la vista
    }

    override fun getItemCount() = details.size
}