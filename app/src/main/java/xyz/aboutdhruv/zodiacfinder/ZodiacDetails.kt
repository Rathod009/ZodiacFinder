package xyz.aboutdhruv.zodiacfinder

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView


class ZodiacDetails : Fragment() {

    private lateinit var communicator: Communicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentView = inflater.inflate(R.layout.fragment_zodiac_details, container, false)

        val signTxt = fragmentView.findViewById<TextView>(R.id.sign)
        val signLordTxt = fragmentView.findViewById<TextView>(R.id.signLord)
        val naksahtraTxt = fragmentView.findViewById<TextView>(R.id.naksahtra)
        val varnaTxt = fragmentView.findViewById<TextView>(R.id.varna)
        val tithiTxt = fragmentView.findViewById<TextView>(R.id.tithi)



        signTxt.text = arguments?.getString("sign")
        signLordTxt.text = arguments?.getString("signLord")
        naksahtraTxt.text = arguments?.getString("naksahtra")
        varnaTxt.text = arguments?.getString("varna")
        tithiTxt.text = arguments?.getString("tithi")

        fragmentView.findViewById<Button>(R.id.backBtn).setOnClickListener{
            Log.i("INSECOND", "HERE")
            communicator = activity as Communicator
            communicator.changeFrag()
        }
        return fragmentView
    }


}