import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantRepository {
    private val api = Retrofit.Builder()
        .baseUrl("http://demo2066522.mockable.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RestaurantApi::class.java)

    fun getRestaurants(callback: (List<Restaurant>) -> Unit) {
        api.getRestaurants().enqueue(object : Callback<List<Restaurant>> {
            override fun onResponse(call: Call<List<Restaurant>>, response: Response<List<Restaurant>>) {
                if (response.isSuccessful) {
                    callback(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                // Gestionamos el error
            }
        })
    }

    fun getRestaurant(restaurantId: String, callback: (Restaurant) -> Unit) {
        api.getRestaurant(restaurantId).enqueue(object : Callback<Restaurant> {
            override fun onResponse(call: Call<Restaurant>, response: Response<Restaurant>) {
                if (response.isSuccessful) {
                    callback(response.body()!!)
                }
            }

            override fun onFailure(call: Call<Restaurant>, t: Throwable) {
                // Gestionamos el error
            }
        })
    }

    fun searchRestaurants(query: String, callback: (List<Restaurant>) -> Unit) {
        api.searchRestaurants(query).enqueue(object : Callback<List<Restaurant>> {
            override fun onResponse(call: Call<List<Restaurant>>, response: Response<List<Restaurant>>) {
                if (response.isSuccessful) {
                    callback(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                // Gestionamos el error
            }
        })
    }
}
