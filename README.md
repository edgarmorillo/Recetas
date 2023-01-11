# Proyecto Kotlin

Organización de archivos:

La estructura de nuestro proyecto podría ser la siguiente:

- Una carpeta de modelos, que contendrá la clase **`Restaurant`** que represente a cada restaurante y que utilizaremos para parsear la respuesta de la API.
- Una carpeta de presentadores, que contendrá a los presentadores del patrón MVP (Modelo-Vista-Presentador). Tendremos un presentador por cada vista que mencionaste.
- Una carpeta de vistas, que contendrá las actividades o fragmentos de Android que representan a cada vista.
- Una carpeta de adaptadores, que contendrá los adaptadores que usaremos para mostrar la lista de restaurantes en la primera vista y los detalles de cada restaurante en la segunda vista.




```kotlin
└── proyecto
    ├── modelos
    │   └── Restaurant.kt
    ├── presentadores
    │   ├── RestaurantDetailsPresenter.kt
    │   └── RestaurantsPresenter.kt
    ├── vistas
    │   ├── MapsActivity.kt
    │   ├── RestaurantDetailsActivity.kt
    │   └── RestaurantsActivity.kt
    └── adaptadores
        ├── RestaurantDetailsAdapter.kt
        └── RestaurantsAdapter.kt
```



primero necesitaremos añadir algunas dependencias al archivo **`build.gradle`**
 de nuestra aplicación. Para consumir la API de restaurantes, necesitaremos añadir una dependencia de Retrofit y para consumir la API de Google Maps, necesitaremos añadir una dependencia de la biblioteca de Google Maps para Android. Añadiremos esto en el archivo **`build.gradle`**
:

```
dependencies {
    // Retrofit para consumir la API de restaurantes
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    
    // Biblioteca de Google Maps para Android
    implementation 'com.google.android.gms:play-services-maps:17.0.0'
}
```

También necesitaremos añadir una clave de API de Google Maps en el archivo **`AndroidManifest.xml`** esto con el fin de poder hacer uso de dicha API
 de nuestra aplicación:

```kotlin
	
	<manifest>
  <!-- Otras etiquetas del manifiesto -->
  <application>
    <!-- Otras etiquetas de la aplicación -->
    <meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="AIzaSyDfG9dSU8ImhQvrxD2JcUTIgAJV2G4lMHE"/>
  </application>
</manifest>
```

Comenzamos viendo los archivos que vamos a crear

**modelos/Restaurant.kt**

```kotlin
data class Restaurant(
    val name: String,
    val photo: String,
    val rating: Float,
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)
```

**presentadores/RestaurantsPresenter.kt**

“RestaurantsPresenter". Esta clase tiene dos propiedades: "view" y "repository". La propiedad "view" es de tipo "RestaurantsView" y la propiedad "repository" es de tipo "RestaurantRepository".

La clase tiene dos funciones públicas: "loadRestaurants()" y "searchRestaurants()".

La función "loadRestaurants()" se utiliza para obtener los restaurantes desde el "repository" y luego mostrarlos en la "view" utilizando el método "showRestaurants()". El método "getRestaurants()" en el "repository" devuelve una lista de restaurantes a través de una función de devolución de llamada (callback function).

La función "searchRestaurants()" funciona de manera similar a "loadRestaurants()", pero toma un parámetro adicional "query" que se utiliza para buscar restaurantes específicos en el "repository" utilizando el método "searchRestaurants()". Luego, los restaurantes devueltos se muestran en la "view" de la misma manera que en la función "loadRestaurants()".

En resumen, esta clase actúa como un intermediario entre la vista y el almacenamiento de datos, y se encarga de obtener los datos del almacenamiento y mostrarlos en la vista.

```kotlin
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
```

**vistas/RestaurantsActivity.kt**

"RestaurantsActivity" y es una subclase de "AppCompatActivity" También implementa la interfaz "RestaurantsView".

