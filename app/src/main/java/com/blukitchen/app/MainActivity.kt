package com.blukitchen.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(32, 32, 32, 32)
        layout.setBackgroundColor(Color.rgb(245, 247, 250))

        val title = TextView(this)
        title.text = "BLU KITCHEN"
        title.textSize = 30f
        title.setTextColor(Color.rgb(20, 45, 80))
        title.gravity = Gravity.CENTER

        val subtitle = TextView(this)
        subtitle.text = "فروشگاه تخصصی لوازم آشپزخانه"
        subtitle.textSize = 18f
        subtitle.gravity = Gravity.CENTER
        subtitle.setPadding(0, 24, 0, 24)

        val welcome = TextView(this)
        welcome.text = "به دنیای آشپزی خوش آمدید!"
        welcome.textSize = 22f
        welcome.gravity = Gravity.CENTER
        welcome.setPadding(0, 30, 0, 30)

        val button = Button(this)
        button.text = "مشاهده محصولات"

        button.setOnClickListener {
            welcome.text = "به‌زودی محصولات Blu Kitchen اضافه می‌شوند"
        }

        layout.addView(title)
        layout.addView(subtitle)
        layout.addView(welcome)
        layout.addView(button)

        setContentView(layout)
    }
}
