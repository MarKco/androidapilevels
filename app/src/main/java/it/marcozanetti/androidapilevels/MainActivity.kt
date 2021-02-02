package it.marcozanetti.androidapilevels

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
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
                    searchView.setVisibility(SearchView.VISIBLE)
                } else {
                    searchView.setVisibility(SearchView.GONE)
                }
            }
        })

        mainActivityViewModel.apiLevelItems.observe(this, Observer {
            if(it.isNotEmpty()) {
                //Once the list is full the progressBar should disappear
                indeterminateBar.visibility = View.GONE
            }
        })
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

    override fun onBackPressed() {
        if (this::searchView.isInitialized
            && searchView.isVisible
            && !searchView.isIconified) {
            searchView.onActionViewCollapsed()
        }
        else {
            super.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()

        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController)

    }
}