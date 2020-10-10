package com.example.notificationreader

import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var nreceiver: NotificationReceiver
    lateinit var model: TransactionsViewModel
    lateinit var adapter: NotificationListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = ViewModelProvider(this).get(TransactionsViewModel::class.java)
        model.refresh()

        nreceiver = NotificationReceiver(this)
        val filter = IntentFilter()
        filter.addAction("com.example.notificationreader.NEW_NOTIFICATION")
        registerReceiver(nreceiver, filter)

        adapter = NotificationListAdapter().apply { dataset = model.transactions }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(nreceiver)
    }

    fun updateNotifications() {
        model.refresh()
        adapter.dataset = model.transactions
    }

}