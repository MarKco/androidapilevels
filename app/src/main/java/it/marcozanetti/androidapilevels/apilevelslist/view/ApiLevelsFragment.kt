package it.marcozanetti.androidapilevels.apilevelslist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import it.marcozanetti.androidapilevels.R
import it.marcozanetti.androidapilevels.apilevelslist.viewmodel.ApiLevelsViewModel
import it.marcozanetti.androidapilevels.apilevelslist.viewmodel.ApiLevelsViewModelFactory
import it.marcozanetti.androidapilevels.databinding.ApiLevelsFragmentBinding

/**
 * A fragment representing a list of Items.
 */
class ApiLevelsFragment : Fragment() {

    private var columnCount = 1

    private lateinit var viewModel: ApiLevelsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: ApiLevelsFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.api_levels_fragment, container, false
        )

        val application = requireNotNull(activity).application
        val viewModelFactory = ApiLevelsViewModelFactory(application)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ApiLevelsViewModel::class.java)

        binding.apiLevelsViewModel = viewModel

        // Set the adapter
        binding.list.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
        binding.list.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        viewModel.retrieveApiLevelData()    // Asking the ViewModel to get API data
                                            // (from web and/or from local storage)

        // Once the elements are retrieved by the ViewModel we load them in the adapter
        viewModel.apiLevelItems.observe(viewLifecycleOwner, Observer { value ->
            binding.list.adapter = MyAPILevelsRecyclerViewAdapter(
                value
            )
        })

        viewModel.exceptionsWhileRetrieving.observe(viewLifecycleOwner, Observer {
            //Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            Toast.makeText(activity, getString(R.string.error_message_generic), Toast.LENGTH_LONG).show()
        })

        return binding.root
    }
}