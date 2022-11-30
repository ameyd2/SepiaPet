package com.amey.sepiapets.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.amey.sepiapets.R
import com.amey.sepiapets.models.Pet
import com.amey.sepiapets.models.PetsModel
import com.bumptech.glide.Glide

class PetsListRvAdapter(
    private val petsList: ArrayList<Pet>,
    private val mainViewModel: MainViewModel
) :
    RecyclerView.Adapter<PetsListRvAdapter.MyViewHolder?>() {
    private var context: Context? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context

        context = parent.context

        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.petslist_recyler_adapter_layout,
                parent,
                false
            )
        )

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val model: Pet = this.petsList[position]

        if (!model.image_url.isNullOrEmpty()){

            Glide.with(context?.applicationContext!!).load(model.image_url)
                .into(holder.bannerImage)
        }
        else{
            holder.bannerImage.visibility = View.GONE
        }

        if (!model.title.isNullOrEmpty()){

            holder.nameText.text = model.title
        }
        else{
            holder.nameText.text = "No Name"
        }

        holder.mainLayout.setOnClickListener {
            mainViewModel.sendContentUrl(model.content_url)
            val activity: AppCompatActivity = it.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction()
                .add(R.id.container, ContentFragment.newInstance()).addToBackStack("Main")
                .commit()
        }
    }


    override fun getItemCount(): Int = petsList.size


    class MyViewHolder(contentView: View) :
        RecyclerView.ViewHolder(contentView) {

        val bannerImage: ImageView = contentView.findViewById<View>(R.id.s_image) as ImageView
        val nameText: TextView = contentView.findViewById<View>(R.id.s_content_text) as TextView
        val mainLayout : LinearLayout = contentView.findViewById<View>(R.id.main_layout) as LinearLayout
    }

}
