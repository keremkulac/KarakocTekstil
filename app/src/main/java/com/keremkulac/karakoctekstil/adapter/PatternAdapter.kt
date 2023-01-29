package com.keremkulac.karakoctekstil.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.karakoctekstil.R
import com.keremkulac.karakoctekstil.databinding.ItemPatternBinding
import com.keremkulac.karakoctekstil.model.Pattern
import com.keremkulac.karakoctekstil.view.PatternDetailFragment
import com.keremkulac.karakoctekstil.viewmodel.PatternViewModel
import kotlinx.android.synthetic.main.item_pattern.view.*
import kotlin.collections.ArrayList

class PatternAdapter(
    var clickListener : ClickListener,
    var patternList : ArrayList<Pattern>,
    var activity: FragmentActivity,
    var patternViewModel: PatternViewModel) : RecyclerView.Adapter<PatternAdapter.PatternViewHolder>() {
    private var lastPosition = -1
   // private var position : Int = 0
    class PatternViewHolder(val binding: ItemPatternBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pattern: Pattern){
            binding.pattern = pattern
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatternViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemPatternBinding>(inflater,R.layout.item_pattern,parent,false)
        return PatternViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatternViewHolder, position: Int) {
    //    this.position = position.toInt()
        holder.apply {
            bind(patternList[position])
        }

     //   holder.itemView.patternImageView.downloadFromUrl(patternList[position].pattern_url!!,placeHolderProgressBar(holder.itemView.context))
        setAnimation(holder.itemView,position,holder.itemView.context)
        holder.itemView.setOnClickListener {
            clickListener.ClickedItem(patternList[position])
        }
        holder.itemView.patternMenu.setOnClickListener {
            popupMenu(holder.binding.root.context,it,position)
        }


    }
    private fun popupMenu(context: Context,view : View,position : Int){
        val popupMenu = PopupMenu(context,view)
        popupMenu.inflate(R.menu.pattern_menu)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.patternDelete->{
                    patternViewModel.deletePattern(activity,patternList[position].name.toString(),patternList[position].pattern_url.toString(),context)
                    true
                }
                R.id.patternEdit->{
                    val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
                    val args = Bundle()
                    val patternDetailFragment = PatternDetailFragment()
                    args.putParcelable("pattern",patternList[position])
                    patternDetailFragment.setArguments(args)
                    fragmentTransaction.replace(R.id.mainFrameLayout,patternDetailFragment)
                    fragmentTransaction.commit()
                    true
                }
                else-> true
            }
        }
        popupMenu.show()
        val popup = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu = popup.get(popupMenu)
        menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
            .invoke(menu,true)
    }

    override fun getItemCount(): Int {
        return patternList.size
    }

    fun filterList(filterlist: ArrayList<Pattern>) {
        patternList = filterlist
        notifyDataSetChanged()
    }


    fun updatePatternList(newPatternList : List<Pattern>){
        patternList.clear()
        patternList.addAll(newPatternList)
        notifyDataSetChanged()
    }


    private fun setAnimation(viewToAnimate : View,position: Int,context : Context){
        if(position > lastPosition){
            val slideIn = AnimationUtils.loadAnimation(context,android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(slideIn)
            lastPosition = position
        }
    }

    interface ClickListener{
        fun ClickedItem(pattern: Pattern)
    }
}