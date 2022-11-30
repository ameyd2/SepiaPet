package com.amey.sepiapets.ui.main

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amey.sepiapets.R
import kotlinx.android.synthetic.main.fragment_content.view.*

// TODO: Rename parameter arguments, choose names that match

class ContentFragment : Fragment() {

    companion object {
        fun newInstance() = ContentFragment()
    }

    // TODO: Rename and change types of parameters
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_content, container, false)

        viewModel.contentUrl.observe(viewLifecycleOwner, Observer {
            // updating data in displayMsg
            webViewSetup(view,it)
        })
        return view
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun webViewSetup(view: View,url:String) {
        view.content_webview.webViewClient = WebViewClient()


        view.content_webview.apply {

            loadUrl(url)
            settings.javaScriptEnabled = true
            settings.safeBrowsingEnabled = true
            settings.domStorageEnabled = true
            settings.setAppCacheEnabled(true)
            settings.mediaPlaybackRequiresUserGesture = false
            settings.allowFileAccess = true
        }





    }

}