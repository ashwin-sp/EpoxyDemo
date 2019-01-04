package com.example.epoxydemo

import android.R.attr.button
import com.airbnb.epoxy.EpoxyHolder
import android.databinding.adapters.TextViewBindingAdapter.setText
import com.airbnb.epoxy.EpoxyAttribute
import android.support.annotation.StringRes
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.EpoxyModelClass

import com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash

@EpoxyModelClass(layout = R.layout.test_holder)
abstract class ContentViewHolder : EpoxyModelWithHolder<ContentViewHolder.Holder>() {

    // Declare your model properties like this
    @EpoxyAttribute
    @StringRes
    var text: String = ""
    @EpoxyAttribute(DoNotHash)
    var listener: View.OnClickListener? = null

    override fun bind(holder: Holder) {
        // Implement this to bind the properties to the view
        holder.textView.text = text
        holder.textView.setOnClickListener(listener)
    }

    class Holder : EpoxyHolder() {
        lateinit var textView: TextView

        override fun bindView(itemView: View) {
            textView = itemView.findViewById(R.id.text_view)
        }
    }
}