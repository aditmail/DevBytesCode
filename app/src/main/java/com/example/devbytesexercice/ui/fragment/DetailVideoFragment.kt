package com.example.devbytesexercice.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.devbytesexercice.R
import com.example.devbytesexercice.database.getDatabase
import com.example.devbytesexercice.databinding.FragmentDetailVideoBinding
import com.example.devbytesexercice.util.getYoutubeIntent
import com.example.devbytesexercice.viewmodels.DetailVideoViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_detail_video.*
import kotlinx.android.synthetic.main.fragment_list_video.toolbar
import timber.log.Timber

class DetailVideoFragment : Fragment() {

    private lateinit var binding: FragmentDetailVideoBinding
    private lateinit var url: String

    private val viewModel: DetailVideoViewModel by lazy {
        val application = this.activity!!.application
        val dataSource = getDatabase(application).videoDAO

        val argument = DetailVideoFragmentArgs.fromBundle(arguments!!).keyUrl
        val viewModelFactory = DetailVideoViewModel.Factory(dataSource, argument)

        ViewModelProvider(this, viewModelFactory)
            .get(DetailVideoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailVideoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collapsingToolbar.setupWithNavController(toolbar, findNavController())
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        viewModel.getVideoDetail().observe(viewLifecycleOwner, Observer { navigate ->
            //change dynamically name of title in fragment
            //(activity as MainActivity).viewModelMain._navigationTitle.value= navigate.title

            //Or use this, but the previous title will be carried too..
            //(requireActivity() as MainActivity).title = it.title

            //Or use this, from the udacity.. The Best Implementation
            //(activity as AppCompatActivity).supportActionBar?!.title = navigate.title
            url = navigate.url
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_youtube -> startActivities(openInYoutube())
            R.id.item_browser -> startActivities(openInBrowser())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openInYoutube(): Intent? {
        var intent: Intent? = null

        if (url != "null") {
            val packageManager = context!!.packageManager
            intent = try {
                Intent(Intent.ACTION_VIEW, getYoutubeIntent(url))
            } catch (e: NullPointerException) {
                Timber.i("Error Intent Null: $e")
                null
            }

            if (intent?.resolveActivity(packageManager) == null) {
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    "No Youtube Apps Found:( Open in Browser",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        return intent
    }

    private fun openInBrowser(): Intent? {
        return try {
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
        } catch (e: NullPointerException) {
            Timber.i("Error Intent Null: $e")
            null
        }
    }

    private fun startActivities(intent: Intent?) {
        if (intent == null) {
            Toast.makeText(context, "There's Something Error", Toast.LENGTH_SHORT).show()
        } else {
            startActivity(intent)
        }
    }
}
