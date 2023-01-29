package com.keremkulac.karakoctekstil.util

import android.app.ProgressDialog
import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.keremkulac.karakoctekstil.R


//Extension
fun ImageView.downloadFromUrl(url: String, progressDrawable: CircularProgressDrawable){
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher_round)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)

}

fun placeHolderProgressBar(context : Context) : CircularProgressDrawable{

    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}
fun replaceFragment(fragment: Fragment, fragmentManager: FragmentManager){
    val fragmentTransaction = fragmentManager.beginTransaction()
    fragmentTransaction.replace(R.id.mainFrameLayout,fragment)
    fragmentTransaction.commit()
}


fun customProgressDialog(context: Context,message: String) : ProgressDialog{
    val progressDialog = ProgressDialog(context)
    progressDialog.setMessage(message)
    progressDialog.setCancelable(false)
    progressDialog.show()
    return progressDialog
}
@BindingAdapter("android:downloadUrl")
fun downLoadImage(view : ImageView,url: String){
    view.downloadFromUrl(url, placeHolderProgressBar(view.context))
}





