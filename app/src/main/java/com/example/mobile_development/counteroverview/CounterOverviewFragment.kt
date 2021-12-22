package com.example.mobile_development.counteroverview

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.mobile_development.R
import androidx.databinding.DataBindingUtil.inflate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mobile_development.database.CounterDatabase
import com.example.mobile_development.databinding.FragmentCounterOverviewBinding

class CounterOverviewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentCounterOverviewBinding = inflate(
            inflater, R.layout.fragment_counter_overview, container, false
        )

        val application = requireNotNull(this.activity).application


        val dataSource = CounterDatabase.getInstance(application).counterDatabaseDao
        val viewModelFactory = CounterOverviewViewModelFactory(dataSource, application)

        val counterOverviewViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(CounterOverviewViewModel::class.java)

        binding.counterOverviewViewModel = counterOverviewViewModel

        val adapter = CounterAdapter(CounterItemListener { counterId ->
            this.findNavController().navigate(CounterOverviewFragmentDirections.actionOverviewToInfo(counterId))
            true
        }, CounterButtonListener { counterId -> counterOverviewViewModel.incrementCounter(counterId) },
            CounterButtonListener {counterId -> counterOverviewViewModel.decrementCounter(counterId) })
        binding.counterList.adapter = adapter

        counterOverviewViewModel.counters.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        counterOverviewViewModel.eventCreate.observe(viewLifecycleOwner, Observer { create ->
            if (create) {

                findNavController().navigate(CounterOverviewFragmentDirections.actionOverviewToCreate())
                counterOverviewViewModel.onCreateComplete()
            }
        })

        counterOverviewViewModel.eventRestart.observe(viewLifecycleOwner, Observer { restart ->
            if (restart) {
                Toast.makeText(context, "Counter goal has been reached!\n The counter's value has been reset", Toast.LENGTH_LONG).show()
                counterOverviewViewModel.onRestartComplete()
            }
        })

        counterOverviewViewModel.eventClear.observe(viewLifecycleOwner, Observer { clear ->
            if (clear) {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to delete all the counters?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->
                        counterOverviewViewModel.onClearComplete()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()

            }
        })

        counterOverviewViewModel.countersEmpty.observe(viewLifecycleOwner, Observer { it ->
            if(it){
                binding.firstMessage.visibility=View.VISIBLE
                binding.deleteAllButton.visibility=View.GONE
            }
            else{
                binding.firstMessage.visibility=View.GONE
                binding.deleteAllButton.visibility=View.VISIBLE
            }
        })

        return binding.root
    }
}