La clase tiene tres propiedades: "presenter", "adapter" y "restaurants". La propiedad "presenter" es de tipo "RestaurantsPresenter" y se utiliza para llamar a los métodos de presentación. La propiedad "adapter" es de tipo "RestaurantsAdapter" y se utiliza para mostrar los restaurantes en un RecyclerView. La propiedad "restaurants" es una lista mutable de "Restaurant" y se utiliza como origen de datos para el adaptador.

La función "onCreate()" es una función de ciclo de vida de la actividad que se ejecuta cuando la actividad se crea. En esta función, primero se establece la vista de la actividad con "setContentView()" y luego se crea una nueva instancia de "RestaurantRepository" y se utiliza para inicializar una nueva instancia de "RestaurantsPresenter" pasándolo como parámetro junto a la vista actual, lo que implica que this es igual a RestaurantsView.

Luego, se crea una nueva instancia de "RestaurantsAdapter" con "restaurants" como origen de datos, y se establece como el adaptador del RecyclerView "restaurantsRecyclerView".

El botón "searchButton" tiene un controlador de evento que se ejecuta cuando se presiona el botón. En el controlador de evento, se obtiene el texto del campo de búsqueda "searchEditText" y se pasa al método "searchRestaurants()" del presentador.

La función "onStart()" es otra función de ciclo de vida de la actividad que se ejecuta cuando la actividad comienza. En esta función, se llama al método "loadRestaurants()" del presentador para cargar los restaurantes desde el almacenamiento.

La función "showRestaurants()" es una función de "RestaurantsView" que se implementa en esta clase. En esta función, se actualiza la lista "restaurants" con los restaurantes proporcionados como parámetro, se limpia la lista actual y se agrega los nuevos elementos, y se notifica al adaptador para que se actualice la vista.

En resumen, esta clase actúa como una actividad en Android y se encarga de mostrar los restaurantes en una vista RecyclerView. Utiliza un objeto RestaurantsPresenter para interactuar con un almacenamiento de datos (RestaurantRepository), y utiliza un RestaurantsAdapter para mostrar los restaurantes en un RecyclerView

```kotlin
class RestaurantsActivity : AppCompatActivity(), RestaurantsView {
    private lateinit var presenter: RestaurantsPresenter

    private lateinit var adapter: RestaurantsAdapter

    private val restaurants = mutableListOf<Restaurant>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants)

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
```

**presentadores/RestaurantDetailsPresenter.kt**:

Este código es una clase en Kotlin llamada "RestaurantDetailsPresenter". Esta clase tiene dos propiedades: "view" y "repository". La propiedad "view" es de tipo "RestaurantDetailsView" y la propiedad "repository" es de tipo "RestaurantRepository".

La clase tiene una función pública "loadRestaurant(restaurantId: String)". La función "loadRestaurant()" se utiliza para obtener un restaurante específico del "repository" utilizando un identificador único de restaurante "restaurantId", y luego mostrarlo en la "view" utilizando el método "showRestaurant()". El método "getRestaurant(restaurantId: String, callback: (restaurant: Restaurant) -> Unit)" en el "repository" devuelve un restaurante a través de una función de devolución de llamada (callback function).

En resumen, esta clase actúa como un intermediario entre la vista y el almacenamiento de datos, y se encarga de obtener un restaurante específico del almacenamiento y mostrarlo en la vista.

```kotlin
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
```

**vistas/RestaurantDetailsActivity.kt**

La clase tiene una propiedad "presenter" de tipo "RestaurantDetailsPresenter" y se utiliza para llamar a los métodos de presentación.

La función "onCreate()" es una función de ciclo de vida de la actividad que se ejecuta cuando la actividad se crea. En esta función, primero se establece la vista de la actividad con "setContentView()" y luego se crea una nueva instancia de "RestaurantRepository" y se utiliza para inicializar una nueva instancia de "RestaurantDetailsPresenter" pasándolo como parámetro junto a la vista actual, lo que implica que this es igual a RestaurantDetailsView

