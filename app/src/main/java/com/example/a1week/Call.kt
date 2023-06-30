package com.example.a1week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class Call : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = inflater.inflate(R.layout.fragment_call, container, false)
        val listView: ListView = contentView.findViewById(R.id.list_view)


        // sample data
        val list: MutableList<String> = ArrayList()
        for (i in 0..99) list.add("Item $i")
        val listAdapter = ArrayAdapter(this.requireContext(), R.layout.simple_list_item, list)
        listView.adapter = listAdapter
        return contentView
    }
}