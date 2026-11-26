package com.blukitchen.app

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import android.text.Editable
import android.text.TextWatcher

class MainActivity : Activity() {

    data class Product(
        val id: Int,
        val name: String,
        val category: String,
        val price: Int,
        val oldPrice: Int,
        val emoji: String,
        val description: String
    )

    private val navy = Color.rgb(23, 35, 53)
    private val gold = Color.rgb(205, 160, 80)
    private val cream = Color.rgb(248, 246, 241)
    private val ink = Color.rgb(38, 43, 50)

    private val products = listOf(
        Product(1, "سرویس قابلمه گرانیتی", "پخت‌وپز", 2850000, 3200000, "🍲", "سرویس چندپارچه با پوشش نچسب"),
        Product(2, "ست چاقوی آشپزخانه", "ابزار آشپزی", 690000, 850000, "🔪", "ست کاربردی برای آشپزی روزانه"),
        Product(3, "ظروف نگهدارنده غذا", "ظروف", 420000, 520000, "🥡", "مناسب نگهداری مواد غذایی"),
        Product(4, "کتری و قوری", "نوشیدنی", 980000, 1150000, "🫖", "مناسب سرو چای و نوشیدنی"),
        Product(5, "تابه دوطرفه", "پخت‌وپز", 1250000, 1490000, "🍳", "مناسب پخت انواع غذا"),
        Product(6, "تخته برش آشپزخانه", "ابزار آشپزی", 350000, 0, "🪵", "کاربردی و مناسب آماده‌سازی"),
        Product(7, "ست لیوان شیشه‌ای", "ظروف", 560000, 680000, "🥛", "طراحی زیبا برای پذیرایی"),
        Product(8, "ظرف سرو دکوراتیو", "ظروف", 740000, 0, "🍽️", "مناسب پذیرایی و میز غذا")
    )

    private val categories = listOf(
        "همه", "پخت‌وپز", "ظروف", "ابزار آشپزی", "نوشیدنی"
    )

    private val cart = linkedMapOf<Int, Int>()
    private val favorites = mutableSetOf<Int>()

