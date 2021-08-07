package xyz.aboutdhruv.zodiacfinder

import xyz.aboutdhruv.zodiacfinder.Modal.UserModal

interface Communicator {

    fun passDataCom(sign : String, signLord:String, naksahtra:String, varna:String, tithi:String)

    fun changeFrag()
}