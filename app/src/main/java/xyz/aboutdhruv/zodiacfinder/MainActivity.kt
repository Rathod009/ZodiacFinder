package xyz.aboutdhruv.zodiacfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import xyz.aboutdhruv.zodiacfinder.Modal.UserModal


class MainActivity : AppCompatActivity(), Communicator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportFragmentManager.beginTransaction()
            .add(R.id.container, InputZodiac())
            .commit()

    }

    override fun passDataCom(
        sign: String,
        signLord: String,
        naksahtra: String,
        varna: String,
        tithi: String
    ) {
        val bundle = Bundle()
        bundle.putString("sign",sign)
        bundle.putString("signLord",signLord)
        bundle.putString("naksahtra",naksahtra)
        bundle.putString("varna",varna)
        bundle.putString("tithi",tithi)

        val transaction = this.supportFragmentManager.beginTransaction()
        val fragDetails = ZodiacDetails()
        fragDetails.arguments = bundle
        transaction.replace(R.id.container, fragDetails)
        transaction.commit()

    }

    override fun changeFrag() {
        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, InputZodiac())
        transaction.commit()
    }

}