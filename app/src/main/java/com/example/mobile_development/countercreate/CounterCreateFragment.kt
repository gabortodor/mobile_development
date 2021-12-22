package com.example.mobile_development.countercreate

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mobile_development.R
import com.example.mobile_development.database.CounterDatabase
import com.example.mobile_development.databinding.FragmentCounterCreateBinding
import petrov.kristiyan.colorpicker.ColorPicker
import petrov.kristiyan.colorpicker.ColorPicker.OnChooseColorListener


class CounterCreateFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCounterCreateBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_counter_create, container, false
        )

        val application = requireNotNull(this.activity).application


        val dataSource = CounterDatabase.getInstance(application).counterDatabaseDao
        val viewModelFactory = CounterCreateViewModelFactory(dataSource, application)
        var counterColor: Int = -5658199

        val counterCreateViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(CounterCreateViewModel::class.java)

        binding.counterCreateViewModel = counterCreateViewModel

        counterCreateViewModel.eventCreate.observe(viewLifecycleOwner, Observer { create ->
            if (create) {
                if (binding.nameEditText.text.isEmpty()) {
                    Toast.makeText(context, "A label must be entered", Toast.LENGTH_LONG).show()

                } else {
                    var value: Int = 0
                    var increase: Int = 1
                    var decrease: Int = 1
                    var goal: Int = 100
                    var cont: Boolean =true
                    try {

                        if (!binding.valueEditText.text.isEmpty()) value =
                            binding.valueEditText.text.toString().toInt()

                        if (!binding.increaseEditText.text.isEmpty()) increase =
                            binding.increaseEditText.text.toString()
                                .toInt()

                        if (!binding.decreaseEditText.text.isEmpty()) decrease =
                            binding.decreaseEditText.text.toString()
                                .toInt()

                        if (!binding.goalValueEditText.text.isEmpty()) goal =
                            binding.goalValueEditText.text.toString()
                                .toInt()
                    } catch (e: NumberFormatException) {
                        Toast.makeText(
                            context,
                            "All values must be between ${Integer.MIN_VALUE} and ${Integer.MAX_VALUE}",
                            Toast.LENGTH_LONG
                        ).show()
                        cont=false
                    }
                    if(cont) {
                        counterCreateViewModel.createCounter(
                            binding.nameEditText.text.toString(),
                            counterColor,
                            value,
                            increase,
                            decrease,
                            goal
                        )
                        val imm =
                            requireNotNull(activity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(requireNotNull(this.view).windowToken, 0)

                        findNavController().navigate(CounterCreateFragmentDirections.actionCreateToOverview())
                        counterCreateViewModel.onCreateComplete()
                    }
                }
            }
        })

        counterCreateViewModel.eventCancel.observe(viewLifecycleOwner, Observer { cancel ->
            if (cancel) {
                findNavController().navigate(CounterCreateFragmentDirections.actionCreateToOverview())
                counterCreateViewModel.onCancelComplete()
            }
        })

        counterCreateViewModel.eventColor.observe(viewLifecycleOwner, Observer { color ->
            if (color) {
                val colorPicker: ColorPicker = ColorPicker(activity)
                colorPicker.setRoundColorButton(true);
                colorPicker.show()
                colorPicker.setOnChooseColorListener(object : OnChooseColorListener {
                    override fun onChooseColor(position: Int, color: Int) {

                        Log.i("color", ("A9A9A9").toLong(radix = 16).toString())
                        counterColor = color
                        binding.colorButton.setBackgroundColor(color)
                        binding.colorButton.text = "Edit"
                        counterCreateViewModel.onColorComplete()
                    }

                    override fun onCancel() {
                        counterCreateViewModel.onColorComplete()
                    }
                })


                counterCreateViewModel.onCancelComplete()
            }
        })

        counterCreateViewModel.eventAdvanced.observe(viewLifecycleOwner, Observer { advanced ->
            if (advanced) {
                binding.advancedSettings.visibility = View.VISIBLE
                binding.advancedButton.visibility = View.GONE
            }
            else {
                binding.advancedSettings.visibility = View.GONE
                binding.advancedButton.visibility = View.VISIBLE
            }
        })

        return binding.root
    }
}