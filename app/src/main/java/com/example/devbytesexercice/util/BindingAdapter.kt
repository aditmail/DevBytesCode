package com.example.devbytesexercice.util

import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.devbytesexercice.R
import com.example.devbytesexercice.adapter.ListProfileAdapter
import com.example.devbytesexercice.adapter.ListVideoAdapter
import com.example.devbytesexercice.database.DatabaseProfile
import com.example.devbytesexercice.database.DatabaseVideo
import com.example.devbytesexercice.models.Video
import com.example.devbytesexercice.ui.dialog.ErrorStatus
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber

@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("setRecyclerVisible")
fun isRecycleVisible(view: View, boolean: Boolean) {
    view.visibility = if (boolean) View.VISIBLE else View.GONE
}

@BindingAdapter("setNoConnectionVisible")
fun isNoConnectionVisible(view: View, boolean: Boolean) {
    view.visibility = if (boolean) View.GONE else View.VISIBLE
}

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String) {
    Glide.with(imageView.context)
        .load(url)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_launcher_background)
        )
        .into(imageView)
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Video>?) {
    val adapter = recyclerView.adapter as ListVideoAdapter
    adapter.submitList(data)
}

@BindingAdapter("listDataProfile")
fun bindingListProfile(recyclerView: RecyclerView, data: List<DatabaseProfile>?) {
    data?.let {
        val adapter = recyclerView.adapter as ListProfileAdapter
        Timber.i("List Data $data")
        adapter.submitList(data)
    }
}

@BindingAdapter("data", "int") //Bisa Gini
//@BindingAdapter(value = ["bind:data","bind:int"]) //Atau Gini
fun bindVisibleClear(view: View, data: List<DatabaseProfile>?, int: Int) {
    data?.let {
        if (data.isNotEmpty()) {
            view.visibility = View.VISIBLE
        } else {
            when (int) {
                0 -> view.visibility = View.GONE
                1 -> view.visibility = View.INVISIBLE
                else -> view.visibility = View.VISIBLE
            }
        }
    }
}

@BindingAdapter("viewVideo")
fun setVideoView(webView: WebView, item: DatabaseVideo?) {
    item?.let {
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        //webView.settings.loadWithOverviewMode = true
        //webView.settings.useWideViewPort = true

        val embeddedCode = yotubeEmbedded(getCodeYoutube(item.url).toString())
        webView.loadData(embeddedCode, "text/html; charset=utf-8", null)
    }
}

@BindingAdapter("updateAt")
fun TextView.setDateUpdate(item: DatabaseVideo?) {
    item?.let {
        text = convertDate(item.updated, context.resources)
    }
}

@BindingAdapter("errorUsername", "errorEmail", "errorPhone", "status", requireAll = false)
fun TextInputLayout.setError(
    errorUsername: String?,
    errorEmail: String?,
    errorPhone: String?,
    status: ErrorStatus?
) {
    status?.let {
        when (status) {
            ErrorStatus.USERNAME -> {
                errorUsername?.let {
                    error = errorUsername
                }
            }
            ErrorStatus.EMAIL -> {
                errorEmail?.let {
                    error = errorEmail
                }
            }
            ErrorStatus.PHONE -> {
                errorPhone?.let {
                    error = errorPhone
                }
            }
            else -> error = null
        }
    }
}

/*@BindingAdapter("errorUsername", "status")
fun TextInputLayout.setErrorUsername(errorUsername: String?, status: ErrorStatus) {
    errorUsername?.let {
        if (status == ErrorStatus.CLEAR) {
            error = null
        } else {
            error = errorUsername
            requestFocus()
        }
    }
}

@BindingAdapter("errorPhone", "status")
fun TextInputLayout.setErrorPhone(errorPhone: String?, status: ErrorStatus) {
    errorPhone?.let {
        if (status == ErrorStatus.CLEAR) {
            error = null
        } else {
            error = errorPhone
            requestFocus()
        }
    }
}

@BindingAdapter("errorEmail", "status")
fun TextInputLayout.setErrorEmail(errorEmail: String?, status: ErrorStatus) {
    errorEmail?.let {
        if (status == ErrorStatus.CLEAR) {
            error = null
        } else {
            error = errorEmail
            requestFocus()
        }
    }
}*/

@BindingAdapter("validateUsername")
fun TextInputLayout.validateUsername(username: String?) {
    username?.let {
        error = when {
            username.isEmpty() -> "Please insert your name"
            username.contains(" ") -> "Spasing isn't Allow"
            else -> null
        }
    }
}

/*
@BindingAdapter("valueListener")
fun setValueListener(editText: TextInputEditText, values: String?) {
    values?.let {
        editText.doAfterTextChanged { text ->
            values = text?.toString()?.trim() ?: ""
        }
    }
}*/
