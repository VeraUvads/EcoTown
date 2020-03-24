package com.example.android.ecotown.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity

import com.example.android.ecotown.R
import com.example.android.ecotown.activities.CheckListActivity
import com.example.android.ecotown.databinding.FragmentTrackBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener


class TrackFragment : Fragment(), OnChartGestureListener {
    private lateinit var bindingTrack: FragmentTrackBinding
    private lateinit var contextTrack: FragmentActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingTrack = FragmentTrackBinding.inflate(inflater, container, false)
        contextTrack = this.activity!!
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
        pieChart.centerText = "Long pressed to open Check List"                    //изменять
        pieChart.setCenterTextColor(R.color.colorText)
        pieChart.setCenterTextSize(20f)


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

        pieChart.onChartGestureListener = this


    }


    override fun onChartGestureEnd(p0: MotionEvent?, p1: ChartTouchListener.ChartGesture?) {
        Log.i("Gesture", "END")
    }

    override fun onChartFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float) {
    }

    override fun onChartSingleTapped(p0: MotionEvent?) {
    }

    override fun onChartGestureStart(p0: MotionEvent?, p1: ChartTouchListener.ChartGesture?) {
        if (p1 == ChartTouchListener.ChartGesture.LONG_PRESS) {
            onChartLongPressed(p0)
        }
    }

    override fun onChartScale(p0: MotionEvent?, p1: Float, p2: Float) {
    }

    override fun onChartLongPressed(p0: MotionEvent?) {
        startActivity(Intent(activity, CheckListActivity::class.java))
    }

    override fun onChartDoubleTapped(p0: MotionEvent?) {
    }

    override fun onChartTranslate(p0: MotionEvent?, p1: Float, p2: Float) {
    }
}
