package com.music.player.common

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.music.player.R
import java.util.Locale
import java.util.concurrent.TimeUnit

// Write all common functions here
object CommonUtil {

    /* Function For Show Message As An Alert Dialog */
    fun showAlert(message: String, context: Context?) {
    }

    fun durationConvertorToMMss(duration: Long): String {
        return String.format(
            Locale.getDefault(),
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(duration) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(duration) % TimeUnit.MINUTES.toSeconds(1)
        )
    }

    fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun setOnGlideImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url).placeholder(
            ContextCompat.getDrawable(context, R.drawable.placeholder)
        ).into(imageView)
    }

    fun scaleOriginal(view: View) {
        val scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1f)
        val scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1f)
        scaleUpX.duration = 200
        scaleUpY.duration = 200
        val scaleUp = AnimatorSet()
        scaleUp.play(scaleUpX).with(scaleUpY)
        scaleUp.start()
    }

    fun scaleDown(view: View) {
        val scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 0.9f)
        val scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 0.9f)
        scaleDownX.duration = 200
        scaleDownY.duration = 200
        val scaleDown = AnimatorSet()
        scaleDown.play(scaleDownX).with(scaleDownY)
        scaleDown.start()
    }
}