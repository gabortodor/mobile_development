package com.example.mobile_development.counterinfo

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mobile_development.R
import com.example.mobile_development.database.CounterDatabase
import com.example.mobile_development.databinding.FragmentCounterInfoBinding
import kotlinx.android.synthetic.main.fragment_counter_info.*

class CounterInfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentCounterInfoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_counter_info, container, false
        )

        val application = requireNotNull(this.activity).application
        val arguments = CounterInfoFragmentArgs.fromBundle(arguments)


        val dataSource = CounterDatabase.getInstance(application).counterDatabaseDao
        val viewModelFactory = CounterInfoViewModelFactory(arguments.counterId, dataSource)


        val counterInfoViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(CounterInfoViewModel::class.java)


        binding.counterInfoViewModel = counterInfoViewModel

        binding.setLifecycleOwner(this)

        counterInfoViewModel.eventDelete.observe(viewLifecycleOwner, Observer { delete ->
            if (delete) {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Delete counter?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->
                        counterInfoViewModel.onDeleteComplete()
                        findNavController().navigate(CounterInfoFragmentDirections.actionInfoToOverview())
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
        })

        counterInfoViewModel.eventUpdate.observe(viewLifecycleOwner, Observer { update ->
            when (update) {
                counterInfoViewModel.UPDATE_START -> {
                    setEditable(binding)
                    binding.valueInfoEditText.requestFocus()
                    val imm =
                        requireNotNull(activity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(requireNotNull(this.view), InputMethodManager.SHOW_IMPLICIT)
                }
                counterInfoViewModel.UPDATE_FINISH -> {
                    setUneditable(binding)

                    counterInfoViewModel.updateCounter(
                        binding.nameInfoEditText.text.toString(),
                        binding.valueInfoEditText.text.toString(),
                        binding.decreaseInfoEditText.text.toString(),
                        binding.increaseInfoEditText.text.toString(),
                        binding.goalInfoEditText.text.toString()
                    )
                    counterInfoViewModel.onUpdateHide()
                }
            }
        })


        return binding.root
    }

    fun setEditable(binding: FragmentCounterInfoBinding){
        binding.apply {
            valueInfoEditText.isEnabled = true
            valueInfoEditText.paintFlags =
                valueInfoEditText.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            nameInfoEditText.isEnabled = true
            nameInfoEditText.paintFlags =
                nameInfoEditText.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            decreaseInfoEditText.isEnabled = true
            decreaseInfoEditText.paintFlags =
                decreaseInfoEditText.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            increaseInfoEditText.isEnabled = true
            increaseInfoEditText.paintFlags =
                increaseInfoEditText.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            goalInfoEditText.isEnabled = true
            goalInfoEditText.paintFlags =
                goalInfoEditText.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            updateFloatingButton.visibility = View.GONE
            updateDoneFloatingButton.visibility = View.VISIBLE
        }
    }

    fun setUneditable(binding: FragmentCounterInfoBinding){
        binding.apply {
            valueInfoEditText.isEnabled = false
            valueInfoEditText.paintFlags=valueInfoEditText.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
            nameInfoEditText.isEnabled = false
            nameInfoEditText.paintFlags=nameInfoEditText.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
            decreaseInfoEditText.isEnabled = false
            decreaseInfoEditText.paintFlags=decreaseInfoEditText.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
            increaseInfoEditText.isEnabled = false
            increaseInfoEditText.paintFlags=increaseInfoEditText.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
            goalInfoEditText.isEnabled = false
            goalInfoEditText.paintFlags=goalInfoEditText.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
            updateFloatingButton.visibility = View.VISIBLE
            updateDoneFloatingButton.visibility = View.GONE
        }
    }
}