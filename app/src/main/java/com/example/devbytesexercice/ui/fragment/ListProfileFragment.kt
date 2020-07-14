package com.example.devbytesexercice.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController

import com.example.devbytesexercice.R
import com.example.devbytesexercice.adapter.ListProfileAdapter
import com.example.devbytesexercice.adapter.OnClickProfile
import com.example.devbytesexercice.databinding.FragmentListProfileBinding
import com.example.devbytesexercice.ui.activity.MainActivity
import com.example.devbytesexercice.ui.dialog.NewProfile
import com.example.devbytesexercice.viewmodels.ListProfileViewModel
import kotlinx.android.synthetic.main.fragment_list_video.*

class ListProfileFragment : Fragment() {

    private lateinit var binding: FragmentListProfileBinding

    private val viewModel: ListProfileViewModel by lazy {
        val activity = this.activity
        ViewModelProvider(this, ListProfileViewModel.Factory(activity!!.application))
            .get(ListProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListProfileBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setupWithNavController(findNavController())

        val adapter = ListProfileAdapter(OnClickProfile { profile ->
            Toast.makeText(context, "Data: ${profile.username}", Toast.LENGTH_SHORT).show()
        })

        binding.btnAddData.setOnClickListener {
            //val newProfileDialog = NewProfile()
            val newProfileDialog = NewProfile.newInstance(activity as MainActivity)
            newProfileDialog.show(activity!!.supportFragmentManager, "Profile")
        }
        binding.rvProfile.adapter = adapter
    }

}