La función "showRestaurant(restaurant: Restaurant)" es una función de "RestaurantDetailsView" que se implementa en esta clase. En esta función, se proporciona el restaurante y se tiene que mostrar los detalles del restaurante en la vista.

En resumen, esta clase actúa como una actividad en Android y se encarga de mostrar los detalles de un restaurante específico en una vista. Utiliza un objeto RestaurantDetailsPresenter para interactuar con un almacenamiento de datos (RestaurantRepository), y utiliza un RestaurantDetailsView para mostrar los detalles del restaurante en una vista.

```kotlin
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
```

**vistas/MapsActivity.kt**

"MapsActivity". Esta clase es una subclase de "AppCompatActivity" en Kotlin. También implementa la interfaz "OnMapReadyCallback".

La clase tiene una propiedad "map" de tipo "GoogleMap" y se utiliza para manejar el mapa mostrado en la vista.

La función "onCreate()" es una función de ciclo de vida de la actividad que se ejecuta cuando la actividad se crea. En esta función, primero se establece la vista de la actividad con "setContentView()" y luego se obtiene una instancia del fragmento del mapa mediante "supportFragmentManager.findFragmentById()" y se llama a "getMapAsync(this)" para inicializar el mapa. Al llamar a esta función, se registra el objeto actual para recibir la notificación de que el mapa está listo mediante la interfaz OnMapReadyCallback, de manera que se ejecutara la función onMapReady.

La función "onMapReady(googleMap: GoogleMap)" es una función implementada de OnMapReadyCallback que se ejecuta cuando el mapa está listo. En esta función, se almacena una referencia al mapa en la propiedad "map" y luego se utiliza una instancia de RestaurantRepository para obtener una lista de restaurantes, y en ese punto se podrían añadir los marcadores de los restaurantes al mapa utilizando métodos propios de googleMap como addMarker().

En resumen, esta clase actúa como una actividad en Android y se encarga de mostrar un mapa de Google utilizando Google Maps SDK. Utiliza un objeto RestaurantRepository para obtener los restaurantes y mostrarlos en el mapa con marcadores.

```kotlin
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
```

**adaptadores/RestaurantsAdapter.kt**

"RestaurantsAdapter", que extiende de la clase "RecyclerView.Adapter" y toma un parámetro de tipo "List<Restaurant>".

La clase tiene una inner class llamada "ViewHolder", que extiende de "RecyclerView.ViewHolder" y toma un parámetro de tipo "View". En esta clase interna, se espera que se enlacen los elementos de la vista con variables para poder mostrar los datos del restaurante en la vista.

La clase tiene tres funciones principales que extienden de la clase "RecyclerView.Adapter": "onCreateViewHolder()", "onBindViewHolder()" y "getItemCount()".

La función "onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder" es utilizada para crear una nueva instancia de "ViewHolder" y configurar la vista que se utilizará para mostrar los elementos del restaurante. Utiliza un LayoutInflater para inflar la vista desde un layout XML y establecer como padre el RecyclerView

La función "onBindViewHolder(holder: ViewHolder, position: Int)" es utilizada para mostrar los datos del restaurante en la vista. En esta función, se recoge el holder con la vista y se posiciona en una determinada posicion de la lista, y se actualiza la vista con los datos del restaurante correspondiente.

La función "getItemCount()" devuelve el número de elementos en la lista de restaurantes.

En resumen, esta clase actúa como un adaptador para mostrar elementos de tipo "Restaurant" en un RecyclerView. Utiliza una clase interna "ViewHolder" para mantener una referencia a los elementos de la vista, y se encarga de crear y enlazar vistas con datos de restaurantes.

```kotlin
class RestaurantsAdapter(private val restaurants: List<Restaurant>) :
    RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Enlazamos los elementos de la vista con variables
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Mostramos los datos del restaurante en la vista
    }

    override fun getItemCount() = restaurants.size
}
```

