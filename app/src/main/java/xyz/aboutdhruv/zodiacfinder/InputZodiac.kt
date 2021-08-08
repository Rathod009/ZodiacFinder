package xyz.aboutdhruv.zodiacfinder

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.aboutdhruv.zodiacfinder.Api.BasicAuthInterceptor
import xyz.aboutdhruv.zodiacfinder.Api.ZodiacApi
import xyz.aboutdhruv.zodiacfinder.Modal.UserModal


class InputZodiac : Fragment() {

    private val TAG:String = "ZODIAC"
    private lateinit var communicator: Communicator


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentView = inflater.inflate(R.layout.fragment_input_zodiac, container, false)

        //DATE HOLDER
        val dateHolder = fragmentView.findViewById<TextView>(R.id.dateHolder)
        //TIME HOLDER
        val timeHolder = fragmentView.findViewById<TextView>(R.id.timeHolder)
        //FIND BTN
        val findbtn = fragmentView.findViewById<Button>(R.id.findbtn)

        //VARIABLES
        var year = 2020
        var month = 0
        var day = 1

        var hour = 0;
        var minute = 0;

        //DATE PICKER
        dateHolder.setOnClickListener{
            val datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener{view, yy, mm, dd ->
                dateHolder.text = "" +  dd + " / " + (mm+1) + " / " +yy
                year = yy
                month = mm
                day = dd

            },year,month,day)
            datePickerDialog.show()

            Log.i(TAG, "" + hour + " : " +minute+ "   "+ day+"/"+month+"/"+year)
        }

        //TIME PICKER
        timeHolder.setOnClickListener{
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(0)
                .setMinute(0)
                .setTitleText("Birth Time")
                .build()
            picker.show(childFragmentManager, "TAG")


            picker.addOnPositiveButtonClickListener{
                timeHolder.text = "" + picker.hour + " : " + picker.minute
                hour = picker.hour
                minute = picker.minute
            }

            Log.i(TAG, "" + hour + " : " +minute+ "   "+ day+"/"+month+"/"+year)
        }

        //FIND BTN CLICK
        findbtn.setOnClickListener{
            Log.i(TAG, "" + hour + " : " +minute+ "   "+ day+"/"+month+"/"+year)

            communicator = activity as Communicator
            val client =  OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptor("61732","362688cde85630c308f11e28ad6bbd65"))
                .build()
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://json.astrologyapi.com")
                .client(client)
                .build()



            val zodiacApi = retrofit.create(ZodiacApi::class.java)
            // Create JSON using JSONObject
            val jsonObject = JSONObject()
            jsonObject.put("day", day)
            jsonObject.put("month", month+1)
            jsonObject.put("year", year)
            jsonObject.put("hour", hour)
            jsonObject.put("min", minute)
            jsonObject.put("lat", 22.29940)
            jsonObject.put("lon", 73.20812)
            jsonObject.put("tzone", 5.5)

            
            // Convert JSONObject to String
            val jsonObjectString = jsonObject.toString()

            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            CoroutineScope(Dispatchers.IO).launch {
                // Do the POST request and get response
                val response = zodiacApi.getZodiacDetails(requestBody)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        // Convert raw JSON to pretty JSON using GSON library
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val prettyJson = gson.toJson(
                            JsonParser.parseString(
                                response.body()
                                    ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                            )
                        )
                        val gsonCon = Gson()
                        val temp : UserModal = gson.fromJson(prettyJson, UserModal::class.java)

                        Log.i("Pretty Printed JSON :", prettyJson)

                        communicator.passDataCom(temp.sign,temp.SignLord,temp.Naksahtra, temp.Varna,temp.Tithi)

                    } else {

                        Log.i("RETROFIT_ERROR", response.code().toString())
                        Toast.makeText(requireContext(),"API ISSUE "+response.code().toString(),Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }

        return fragmentView;
    }

}