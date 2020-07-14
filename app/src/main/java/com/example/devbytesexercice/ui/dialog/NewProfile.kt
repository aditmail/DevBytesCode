package com.example.devbytesexercice.ui.dialog

import android.app.Application
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.*
import com.example.devbytesexercice.R
import com.example.devbytesexercice.databinding.DialogNewProfileBinding
import com.example.devbytesexercice.ui.activity.MainActivity
import com.example.devbytesexercice.util.isEmailValid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

class NewProfile : DialogFragment() {

    private lateinit var activity: MainActivity

    companion object {
        fun newInstance(activity: MainActivity): NewProfile {
            val newProfile = NewProfile()
            newProfile.activity = activity
            return newProfile
        }
    }

    private val viewModel: NewProfileViewModel by lazy {
        val activity = this.activity
        ViewModelProvider(this, NewProfileViewModel.Factory(activity.application))
            .get(NewProfileViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogNewProfileBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.etUsername.doAfterTextChanged { text ->
            viewModel.username = text?.toString()?.trim() ?: ""
        }

        binding.etEmail.doAfterTextChanged { text ->
            viewModel.email = text?.toString()?.trim() ?: ""
        }

        binding.etPhone.doAfterTextChanged { text ->
            viewModel.phone = text?.toString()?.trim() ?: ""
        }


        /*viewModel.isFormValidate.observe(viewLifecycleOwner, Observer { valid ->
            binding.btnAddProfile.isEnabled = valid ?: false
        })*/
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.DialogThemes
    }

    override fun onStart() {
        super.onStart()
        val dialogFragment = dialog
        if (dialogFragment != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialogFragment.window?.setLayout(width, height)
        }
    }

    /*override fun <T> afterTextChanged(viewModel: T) {
    }*/

    /** First Model **/
    /*override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialog_new_profile, null, false)

        val alertDialogFragment = AlertDialog.Builder(context, R.style.DialogThemes)
            .setView(view)
            //.setTitle("Hellows")
            .setCancelable(false)
            //.setPositiveButton("OK", null)
            .create()

        alertDialogFragment.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialogFragment.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialogFragment.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        alertDialogFragment.setCanceledOnTouchOutside(false)
        alertDialogFragment.setCancelable(false)

        return alertDialogFragment
    }*/

    /** Second Model **/
    /*companion object {
        fun NewProfile(context: Context) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogNewProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun getTheme(): Int {
        //return R.style.Theme_MaterialComponents_Dialog
        return R.style.DialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }*/
}

class NewProfileViewModel(application: Application) : AndroidViewModel(application),
    InputLayoutText.ErrorInputLayout {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val applicationModel = application

    private val _status = MutableLiveData<ErrorStatus>()
    val status: LiveData<ErrorStatus>
        get() = _status

    private val _isFormValidate = MutableLiveData<Boolean>()
    val isFormValidate: LiveData<Boolean>
        get() = _isFormValidate

    private val _errorText = MutableLiveData<String>()
    val errorText: LiveData<String>
        get() = _errorText

    var username = ""
        set(value) {
            field = value
            validateUsername()
        }

    var email = ""
        set(value) {
            field = value
            validateEmail()
        }

    var phone = ""
        set(value) {
            field = value
            validatePhone()
        }

    private fun validateUsername() {
        when {
            username.isEmpty() -> {
                errorValue("Insert Name", ErrorStatus.USERNAME)
            }
            username.contains(" ") -> {
                errorValue("No Space Allow", ErrorStatus.USERNAME)
                Timber.i("ErrorName: ${errorText.value} Status: ${status.value}")
            }
            else -> {
                errorSolve()
            }
        }
    }

    private fun validatePhone() {
        if (phone.isEmpty()) {
            errorValue("Insert Phone", ErrorStatus.PHONE)
        } else if (!phone.startsWith("628")) {
            errorValue("Start With 628", ErrorStatus.PHONE)
        } else if (phone.length <= 10 || phone.length > 16) {
            errorValue("Start in 10-16 Number", ErrorStatus.PHONE)
        } else {
            errorSolve()
        }
    }

    private fun validateEmail() {
        if (email.isNotEmpty() && !isEmailValid(email)) {
            errorValue("Format Email: XXX@XXX.XXX", ErrorStatus.EMAIL)
        } else {
            errorSolve()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewProfileViewModel::class.java)) {
                return NewProfileViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to Construct the ViewModel")
        }
    }

    override fun errorValue(value: String, errorStatus: ErrorStatus) {
        _isFormValidate.postValue(false)
        _errorText.value = value
        _status.value = errorStatus
        return
    }

    override fun errorSolve() {
        _errorText.value = null
        _status.value = ErrorStatus.CLEAR
        _isFormValidate.value = true
    }
}

interface InputLayoutText {

    interface ErrorInputLayout {
        fun errorValue(value: String, errorStatus: ErrorStatus)
        fun errorSolve()
    }

    /*interface SetInputValue {
        fun <T> afterTextChanged(viewModel: T)
    }*/
}

enum class ErrorStatus {
    USERNAME,
    EMAIL,
    PHONE,
    CLEAR
}