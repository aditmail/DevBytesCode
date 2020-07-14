package com.example.devbytesexercice.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.devbytesexercice.R
import com.example.devbytesexercice.adapter.ListVideoAdapter
import com.example.devbytesexercice.adapter.OnClickListener
import com.example.devbytesexercice.databinding.FragmentListVideoBinding
import com.example.devbytesexercice.viewmodels.ListVideoViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_list_video.*
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class ListVideoFragment : Fragment() {

    //private var listVideoAdapter: ListVideoAdapter? = null
    private lateinit var binding: FragmentListVideoBinding

    //Init ViewModel
    private val viewModel: ListVideoViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, ListVideoViewModel.Factory(activity.application))
            .get(ListVideoViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        collapsingToolbar.setupWithNavController(toolbar, findNavController())
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        /*viewModel.listVideo.observe(viewLifecycleOwner, Observer {
            it?.apply {
                listVideoAdapter?.submitList(it)
            }
        })*/

        (activity as AppCompatActivity).supportActionBar?.title = "Dev Bytes Videos"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListVideoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listVideoAdapter = ListVideoAdapter(OnClickListener { viewModel.onListVideoClick(it) })
        binding.rvListVideo.adapter = listVideoAdapter

        viewModel.selectedData.observe(viewLifecycleOwner, Observer { navigate ->
            navigate?.let {
                Timber.i("Navigating: ${navigate.url}")
                this.findNavController().navigate(
                    ListVideoFragmentDirections
                        .actionListVideoFragmentToDetailVideoFragment(navigate.url)
                )

                viewModel.onDoneVideoClick()
            }
        })

        viewModel.listVideo.observe(viewLifecycleOwner, Observer {
            Timber.i("Datas: ${it.size}")
        })

        viewModel.isNetworkActive.observe(viewLifecycleOwner, Observer {
            if (it != null && it != true) {
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    "No Internet Connection Found Here..",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
    }
}
