package com.rf.locationSource.ui.activity

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.rf.locationSource.R
import com.rf.locationSource.databinding.GoogleMapDemoActivityBinding
import com.rf.locationSource.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoogleMapDemoActivity : BaseActivity<GoogleMapDemoActivityBinding> (R.layout.google_map_demo_activity){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.google_map_demo_activity)

       val navController = Navigation.findNavController(this, R.id.navHostOnDashBoardFragment)
       // setupActionBar(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.googleMapLocationFragment -> {
                    viewDataBinding?.headerLayout?.toolbar?.title = "Location Source"
                    viewDataBinding?.headerLayout?.ivAppIcon?.setBackgroundResource(R.drawable.baseline_add_location_alt_24) // Hide right-side icon
                    viewDataBinding?.headerLayout?.ivSortLocation?.visibility = View.VISIBLE
                    viewDataBinding?.headerLayout?.ivAppIcon?.setOnClickListener {
                        finish()
                    }
                }
                R.id.searchMapLocationFragment -> {
                    viewDataBinding?.headerLayout?.toolbar?.title = "Search Place"
                    viewDataBinding?.headerLayout?.ivAppIcon?.setBackgroundResource(R.drawable.left_arrow)
                    viewDataBinding?.headerLayout?.ivSortLocation?.visibility = View.GONE
                    viewDataBinding?.headerLayout?.ivAppIcon?.setOnClickListener {
                        navController.popBackStack()
                        navController.navigate(R.id.googleMapLocationFragment)
                    }
                }
            }
        }
    }

    private fun setupActionBar(navController: NavController) {
       // setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.navHostOnDashBoardFragment).navigateUp() || super.onSupportNavigateUp()
    }
}