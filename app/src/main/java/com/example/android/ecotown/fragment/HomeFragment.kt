package com.example.android.ecotown.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android.ecotown.R
import com.example.android.ecotown.databinding.FragmentHomeBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    lateinit var mAuth: FirebaseAuth

    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        binding.textView.text = currentUser?.displayName
        createPieChart()

        super.onStart()
    }

    private fun createPieChart() {
        val pieChart: PieChart = this.pieChart
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
