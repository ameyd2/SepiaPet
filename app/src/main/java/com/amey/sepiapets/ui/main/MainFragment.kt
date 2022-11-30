package com.amey.sepiapets.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amey.sepiapets.R
import com.amey.sepiapets.models.Pet
import com.amey.sepiapets.models.PetsModel
import com.amey.sepiapets.models.WorkingHrsModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_main.view.*
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var petsListRvAdapter: PetsListRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        // TODO: Use the ViewModel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =  inflater.inflate(R.layout.fragment_main, container, false)
        initViews(view)
        val workingHrs = checkForWorkingHrs()
        if (workingHrs){

            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Not In Working Hours!")
            builder.setMessage("You can use application in working hours.")


            builder.setPositiveButton("Close") { dialog, which ->

                requireActivity().finishAffinity()
            }


            builder.show()

        }
        else {
            val jsonStr = getJsonDataFromAsset(requireContext(), "pets_list.json")
            val gson = Gson()

            var petsList: PetsModel = gson.fromJson(jsonStr, PetsModel::class.java)
            petsListRvAdapter = PetsListRvAdapter(petsList.pets as ArrayList<Pet>, viewModel)
            view.pets_list_rv.adapter = petsListRvAdapter
        }
        return view
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkForWorkingHrs():Boolean{
        var workingHrs = false
        val jsonStr = getJsonDataFromAsset(requireContext(),"config.json")
        val gson = Gson()
        val workingHrsData: WorkingHrsModel = gson.fromJson(jsonStr, WorkingHrsModel::class.java)


        if (!workingHrsData.settings.workingDays.startsWith("S")){


            val calendar: Calendar = Calendar.getInstance()

            val currentTime = calendar.get(Calendar.HOUR_OF_DAY).toString()+":"+calendar.get(Calendar.MINUTE).toString()
            val totalHrs = workingHrsData.settings.workHours.split("-")
            val hourOfDay = parseDate(currentTime)
            val startTime = parseDate(totalHrs[0])
            val endTime = parseDate(totalHrs[1])
            if (startTime!!.before( hourOfDay ) && endTime!!.after(hourOfDay)) {
                workingHrs = true
            }

        }


        return workingHrs

    }
    private fun parseDate(date: String): Date? {
        val inputFormat = "HH:mm"
        val inputParser = SimpleDateFormat(inputFormat, Locale.US)
        return try {
            inputParser.parse(date)
        } catch (e: ParseException) {
            Date(0)
        }
    }
    private fun initViews(view: View){

        view.pets_list_rv.isNestedScrollingEnabled = false
        view.pets_list_rv.setHasFixedSize(true)
        view.pets_list_rv.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }
    private fun getJsonDataFromAsset(context: Context, fileName: String) :String{
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }

        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return ""
        }

        return jsonString
    }
}