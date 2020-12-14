package it.marcozanetti.androidapilevels

import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import it.marcozanetti.androidapilevels.placeholder.SdkVersionContent.SingleSDKversion
import it.marcozanetti.androidapilevels.databinding.FragmentItemBinding

/**
 * [RecyclerView.Adapter] that can display a [SingleSDKversion].
 */
class MysdkVersionsRecyclerViewAdapter(
    private val values: List<SingleSDKversion>
) : RecyclerView.Adapter<MysdkVersionsRecyclerViewAdapter.ViewHolder>() {

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

        if(position % 2 == 0) {
            //Alternate color for item rows
            holder.itemView.setBackgroundResource(R.color.soft_yellow_a_bit_darker)
        }

        //If the device's API level is included in the levels of the
        //Android version we're drawing, we change the background color
        if(Build.VERSION.SDK_INT >= item.apiLevelStart
            && Build.VERSION.SDK_INT <= item.apiLevelEnd) {
            holder.itemView.setBackgroundResource(R.color.reddish)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.versionNumber
        val version_name: TextView = binding.versionName
        val furtherContent: TextView = binding.furtherContent
        val apiVersion: TextView = binding.apiVersion

        override fun toString(): String {
            return super.toString() + " '" + furtherContent.text + "'"
        }
    }

}