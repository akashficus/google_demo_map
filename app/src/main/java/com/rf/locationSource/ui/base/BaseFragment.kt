package com.rf.locationSource.ui.base

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.rf.locationSource.R
import com.rf.locationSource.sdkInit.GoogleMapDemoSDK
import com.rf.locationSource.utils.Constant

/**
 * Akash.Singh
 * RootFicus.
 */
abstract class BaseFragment<DB : ViewDataBinding>(@LayoutRes val layoutRes: Int) : Fragment() {

    private lateinit var mActivity: AppCompatActivity

    lateinit var mDataBinding: DB

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as AppCompatActivity).let {
            this.mActivity = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        mDataBinding.root.isClickable = true
        return mDataBinding.root
    }


    fun removeFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = mActivity.supportFragmentManager
        val fragmentTransaction: FragmentTransaction =
            fragmentManager.beginTransaction()
        fragment?.let { fragmentTransaction.remove(it) }
        fragmentTransaction.commit()
    }

    open fun getApp(): GoogleMapDemoSDK? {
        return mActivity.application as GoogleMapDemoSDK?
    }

    fun showMessage(message: String){
        Toast.makeText(mActivity,message, Toast.LENGTH_SHORT).show()
    }

    fun showAlertMessage(title: String,message: String){
        val mBuilder = AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.key_ok, null)
            .show()
        val mPositiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE)
        mPositiveButton.setOnClickListener {
            mBuilder.dismiss()
        }
    }

    fun sessionExpired() {
        val mBuilder = AlertDialog.Builder(activity)
            .setTitle(R.string.key_session_expired)
            .setMessage(R.string.key_you_session_has_expired)
            .setPositiveButton(R.string.key_ok, null)
            .show()
        val mPositiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE)
        mPositiveButton.setOnClickListener {
           // startActivity(Intent(activity, SignInActivity::class.java))
            activity?.finishAffinity()
        }
    }

    fun showErrorMessage(message: String?) {
        if (message == Constant.KEY_INVALID_ACCESS_TOKEN) {
            //Show Toast
            showMessage(
                getString(R.string.key_invalid_access_token)
            )
            sessionExpired()
        } else {
            showMessage(message.toString())
        }
    }
}