    private lateinit var productList: LinearLayout
    private lateinit var cartButton: TextView
    private lateinit var searchBox: EditText
    private var selectedCategory = "همه"
    private var searchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = navy
        window.navigationBarColor = navy
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        showHome()
    }

    private fun showHome() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(cream)
        }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(18), dp(14), dp(18), dp(14))
            setBackgroundColor(navy)
        }

        val brand = TextView(this).apply {
            text = "Blu Kitchen"
            textSize = 24f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
        }
        header.addView(brand, LinearLayout.LayoutParams(0, dp(48), 1f))

        cartButton = TextView(this).apply {
            text = "🛒 ${cart.values.sum()}"
            textSize = 16f
            gravity = Gravity.CENTER
            setTextColor(navy)
            background = rounded(gold, 16)
            setPadding(dp(16), dp(10), dp(16), dp(10))
            setOnClickListener { showCart() }
        }
        header.addView(cartButton)
        root.addView(header)

        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(14), dp(16), dp(20))
        }

        val welcome = TextView(this).apply {
            text = "آشپزخانه‌ای زیباتر، با Blu Kitchen"
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(ink)
        }
        content.addView(welcome)

        val subtitle = TextView(this).apply {
            text = "انتخابی هوشمند برای خانه شما"
            textSize = 14f
            setTextColor(Color.GRAY)
            setPadding(0, dp(5), 0, dp(14))
        }
        content.addView(subtitle)

        searchBox = EditText(this).apply {
            hint = "جست‌وجوی محصولات..."
            textSize = 15f
            isSingleLine = true
            setPadding(dp(14), dp(8), dp(14), dp(8))
            background = rounded(Color.WHITE, 14)
        }
        content.addView(searchBox, LinearLayout.LayoutParams(-1, dp(52)))

        val banner = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(18), dp(18), dp(18))
            background = rounded(navy, 18)
        }
        val bannerTitle = TextView(this).apply {
            text = "تجربه خرید متفاوت ✨"
            textSize = 22f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
        }
        banner.addView(bannerTitle)

        val bannerText = TextView(this).apply {
            text = "لوازم آشپزخانه باکیفیت، برای لحظه‌های خوشمزه"
            textSize = 14f
            setTextColor(Color.LTGRAY)
            setPadding(0, dp(8), 0, dp(12))
        }
        banner.addView(bannerText)

        val offer = TextView(this).apply {
            text = "مشاهده محصولات"
            textSize = 14f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(navy)
            gravity = Gravity.CENTER
            background = rounded(gold, 12)
            setPadding(dp(14), dp(10), dp(14), dp(10))
        }
        banner.addView(offer)
        content.addView(banner, LinearLayout.LayoutParams(-1, -2).apply {
            setMargins(0, dp(16), 0, dp(18))
        })

        val categoryTitle = TextView(this).apply {
            text = "دسته‌بندی محصولات"
            textSize = 18f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(ink)
        }
        content.addView(categoryTitle)

        val categoryScroll = HorizontalScrollView(this)
        val categoryRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, dp(12), 0, dp(18))
        }

        categories.forEach { category ->
            val item = TextView(this).apply {
                text = category
                textSize = 13f
                gravity = Gravity.CENTER
                setPadding(dp(16), dp(10), dp(16), dp(10))
                setTextColor(if (category == selectedCategory) Color.WHITE else navy)
                background = rounded(
                    if (category == selectedCategory) navy else Color.WHITE,
                    20
                )
                setOnClickListener {
                    selectedCategory = category
                    showHome()
                }
            }
            categoryRow.addView(item, LinearLayout.LayoutParams(-2, dp(42)).apply {
                setMargins(0, 0, dp(8), 0)
            })
        }

        categoryScroll.addView(categoryRow)
        content.addView(categoryScroll)

        val productsTitle = TextView(this).apply {
            text = "محصولات فروشگاه"
            textSize = 19f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(ink)
            setPadding(0, dp(4), 0, dp(12))
        }
        content.addView(productsTitle)

        productList = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        content.addView(productList)

        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        val bottom = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.WHITE)
            setPadding(dp(8), dp(10), dp(8), dp(10))
        }

        listOf("خانه", "محصولات", "علاقه‌مندی‌ها", "سبد خرید").forEachIndexed { index, label ->
            val nav = TextView(this).apply {
                text = listOf("⌂", "▦", "♡", "🛒")[index] + "\n" + label
                textSize = 12f
                gravity = Gravity.CENTER
                setTextColor(navy)
                setPadding(dp(4), dp(4), dp(4), dp(4))
                setOnClickListener {
                    when (index) {
                        0 -> {
                            selectedCategory = "همه"
                            searchText = ""
                            showHome()
                        }
                        1 -> {
                            selectedCategory = "همه"
                            showHome()
                        }
                        2 -> showFavorites()
                        3 -> showCart()
                    }
                }
            }
            bottom.addView(nav, LinearLayout.LayoutParams(0, -2, 1f))
        }
        root.addView(bottom)

        setContentView(root)
        renderProducts()

        searchBox.setText(searchText)
        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s?.toString() ?: ""
                renderProducts()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun renderProducts(favoritesOnly: Boolean = false) {
        productList.removeAllViews()

        val filtered = products.filter { product ->
            (selectedCategory == "همه" || product.category == selectedCategory) &&
            product.name.contains(searchText, ignoreCase = true) &&
            (!favoritesOnly || favorites.contains(product.id))
        }

        if (filtered.isEmpty()) {
            val empty = TextView(this).apply {
                text = "محصولی پیدا نشد"
                textSize = 16f
                setTextColor(Color.GRAY)
                gravity = Gravity.CENTER
                setPadding(0, dp(30), 0, dp(30))
            }
            productList.addView(empty)
        }

        filtered.forEach { product ->
            productList.addView(productCard(product))
        }
    }

    private fun productCard(product: Product): View {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            background = rounded(Color.WHITE, 18)
            setPadding(dp(12), dp(12), dp(12), dp(12))
        }

        val image = TextView(this).apply {
            text = product.emoji
            textSize = 38f
            gravity = Gravity.CENTER
            background = rounded(cream, 14)
        }
        card.addView(image, LinearLayout.LayoutParams(dp(82), dp(92)))

        val details = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), 0, dp(8), 0)
        }

        val name = TextView(this).apply {
            text = product.name
            textSize = 15f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(ink)
        }
        details.addView(name)

        val desc = TextView(this).apply {
            text = product.description
            textSize = 11f
            setTextColor(Color.GRAY)
            setPadding(0, dp(4), 0, dp(6))
        }
        details.addView(desc)

        val price = TextView(this).apply {
            text = "${formatPrice(product.price)} تومان"
            textSize = 14f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(navy)
        }
        details.addView(price)

        if (product.oldPrice > product.price) {
            val old = TextView(this).apply {
                text = "قیمت قبلی: ${formatPrice(product.oldPrice)}"
                textSize = 11f
                setTextColor(Color.GRAY)
            }
            details.addView(old)
        }

        val actions = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
        }

        val favorite = TextView(this).apply {
            text = if (favorites.contains(product.id)) "♥" else "♡"
            textSize = 24f
            gravity = Gravity.CENTER
            setTextColor(if (favorites.contains(product.id)) Color.RED else navy)
            setOnClickListener {
                if (favorites.contains(product.id)) favorites.remove(product.id)
                else favorites.add(product.id)
                renderProducts()
            }
        }
        actions.addView(favorite)

        val add = TextView(this).apply {
            text = "+"
            textSize = 24f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            background = rounded(navy, 12)
            setOnClickListener {
                cart[product.id] = (cart[product.id] ?: 0) + 1
                cartButton.text = "🛒 ${cart.values.sum()}"
                Toast.makeText(this@MainActivity, "به سبد خرید اضافه شد", Toast.LENGTH_SHORT).show()
            }
        }
        actions.addView(add, LinearLayout.LayoutParams(dp(38), dp(38)).apply {
            topMargin = dp(8)
        })

        card.addView(details, LinearLayout.LayoutParams(0, -2, 1f))
        card.addView(actions, LinearLayout.LayoutParams(dp(42), -2))

        return card.apply {
            setOnClickListener {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle(product.name)
                    .setMessage("${product.description}\n\nقیمت: ${formatPrice(product.price)} تومان")
                    .setPositiveButton("افزودن به سبد") { _, _ ->
                        cart[product.id] = (cart[product.id] ?: 0) + 1
                        cartButton.text = "🛒 ${cart.values.sum()}"
                    }
                    .setNegativeButton("بستن", null)
                    .show()
            }
        }.also {
            it.layoutParams = LinearLayout.LayoutParams(-1, -2).apply {
                bottomMargin = dp(12)
            }
        }
    }

    private fun showCart() {
        val message = if (cart.isEmpty()) {
            "سبد خرید شما خالی است."
        } else {
            cart.entries.joinToString("\n") { entry ->
                val product = products.first { it.id == entry.key }
                "${product.name} × ${entry.value} = ${formatPrice(product.price * entry.value)} تومان"
            } + "\n\nمجموع: ${formatPrice(cart.entries.sumOf { entry ->
                products.first { it.id == entry.key }.price * entry.value
            })} تومان"
        }

        AlertDialog.Builder(this)
            .setTitle("سبد خرید شما")
            .setMessage(message)
            .setPositiveButton("ثبت سفارش در واتساپ") { _, _ ->
                if (cart.isNotEmpty()) sendOrder()
            }
            .setNeutralButton("خالی کردن سبد") { _, _ ->
                cart.clear()
                cartButton.text = "🛒 0"
                Toast.makeText(this, "سبد خرید خالی شد", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("ادامه خرید", null)
            .show()
    }

    private fun sendOrder() {
        val order = cart.entries.joinToString("\n") { entry ->
            val product = products.first { it.id == entry.key }
            "${product.name} - تعداد: ${entry.value}"
        }
        val total = cart.entries.sumOf { entry ->
            products.first { it.id == entry.key }.price * entry.value
        }

        val message = "سلام Blu Kitchen 🌿\nمی‌خواهم سفارش ثبت کنم:\n$order\n\nمجموع: ${formatPrice(total)} تومان"
        val url = "https://wa.me/?text=${Uri.encode(message)}"
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun showFavorites() {
        selectedCategory = "همه"
        searchText = ""
        showHome()
        renderProducts(true)
    }

    private fun formatPrice(value: Int): String =
        String.format("%,d", value)

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()

    private fun rounded(color: Int, radius: Int): GradientDrawable =
        GradientDrawable().apply {
            setColor(color)
            cornerRadius = dp(radius).toFloat()
        }
}
