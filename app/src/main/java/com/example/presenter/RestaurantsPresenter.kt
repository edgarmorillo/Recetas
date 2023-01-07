class RestaurantsPresenter(
    private val view: RestaurantsView,
    private val repository: RestaurantRepository
) {
    fun loadRestaurants() {
        repository.getRestaurants { restaurants ->
            view.showRestaurants(restaurants)
        }
    }

    fun searchRestaurants(query: String) {
        repository.searchRestaurants(query) { restaurants ->
            view.showRestaurants(restaurants)
        }
    }
}