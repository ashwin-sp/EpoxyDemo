package com.example.epoxydemo

import android.content.Context
import android.widget.Toast
import com.airbnb.epoxy.Typed3EpoxyController


class ContentController : Typed3EpoxyController<List<String>, Boolean, Context>() {

    override fun buildModels(content: List<String>, isTitle: Boolean, context: Context) {
        content.forEach {

            ContentViewHolder_().id(it.toLong()).text(it).listener { _ ->
                run {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }.addTo(this)
        }
    }
}

