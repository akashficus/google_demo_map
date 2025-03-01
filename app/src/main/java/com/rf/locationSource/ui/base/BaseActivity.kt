package com.rf.locationSource.ui.base

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rf.locationSource.R
import com.rf.locationSource.localDB.GoogleMapDemoDB
import com.rf.locationSource.utils.Constant

/**
 * Akash.Singh
 * RootFicus.
 */
abstract class BaseActivity<VB : ViewDataBinding>(@LayoutRes val layoutRes: Int) :
    AppCompatActivity() {
    protected var viewDataBinding: VB? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       /* AppConfig(this).setLocale(
            SharedPreference(this).getModemSupportCountryCode(),
            SharedPreference(this).getSelectedLanguage()
        )*/
        viewDataBinding = DataBindingUtil.setContentView(this, layoutRes)
    }

    fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    fun <T> moveToNextScreenWithFinishActivity(nextMoveClass: Class<T>) {
        val intent = Intent(applicationContext, nextMoveClass)
        startActivity(intent)
        finish()
    }

    fun moveToNextScreen(nextMoveClass: Class<AppCompatActivity>) {
        val intent = Intent(applicationContext, nextMoveClass)
        startActivity(intent)
    }

    open fun getApp(): GoogleMapDemoDB? {
        return this.application as GoogleMapDemoDB?
    }

    fun sessionExpired() {
        val mBuilder = AlertDialog.Builder(this@BaseActivity).setTitle(R.string.key_session_expired)
            .setMessage(R.string.key_you_session_has_expired)
            .setPositiveButton(R.string.key_ok, null).show()
        val mPositiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE)
        mPositiveButton.setOnClickListener {
            //startActivity(Intent(this, SignInActivity::class.java))
            //this.finishAffinity()
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