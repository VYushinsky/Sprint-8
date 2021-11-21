package com.example.retailer.adapter

interface Consumer {
    // update upon receiving
    fun receiveUpdate(incomingMsg: String)
}