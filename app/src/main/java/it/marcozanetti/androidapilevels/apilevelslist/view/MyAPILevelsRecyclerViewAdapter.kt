package it.marcozanetti.androidapilevels.apilevelslist.view

import android.opengl.Visibility
import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import it.marcozanetti.androidapilevels.R
import it.marcozanetti.androidapilevels.apilevelslist.model.SingleAPILevel

import it.marcozanetti.androidapilevels.databinding.FragmentItemBinding

/**
 * [RecyclerView.Adapter] that can display a [SingleAPILevel].
 *
 * Inclues the ViewHolder class
 */
class MyAPILevelsRecyclerViewAdapter(
    private val values: List<SingleAPILevel>
) : RecyclerView.Adapter<MyAPILevelsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.versionNumber
        holder.version_name.text = item.codeName
        holder.furtherContent.text = item.releaseDate
        holder.apiVersion.text = item.getApiText()

        // Set the logo image
        if (item.logoResourceId != 0) {
            holder.versionLogo.setImageResource(item.logoResourceId)
            holder.versionLogo.visibility = View.VISIBLE
        } else {
            holder.versionLogo.visibility = View.GONE
        }

        if(position % 2 == 0) {
            //Alternate color for item rows
            holder.itemView.setBackgroundResource(R.color.another_row_background)
        }
        else {
            holder.itemView.setBackgroundResource(R.color.one_row_background)
        }

        if(item.releaseDate.isEmpty()) {
            holder.furtherContent.visibility = View.GONE
        } else {
            holder.furtherContent.visibility = View.VISIBLE
        }

        //If the device's API level is included in the levels of the
        //Android version we're drawing, we change the background color
        if(Build.VERSION.SDK_INT >= item.apiLevelStart
            && Build.VERSION.SDK_INT <= item.apiLevelEnd) {
            holder.itemView.setBackgroundResource(R.color.emphasize)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.versionNumber
        val version_name: TextView = binding.versionName
        val furtherContent: TextView = binding.furtherContent
        val apiVersion: TextView = binding.apiVersion
        val versionLogo: ImageView = binding.versionLogo

        override fun toString(): String {
            return super.toString() + " '" + furtherContent.text + "'"
        }
    }

}