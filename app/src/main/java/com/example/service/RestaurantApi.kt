import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestaurantApi {
    @GET("restaurants")
    fun getRestaurants(): Call<List<Restaurant>>

    @GET("restaurants/{restaurantId}")
    fun getRestaurant(@Path("restaurantId") restaurantId: String): Call<Restaurant>

    @GET("restaurants/search")
    fun searchRestaurants(@Query("q") query: String): Call<List<Restaurant>>
}
