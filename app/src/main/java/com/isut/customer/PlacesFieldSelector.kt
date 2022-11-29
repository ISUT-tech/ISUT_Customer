package com.isut.customer

import android.R
import android.content.Context
import android.view.View
import kotlin.jvm.JvmOverloads
import android.widget.ArrayAdapter
import android.widget.AdapterView.OnItemClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckedTextView
import com.google.android.libraries.places.api.model.Place
import java.lang.StringBuilder
import java.util.*

class PlacesFieldSelector @JvmOverloads constructor(validFields: List<Place.Field> = Arrays.asList(*Place.Field.values())) {
    private val placeFields: MutableList<PlaceField>

    /**
     * Returns all [Place.Field] that are selectable.
     */
    val allFields: List<Place.Field>
        get() {
            val list: MutableList<Place.Field> = ArrayList()
            for (placeField in placeFields) {
                list.add(placeField.field)
            }
            return list
        }

    /**
     * Returns all [Place.Field] values the user selected.
     */
    val selectedFields: List<Place.Field>
        get() {
            val selectedList: MutableList<Place.Field> = ArrayList()
            for (placeField in placeFields) {
                if (placeField.checked) {
                    selectedList.add(placeField.field)
                }
            }
            return selectedList
        }

    /**
     * Returns a String representation of all selected [Place.Field] values. See [ ][.getSelectedFields].
     */
    val selectedString: String
        get() {
            val builder = StringBuilder()
            for (field in selectedFields) {
                builder.append(field).append("\n")
            }
            return builder.toString()
        }

    //////////////////////////
    // Helper methods below //
    //////////////////////////
    private class PlaceField(val field: Place.Field) {
        var checked = false
    }

    private class PlaceFieldArrayAdapter(context: Context?, placeFields: List<PlaceField?>?) :
        ArrayAdapter<PlaceField?>(
            context!!, R.layout.simple_list_item_multiple_choice, placeFields!!
        ), OnItemClickListener {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent)
            val placeField = getItem(position)
            updateView(view, placeField)
            return view
        }

        override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
            val placeField = getItem(position)
            placeField!!.checked = !placeField.checked
            updateView(view, placeField)
        }

        private fun updateView(view: View, placeField: PlaceField?) {
            if (view is CheckedTextView) {
                val checkedTextView = view
                checkedTextView.text = placeField!!.field.toString()
                checkedTextView.isChecked = placeField.checked
            }
        }
    }

    init {
        placeFields = ArrayList()
        for (field in validFields) {
            placeFields.add(PlaceField(field))
        }
    }
}