package it.marcozanetti.androidapilevels.apilevelslist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
            inflater, R.layout.api_levels_fragment, container, false)

        //val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        val application = requireNotNull(activity).application
        val viewModelFactory = ApiLevelsViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ApiLevelsViewModel::class.java)

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

        binding.list.adapter = MyAPILevelsRecyclerViewAdapter(viewModel.getAPILevels())

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