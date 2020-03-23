package com.example.android.ecotown.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.android.ecotown.R
import com.example.android.ecotown.databinding.FragmentTrackBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class TrackFragment : Fragment() {
    private lateinit var bindingTrack: FragmentTrackBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingTrack = FragmentTrackBinding.inflate(inflater, container, false)
        return bindingTrack.root
    }

    override fun onStart() {
        super.onStart()
        createPieChart()
    }

    private fun createPieChart() {
        val pieChart: PieChart = bindingTrack.pieChart
        pieChart.description.isEnabled = false

        pieChart.dragDecelerationFrictionCoef = 0.8f

        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)
        pieChart.transparentCircleRadius = 50f
        pieChart.centerText = "Good Job"                    //изменять
        pieChart.setCenterTextColor(R.color.colorText)
        pieChart.setCenterTextSize(50f)


        val pieData = mutableListOf<PieEntry>()
        pieData.add(PieEntry(25f))
        pieData.add(PieEntry(75f))


        val dataSet = PieDataSet(pieData, "Hello")   //ДАННЫЕ ДЛЯ РАСПРЕДЕЛЕНИЯ
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        dataSet.colors = mutableListOf(Color.parseColor("#8aa632"), Color.parseColor("#7ac8c6"))

        pieChart.data = PieData(dataSet)
        pieChart.data.setValueTextColor(Color.parseColor("#533D2B"))
        pieChart.data.setValueTextSize(0.1f)



        pieChart.animateXY(2000, 2000)
    }
}