**adaptadores/RestaurantDetailsAdapter.kt**

"RestaurantDetailsAdapter", que extiende de la clase "RecyclerView.Adapter" y toma un parámetro de tipo "List<Pair<Int, String>>".

La clase tiene una inner class llamada "ViewHolder", que extiende de "RecyclerView.ViewHolder" y toma un parámetro de tipo "View". En esta clase interna, se espera que se enlacen los elementos de la vista con variables para poder mostrar los detalles del restaurante en la vista.

La clase tiene tres funciones principales que extienden de la clase "RecyclerView.Adapter": "onCreateViewHolder()", "onBindViewHolder()" y "getItemCount()".

La función "onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder" es utilizada para crear una nueva instancia de "ViewHolder" y configurar la vista que se utilizará para mostrar los elementos de los detalles del restaurante. Utiliza un LayoutInflater para inflar la vista desde un layout XML y establecer como padre el RecyclerView

La función "onBindViewHolder(holder: ViewHolder, position: Int)" es utilizada para mostrar los detalles del restaurante en la vista. En esta función, se recoge el holder con la vista y se posiciona en una determinada posicion de la lista, y se actualiza la vista con los detalles del restaurante correspondiente.

La función "getItemCount()" devuelve el número de elementos en la lista de detalles del restaurante.

En resumen, esta clase actúa como un adaptador para mostrar los detalles de un restaurante en un RecyclerView. Utiliza una clase interna "ViewHolder" para mantener una referencia a los elementos de la vista, y se encarga de crear y enlazar vistas con los detalles del restaurante.

```kotlin
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
```

**Acá tenemos los XML que van en la carpeta Layaout:**

**activity_restaurants.xml**

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <EditText
        android:id="@+id/searchEditText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <Button
        android:id="@+id/searchButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/search" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/restaurantsRecyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</LinearLayout>
```

**item_restaurant.xml**:

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal">

    <ImageView
        android:id="@+id/photoImageView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <TextView
            android:id="@+id/nameTextView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />

        <TextView
            android:id="@+id/locationTextView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />

    </LinearLayout>

</LinearLayout>
```

**activity_restaurant_details.xml**

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <ImageView
        android:id="@+id/photoImageView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

    <TextView
        android:id="@+id/nameTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

    <TextView
        android:id="@+id/ratingTextView"
```

En el archivo **`strings.xml`**
 debes colocar las cadenas de texto que se usarán en la aplicación. Por ejemplo, si en la vista de listado de restaurantes quieres mostrar un mensaje "Buscando..." mientras se realiza la búsqueda, debes agregar una entrada en **`strings.xml`**
 como esta:

```kotlin
<string name="searching">Buscando...</string>
```

Luego, en el código de la aplicación puedes acceder a esta cadena usando **`getString(R.string.searching)`**
.

Aquí te dejo algunos puntos a tener en cuenta al desarrollar este proyecto:

1. Es importante tener en cuenta que la API de restaurantes y la API de Google Maps tienen límites de uso diario y por segundo, por lo que debes implementar un sistema de cacheo para evitar exceder estos límites y evitar problemas.
2. Asegúrate de gestionar adecuadamente los errores que puedan ocurrir al consumir las APIs, ya sea por problemas de conexión o por errores en el servidor.
3. Ten en cuenta la experiencia de usuario al desarrollar la aplicación. Por ejemplo, puedes mostrar un indicador de carga mientras se obtienen los datos de las APIs o implementar un sistema de paginación para mejorar la carga de la lista de restaurantes.
4. Usa un sistema de dependencias para gestionar las librerías que utilices en el proyecto. Por ejemplo, puedes usar Gradle para gestionar las dependencias de Android y Maven para las dependencias de Java.
5. Por último, asegúrate de probar adecuadamente la aplicación en diferentes dispositivos y versiones de Android para garantizar su correcto funcionamiento.
