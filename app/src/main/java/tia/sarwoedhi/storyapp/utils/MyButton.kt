package tia.sarwoedhi.storyapp.utils

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import tia.sarwoedhi.storyapp.R

class MyButton : AppCompatButton {
    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        txtColor =
            ContextCompat.getColor(context, if (isEnabled) R.color.colorPrimary else R.color.white)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setTextColor(txtColor)
        alpha = if (!isEnabled) 0.5f else 1.0f
        textSize = 14f
        gravity = Gravity.CENTER
    }

}