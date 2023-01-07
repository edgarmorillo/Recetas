class RestaurantDetailsPresenter(
    private val view: RestaurantDetailsView,
    private val repository: RestaurantRepository
) {
    fun loadRestaurant(restaurantId: String) {
        repository.getRestaurant(restaurantId) { restaurant ->
            view.showRestaurant(restaurant)
        }
    }
}