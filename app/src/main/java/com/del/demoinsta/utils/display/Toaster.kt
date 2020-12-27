package com.del.demoinsta.utils.display

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.del.demoinsta.R

/**
 *
 * An customized Toast class for showing toast
 * Bg color changed
 *
 * </> We can also use Snackbar.
 */

object Toaster {
    fun show(context: Context, text: CharSequence) {
        val toast = android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_SHORT)

        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            toast.view?.background?.colorFilter = BlendModeColorFilter(
                ContextCompat.getColor(context, R.color.white),
                BlendMode.SRC_IN
            )
        } else {
            toast.view?.background?.setColorFilter(
                ContextCompat.getColor(context, R.color.white),
                PorterDuff.Mode.SRC_IN
            )
        }

        //
        val textView = toast.view?.findViewById(android.R.id.message) as TextView
        textView.setTextColor(ContextCompat.getColor(context, R.color.black))
        toast.show()
    }
}