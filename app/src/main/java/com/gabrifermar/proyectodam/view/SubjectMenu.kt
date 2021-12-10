package com.gabrifermar.proyectodam.view

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.gabrifermar.proyectodam.R
import com.gabrifermar.proyectodam.databinding.ActivitySubjectMenuBinding
import com.gabrifermar.proyectodam.viewmodel.SubjectMenuViewModel
import kotlinx.android.synthetic.main.fragment_usermain.*

class SubjectMenu : AppCompatActivity() {

    private lateinit var binding: ActivitySubjectMenuBinding
    private lateinit var viewModel: SubjectMenuViewModel
    private lateinit var airlawpb: ObjectAnimator
    private lateinit var airframepb: ObjectAnimator
    private lateinit var commspb: ObjectAnimator
    private lateinit var humanpb: ObjectAnimator
    private lateinit var instrumentationpb: ObjectAnimator
    private lateinit var massbalancepb: ObjectAnimator
    private lateinit var meteopb: ObjectAnimator
    private lateinit var navpb: ObjectAnimator
    private lateinit var performancepb: ObjectAnimator
    private lateinit var planningpb: ObjectAnimator
    private lateinit var poppb: ObjectAnimator
    private lateinit var principlespb: ObjectAnimator
    private lateinit var radiopb: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //variable
        viewModel = ViewModelProvider(this)[SubjectMenuViewModel::class.java]
        airlawpb = ObjectAnimator.ofInt(binding.subjectmenuPbAirLaw, "progress", 0)
        airframepb = ObjectAnimator.ofInt(binding.subjectmenuPbAirframe, "progress", 0)
        commspb = ObjectAnimator.ofInt(binding.subjectmenuPbComms, "progress", 0)
        humanpb = ObjectAnimator.ofInt(binding.subjectmenuPbHuman, "progress", 0)
        instrumentationpb =
            ObjectAnimator.ofInt(binding.subjectmenuPbInstrumentation, "progress", 0)
        massbalancepb = ObjectAnimator.ofInt(binding.subjectmenuPbMassBalance, "progress", 0)
        meteopb = ObjectAnimator.ofInt(binding.subjectmenuPbMeteo, "progress", 0)
        navpb = ObjectAnimator.ofInt(binding.subjectmenuPbNav, "progress", 0)
        performancepb = ObjectAnimator.ofInt(binding.subjectmenuPbPerformance, "progress", 0)
        planningpb = ObjectAnimator.ofInt(binding.subjectmenuPbPlanning, "progress", 0)
        poppb = ObjectAnimator.ofInt(binding.subjectmenuPbPop, "progress", 0)
        principlespb = ObjectAnimator.ofInt(binding.subjectmenuPbPrinciples, "progress", 0)
        radiopb = ObjectAnimator.ofInt(binding.subjectmenuPbRadio, "progress", 0)


        //backarrow
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //title
        supportActionBar!!.title = getString(R.string.subjects)

        //listeners
        startListeners()

