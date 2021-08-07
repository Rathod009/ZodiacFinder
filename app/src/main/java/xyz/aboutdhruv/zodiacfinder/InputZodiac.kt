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
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.aboutdhruv.zodiacfinder.Api.BasicAuthInterceptor
import xyz.aboutdhruv.zodiacfinder.Api.ZodiacApi
import xyz.aboutdhruv.zodiacfinder.Modal.UserModal
import java.util.*


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
            val userDetail = UserModal(day,month+1,year, hour, minute)
            val resp = zodiacApi.getZodiacDetails(userDetail)

            resp.enqueue(object : Callback<UserModal>{
                override fun onResponse(call: Call<UserModal>, response: Response<UserModal>) {
                    Log.i(TAG, "" + response.code())
                    if(response.code() == 401)
                        Toast.makeText(requireContext(),"API ISSUE",Toast.LENGTH_LONG).show()
                    else
                    {
                        Log.i(TAG, "" + response.body())
                        val temp: UserModal? = response.body()
                        if (temp != null) {
                            communicator.passDataCom(temp.sign,temp.SignLord,temp.Naksahtra, temp.Varna,temp.Tithi)
                        }
                    }
                }

                override fun onFailure(call: Call<UserModal>, t: Throwable) {

                    Log.i(TAG, "FAIL " + t.message.toString())

                }

//
            })
        }

        return fragmentView;
    }
}
