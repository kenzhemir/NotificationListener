package com.example.notificationreader.fragments

import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notificationreader.R
import com.example.notificationreader.adapters.NotificationListAdapter
import com.example.notificationreader.notifications.BroadcastReceiver
import com.example.notificationreader.viewmodels.TransactionsViewModel
import kotlinx.android.synthetic.main.fragment_notification_list.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [NotificationListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationListFragment : Fragment() {

    lateinit var nreceiver: BroadcastReceiver
    lateinit var model: TransactionsViewModel
    lateinit var adapter: NotificationListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(this).get(TransactionsViewModel::class.java)
        model.refresh()

        nreceiver = BroadcastReceiver {
            model.refresh()
            adapter.dataset = model.transactions
        }
        val filter = IntentFilter()
        filter.addAction("com.example.notificationreader.NEW_NOTIFICATION")
        requireActivity().registerReceiver(nreceiver, filter)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notification_list, container, false)

        adapter = NotificationListAdapter().apply { dataset = model.transactions }
        view.recyclerView.adapter = adapter
        view.recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(nreceiver)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NotificationListFragment()
    }
}