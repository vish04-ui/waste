package com.example.wastemanagement.ui.activities

import android.content.Intent
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import android.widget.TextView
import com.example.wastemanagement.R
import com.example.wastemanagement.ui.auth.AuthManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class OnboardingActivity : AppCompatActivity() {
    private val scope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        val authManager = AuthManager(this)
        val pager = findViewById<ViewPager2>(R.id.onboardingPager)
    val skip = findViewById<MaterialButton>(R.id.buttonSkip)
    val next = findViewById<MaterialButton>(R.id.buttonNext)
        val indicator = findViewById<TextView>(R.id.pageIndicator)
        pager.adapter = SimpleTextPagerAdapter(
            this,
            listOf(
                Pair(getString(R.string.onboarding_title_1), getString(R.string.onboarding_desc_1)),
                Pair(getString(R.string.onboarding_title_2), getString(R.string.onboarding_desc_2)),
                Pair(getString(R.string.onboarding_title_3), getString(R.string.onboarding_desc_3))
            )
        ).apply { useRichLayout = true }
        fun update() {
            indicator.text = "${pager.currentItem + 1}/${pager.adapter?.itemCount ?: 0}"
            next.text = if (pager.currentItem == (pager.adapter!!.itemCount - 1)) getString(R.string.finish) else getString(R.string.next)
        }
        pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){override fun onPageSelected(position:Int){update()}})
        update()
        skip.setOnClickListener { complete(authManager) }
        next.setOnClickListener {
            if (pager.currentItem < (pager.adapter!!.itemCount - 1)) pager.currentItem += 1 else complete(authManager)
        }
    }
    private fun complete(authManager: AuthManager){
        scope.launch { authManager.setOnboardingCompleted(true); startActivity(Intent(this@OnboardingActivity, LoginActivity::class.java)); finish() }
    }
}