        //setup viewmodel
        setupViewModel()

    }

    private fun setupViewModel() {

        //Air Law
        viewModel.airlawProgress()
        airlawProgress()

        //Airframe
        viewModel.airframeProgress()
        airframeProgress()

        //Communications
        viewModel.commsProgress()
        commsProgress()

        //Human performance
        viewModel.humanProgress()
        humanProgress()

        //Instrumentation
        viewModel.instrumentationProgress()
        instrumentationProgress()

        //Mass and Balance
        viewModel.massbalanceProgress()
        massbalanceProgress()

        //Meteorology
        viewModel.meteoProgress()
        meteoProgress()

        //General Navigation
        viewModel.navProgress()
        navProgress()

        //Performance
        viewModel.performanceProgress()
        performanceProgress()

        //Flight Planning
        viewModel.planningProgress()
        planningProgress()

        //Operational Procedures
        viewModel.popProgress()
        popProgress()

        //Flight Principles
        viewModel.principlesProgress()
        principlesProgress()

        //Radio Navigation
        viewModel.radioProgress()
        radioProgress()
    }

    private fun radioProgress() {
        viewModel.radioProgress.observe(this, {
            radiopb = ObjectAnimator.ofInt(binding.subjectmenuPbRadio, "progress", it)
                .apply {
                    duration = 1500
                    start()
                    addUpdateListener { updatedAnimation ->
                        binding.subjectmenuTxtRadioProgress.text = this@SubjectMenu.getString(
                            R.string.progress,
                            updatedAnimation.animatedValue.toString()
                        )
                    }
                }
        })
    }

    private fun principlesProgress() {
        viewModel.principlesProgress.observe(this, {
            principlespb = ObjectAnimator.ofInt(binding.subjectmenuPbPrinciples, "progress", it)
                .apply {
                    duration = 1500
                    start()
                    addUpdateListener { updatedAnimation ->
                        binding.subjectmenuTxtPrinciplesProgress.text = this@SubjectMenu.getString(
                            R.string.progress,
                            updatedAnimation.animatedValue.toString()
                        )
                    }
                }
        })
    }

    private fun popProgress() {
        viewModel.popProgress.observe(this, {
            poppb = ObjectAnimator.ofInt(binding.subjectmenuPbPop, "progress", it)
                .apply {
                    duration = 1500
                    start()
                    addUpdateListener { updatedAnimation ->
                        binding.subjectmenuTxtPopProgress.text = this@SubjectMenu.getString(
                            R.string.progress,
                            updatedAnimation.animatedValue.toString()
                        )
                    }
                }
        })
    }

    private fun planningProgress() {
        viewModel.planningProgress.observe(this, {
            planningpb = ObjectAnimator.ofInt(binding.subjectmenuPbPlanning, "progress", it)
                .apply {
                    duration = 1500
                    start()
                    addUpdateListener { updatedAnimation ->
                        binding.subjectmenuTxtPlanningProgress.text = this@SubjectMenu.getString(
                            R.string.progress,
                            updatedAnimation.animatedValue.toString()
                        )
                    }
                }
        })
    }

    private fun performanceProgress() {
        viewModel.performanceProgress.observe(this, {
            performancepb = ObjectAnimator.ofInt(binding.subjectmenuPbPerformance, "progress", it)
                .apply {
                    duration = 1500
                    start()
                    addUpdateListener { updatedAnimation ->
                        binding.subjectmenuTxtPerformanceProgress.text = this@SubjectMenu.getString(
                            R.string.progress,
                            updatedAnimation.animatedValue.toString()
                        )
                    }
                }
        })
    }

    private fun navProgress() {
        viewModel.navProgress.observe(this, {
            navpb = ObjectAnimator.ofInt(binding.subjectmenuPbNav, "progress", it)
                .apply {
                    duration = 1500
                    start()
                    addUpdateListener { updatedAnimation ->
                        binding.subjectmenuTxtNavProgress.text = this@SubjectMenu.getString(
                            R.string.progress,
                            updatedAnimation.animatedValue.toString()
                        )
                    }
                }
        })
    }

    private fun meteoProgress() {
        viewModel.meteoProgress.observe(this, {
            meteopb = ObjectAnimator.ofInt(binding.subjectmenuPbMeteo, "progress", it)
                .apply {
                    duration = 1500
                    start()
                    addUpdateListener { updatedAnimation ->
                        binding.subjectmenuTxtMeteoProgress.text = this@SubjectMenu.getString(
                            R.string.progress,
                            updatedAnimation.animatedValue.toString()
                        )
                    }
                }
        })
    }

    private fun massbalanceProgress() {
        viewModel.massbalanceProgress.observe(this, {
            massbalancepb = ObjectAnimator.ofInt(binding.subjectmenuPbMassBalance, "progress", it)
                .apply {
                    duration = 1500
                    start()
                    addUpdateListener { updatedAnimation ->
                        binding.subjectmenuTxtMassBalanceProgress.text =
                            this@SubjectMenu.getString(
                                R.string.progress,
                                updatedAnimation.animatedValue.toString()
                            )
                    }
                }
        })
    }

    private fun instrumentationProgress() {
        viewModel.instrumentationProgress.observe(this, {
            instrumentationpb =
                ObjectAnimator.ofInt(binding.subjectmenuPbInstrumentation, "progress", it)
                    .apply {
                        duration = 1500
                        start()
                        addUpdateListener { updatedAnimation ->
                            binding.subjectmenuTxtInstrumentationProgress.text =
                                this@SubjectMenu.getString(
                                    R.string.progress,
                                    updatedAnimation.animatedValue.toString()
                                )
                        }
                    }
        })
    }

    private fun humanProgress() {
        viewModel.humanProgress.observe(this, {
            humanpb = ObjectAnimator.ofInt(binding.subjectmenuPbHuman, "progress", it)
                .apply {
                    duration = 1500
                    start()
                    addUpdateListener { updatedAnimation ->
                        binding.subjectmenuTxtHumanProgress.text = this@SubjectMenu.getString(
                            R.string.progress,
                            updatedAnimation.animatedValue.toString()
                        )
                    }
                }
        })
    }

    private fun commsProgress() {
        viewModel.commsProgress.observe(this, {
            commspb = ObjectAnimator.ofInt(binding.subjectmenuPbComms, "progress", it)
                .apply {
                    duration = 1500
                    start()
                    addUpdateListener { updatedAnimation ->
                        binding.subjectmenuTxtCommsProgress.text = this@SubjectMenu.getString(
                            R.string.progress,
                            updatedAnimation.animatedValue.toString()
                        )
                    }
                }
        })
    }

    private fun airframeProgress() {
        viewModel.airframeProgress.observe(this, {
            airframepb = ObjectAnimator.ofInt(binding.subjectmenuPbAirframe, "progress", it)
                .apply {
                    duration = 1500
                    start()
                    addUpdateListener { updatedAnimation ->
                        binding.subjectmenuTxtAirframeProgress.text = this@SubjectMenu.getString(
                            R.string.progress,
                            updatedAnimation.animatedValue.toString()
                        )
                    }
                }
        })
    }

    private fun airlawProgress() {
        viewModel.airlawProgress.observe(this, {
            airlawpb = ObjectAnimator.ofInt(binding.subjectmenuPbAirLaw, "progress", it)
                .apply {
                    duration = 1500
                    start()
                    addUpdateListener { updatedAnimation ->
                        binding.subjectmenuTxtAirLawProgress.text = this@SubjectMenu.getString(
                            R.string.progress,
                            updatedAnimation.animatedValue.toString()
                        )
                    }
                }
        })
    }

    private fun startListeners() {
        binding.subjectmenuCvAirLaw.setOnClickListener {
            startActivity(
                Intent(this, Subject::class.java)
                    .putExtra("ref", "airlaw")
                    .putExtra("title", "Air Law")
            )
        }

        binding.subjectmenuCvAirframe.setOnClickListener {
            startActivity(
                Intent(this, Subject::class.java)
                    .putExtra("ref", "airframe")
                    .putExtra("title", "Airframes and systems")
            )
        }

        binding.subjectmenuCvComms.setOnClickListener {
            Toast.makeText(this, R.string.wip, Toast.LENGTH_SHORT).show()
        }

        binding.subjectmenuCvHuman.setOnClickListener {
            Toast.makeText(this, R.string.wip, Toast.LENGTH_SHORT).show()
        }

        binding.subjectmenuCvInstrumentation.setOnClickListener {
            Toast.makeText(this, R.string.wip, Toast.LENGTH_SHORT).show()
        }

        binding.subjectmenuCvMassBalance.setOnClickListener {
            Toast.makeText(this, R.string.wip, Toast.LENGTH_SHORT).show()
        }

        binding.subjectmenuCvMeteo.setOnClickListener {
            Toast.makeText(this, R.string.wip, Toast.LENGTH_SHORT).show()
        }

        binding.subjectmenuCvNav.setOnClickListener {
            Toast.makeText(this, R.string.wip, Toast.LENGTH_SHORT).show()
        }

        binding.subjectmenuCvPerformance.setOnClickListener {
            Toast.makeText(this, R.string.wip, Toast.LENGTH_SHORT).show()
        }

        binding.subjectmenuCvPlanning.setOnClickListener {
            Toast.makeText(this, R.string.wip, Toast.LENGTH_SHORT).show()
        }

        binding.subjectmenuCvPop.setOnClickListener {
            Toast.makeText(this, R.string.wip, Toast.LENGTH_SHORT).show()
        }

        binding.subjectmenuCvPrinciples.setOnClickListener {
            Toast.makeText(this, R.string.wip, Toast.LENGTH_SHORT).show()
        }

        binding.subjectmenuCvRadio.setOnClickListener {
            Toast.makeText(this, R.string.wip, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onPause() {
        airlawpb.end()
        airframepb.end()
        commspb.end()
        humanpb.end()
        instrumentationpb.end()
        massbalancepb.end()
        meteopb.end()
        navpb.end()
        performancepb.end()
        planningpb.end()
        poppb.end()
        principlespb.end()
        radiopb.end()
        super.onPause()
    }

    override fun onStop() {
        airlawpb.end()
        airframepb.end()
        commspb.end()
        humanpb.end()
        instrumentationpb.end()
        massbalancepb.end()
        meteopb.end()
        navpb.end()
        performancepb.end()
        planningpb.end()
        poppb.end()
        principlespb.end()
        radiopb.end()
        super.onStop()
    }
}