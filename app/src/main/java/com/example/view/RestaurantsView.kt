
interface RestaurantsView {

    fun showLoading()
    fun hideLoading()
    fun showRestaurants(restaurants: List<Restaurant>)
    fun showError(error: String)

}
