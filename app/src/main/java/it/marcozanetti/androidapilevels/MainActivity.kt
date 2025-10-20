package it.marcozanetti.androidapilevels

import android.app.SearchManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.SearchView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.github.ybq.android.spinkit.SpinKitView
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import it.marcozanetti.androidapilevels.apilevelslist.viewmodel.ApiLevelsViewModel
import it.marcozanetti.androidapilevels.apilevelslist.viewmodel.ApiLevelsViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var searchView: SearchView
    lateinit var mainActivityViewModel: ApiLevelsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Apply system bar insets as padding to the root container
        val rootView = findViewById<View>(R.id.container)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemInsets = insets.getInsets(
                androidx.core.view.WindowInsetsCompat.Type.statusBars() or
                androidx.core.view.WindowInsetsCompat.Type.navigationBars()
            )
            v.setPadding(systemInsets.left, systemInsets.top, systemInsets.right, systemInsets.bottom)
            insets
        }

        // Enable edge-to-edge (draw behind system bars)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // Set status bar and navigation bar color and icon appearance
        val isLightTheme = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK != android.content.res.Configuration.UI_MODE_NIGHT_YES
        val statusBarColor = if (isLightTheme) {
            ContextCompat.getColor(this, R.color.status_bar_light_blue)
        } else {
            ContextCompat.getColor(this, R.color.status_bar_translucent_dark)
        }
        setStatusBarColor(window, statusBarColor)
        Log.d("MainActivity", "Status bar color set: $statusBarColor, isLightTheme: $isLightTheme")

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        // Set icon appearance after color
        if (isLightTheme) {
            controller.isAppearanceLightStatusBars = false // Light icons for blue background
            controller.isAppearanceLightNavigationBars = false
        } else {
            controller.isAppearanceLightStatusBars = true // Dark icons for dark background
            controller.isAppearanceLightNavigationBars = true
        }

        // ProgressBar is instantiated in activity in order to be displayed immediately,
        // before the Fragment is attached
        val indeterminateBar = findViewById<SpinKitView>(R.id.indeterminateBar)
        indeterminateBar.visibility = View.VISIBLE
        val doubleBounce: Sprite = DoubleBounce()
        indeterminateBar.setIndeterminateDrawable(doubleBounce)

        val viewModelFactory = ApiLevelsViewModelFactory(application)
        mainActivityViewModel = ViewModelProvider(this, viewModelFactory).get(ApiLevelsViewModel::class.java)

        mainActivityViewModel.displaySearchView.observe(this, Observer { shouldDisplaySearchView ->
            if (this::searchView.isInitialized) {
                if (shouldDisplaySearchView) {
                    searchView.visibility = SearchView.VISIBLE
                } else {
                    searchView.visibility = SearchView.GONE
                }
            }
        })

        mainActivityViewModel.apiLevelItems.observe(this, Observer {
            if(it.isNotEmpty()) {
                //Once the list is full the progressBar should disappear
                indeterminateBar.visibility = View.GONE
            }
        })

        // Handle back press using OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this) {
            if (this@MainActivity::searchView.isInitialized && searchView.isVisible && !searchView.isIconified) {
                searchView.onActionViewCollapsed()
            } else {
                // Call the default back action
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
                isEnabled = true
            }
        }
    }

    fun setStatusBarColor(window: Window, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) { // Android 15+
            window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
                view.setBackgroundColor(color)
                insets
            }
        } else {
            // For Android 14 and below
            window.statusBarColor = color
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.app_bar_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        // Retrieves searchView
        searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(componentName)
        )

        /**
         * Function that performs the search. Retrieves the ViewModel and calls the search method
         * with the search query provided in the search bar of the activity
         */
        fun performSearch(query: String = "") {
            if(query == "")
                mainActivityViewModel.resetData()
            else
                mainActivityViewModel.filterData(query)
        }

        /**
         * I couldn't find a better way to map searchView events than attaching explicitly some
         * listeners to it. I don't like it, I think there must be a cleaner way somehow.
         * Still, it works.
         */
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // This is called when the user presses 'ok' or 'search' key on the keyboard
            override fun onQueryTextSubmit(query: String): Boolean {
                performSearch(query)
                return true
            }

            // This is called each time the user types a key in the search string
            override fun onQueryTextChange(newText: String): Boolean {
                 performSearch(newText) // If search box is cleared we perform a search neverthless
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.app_bar_search -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()


    override fun onStart() {
        super.onStart()

        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController)

    }
}
