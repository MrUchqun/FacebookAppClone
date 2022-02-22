package com.example.facebookappclone.activity

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.*
import com.airbnb.lottie.LottieAnimationView
import com.example.facebookappclone.R
import com.example.facebookappclone.helper.Utils
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jsoup.nodes.Document

class CreatePostActivity : AppCompatActivity() {

    private lateinit var etNewPost: EditText
    private lateinit var btnPost: Button
    private lateinit var llPreview: LinearLayout
    private lateinit var ivPost: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var btnClose: ImageView
    private lateinit var laLoading: LottieAnimationView
    private var isFindLink = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        initViews()

        window.statusBarColor = Color.parseColor("#EBFBFF")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun initViews() {
        etNewPost = findViewById(R.id.et_post_text)
        btnPost = findViewById(R.id.btn_post)
        btnClose = findViewById(R.id.iv_close)
        ivPost = findViewById(R.id.iv_post)
        tvTitle = findViewById(R.id.tv_title)
        tvDescription = findViewById(R.id.tv_description)
        llPreview = findViewById(R.id.ll_preview)
        laLoading = findViewById(R.id.la_loading)

        btnClose.setOnClickListener {
            finish()
        }

        etNewPost.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (containsLink(p0.toString())) llPreview.visibility = View.VISIBLE
                else llPreview.visibility = View.GONE

                if (p0.toString() == "") btnPost.setBackgroundResource(R.drawable.shape_border_rounded)
                else btnPost.setBackgroundResource(R.drawable.shape_border_rounded_enabled)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        btnPost.setOnClickListener {

        }
    }

    private fun getElementsJsoup(url: String) {
        Utils.getJsoupData(url)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result: Document ->
                val metaTags = result.getElementsByTag("meta")
                for (element in metaTags) {
                    when {
                        element.attr("property").equals("og:image") -> {
                            Picasso.get().load(element.attr("content")).into(ivPost)
                        }
                        element.attr("property").equals("og:description") -> {
                            tvDescription.text = element.attr("content")
                        }
                        element.attr("property").equals("og:title") -> {
                            tvTitle.text = element.attr("content")
                        }
                    }
                }
                laLoading.visibility = View.GONE
            }
    }

    private fun containsLink(input: String): Boolean {
        val parts = input.split(" ")
        for (item in parts) {
            if (Patterns.WEB_URL.matcher(item).matches()) {
                getElementsJsoup(item)
                isFindLink = true
                return true
            }
        }
        return false
    }
}