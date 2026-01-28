package it.marcozanetti.androidapilevels.apilevelslist.view

import android.annotation.SuppressLint
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
    private val values: List<SingleAPILevel>,
    private val onItemClick: (SingleAPILevel) -> Unit
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

    @SuppressLint("WrongConstant", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.versionNumber
        holder.version_name.text = item.codeName
        holder.furtherContent.text = item.releaseDate
        holder.apiVersion.text = item.getApiText()
// Change these lines in onBindViewHolder:
        holder.apiName.text = if (getApiNameForLevel(item.apiLevelStart.toFloat()) != "?") {
            getApiNameForLevel(item.apiLevelStart.toFloat())
        } else {
            "No API name"
        }

        // Set the logo image
        if (item.logoResourceId != 0) {
            holder.versionLogo.setImageResource(item.logoResourceId)
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

        if(Build.VERSION.SDK_INT >= 36) {
            // From Android 16 api naming changes

            // Use BigDecimal for precision
            val startApiLevel = item.apiLevelStart.toBigDecimal()
            val endApiLevel = item.apiLevelEnd.toBigDecimal()

            val startMajorVersion = startApiLevel.setScale(0, java.math.RoundingMode.DOWN).toInt()
            val startMinorVersion = startApiLevel.subtract(startMajorVersion.toBigDecimal())
            val startMinorVersionAsInt = (startMinorVersion.multiply(10.toBigDecimal())).toInt()

            val endMajorVersion = endApiLevel.setScale(0, java.math.RoundingMode.DOWN).toInt()
            val endMinorVersion = endApiLevel.subtract(endMajorVersion.toBigDecimal())
            val endMinorVersionAsInt = (endMinorVersion.multiply(10.toBigDecimal())).toInt()

            val deviceApiLevel = Build.VERSION.SDK_INT_FULL
            val deviceApiLevelInt = deviceApiLevel.toInt()
            val deviceMajorVersion = Build.getMajorSdkVersion(deviceApiLevelInt)
            val deviceMinorVersion = Build.getMinorSdkVersion(deviceApiLevelInt)

            if(deviceMajorVersion == startMajorVersion && deviceMinorVersion >= startMinorVersionAsInt
                && deviceMajorVersion == endMajorVersion && deviceMinorVersion <= endMinorVersionAsInt) {
                holder.itemView.setBackgroundResource(R.color.emphasize)
            }
        }
        else {
            //In previous Android versions

            //If the device's API level is included in the levels of the
            //Android version we're drawing, we change the background color
            if(Build.VERSION.SDK_INT >= item.apiLevelStart
                && Build.VERSION.SDK_INT <= item.apiLevelEnd) {
                holder.itemView.setBackgroundResource(R.color.emphasize)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = values.size

    class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.versionNumber
        val version_name: TextView = binding.versionName
        val furtherContent: TextView = binding.furtherContent
        val apiVersion: TextView = binding.apiVersion
        val versionLogo: ImageView = binding.versionLogo
        val apiName: TextView = binding.apiName

        override fun toString(): String {
            return super.toString() + " '" + furtherContent.text + "'"
        }
    }

    companion object {
        // Mappa statica dei nomi delle API fino a 36 (aggiornata dai sorgenti AOSP)
        val apiLevelNameMap = mapOf(
            1 to "BASE",
            2 to "BASE_1_1",
            3 to "CUPCAKE",
            4 to "DONUT",
            5 to "ECLAIR",
            6 to "ECLAIR_0_1",
            7 to "ECLAIR_MR1",
            8 to "FROYO",
            9 to "GINGERBREAD",
            10 to "GINGERBREAD_MR1",
            11 to "HONEYCOMB",
            12 to "HONEYCOMB_MR1",
            13 to "HONEYCOMB_MR2",
            14 to "ICE_CREAM_SANDWICH",
            15 to "ICE_CREAM_SANDWICH_MR1",
            16 to "JELLY_BEAN",
            17 to "JELLY_BEAN_MR1",
            18 to "JELLY_BEAN_MR2",
            19 to "KITKAT",
            20 to "KITKAT_WATCH",
            21 to "LOLLIPOP",
            22 to "LOLLIPOP_MR1",
            23 to "MARSHMALLOW",
            24 to "NOUGAT",
            25 to "NOUGAT_MR1",
            26 to "OREO",
            27 to "OREO_MR1",
            28 to "PIE",
            29 to "Q",
            30 to "R",
            31 to "S",
            32 to "S_V2",
            33 to "TIRAMISU",
            34 to "UPSIDE_DOWN_CAKE",
            35 to "VANILLA_ICE_CREAM",
            36 to "WALNUT_CREAM"
        )
    }

    private fun getApiNameForLevel(apiLevel: Float): String {
        val apiLevelInt = apiLevel.toInt()
        apiLevelNameMap[apiLevelInt]?.let { return it }
        val fields = Build.VERSION_CODES::class.java.fields
        for (field in fields) {
            try {
                if (field.type == Int::class.javaPrimitiveType && field.getInt(null) == apiLevelInt) {
                    return field.name
                }
            } catch (_: Exception) {}
        }
        return "?"
    }

}