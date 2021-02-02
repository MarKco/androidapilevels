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
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

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
            Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            ApiLevelsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}