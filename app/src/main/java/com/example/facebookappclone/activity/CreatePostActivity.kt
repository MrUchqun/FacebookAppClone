package com.example.facebookappclone.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.facebookappclone.R
import com.example.facebookappclone.helper.Utils
import com.example.facebookappclone.model.LinkPost
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jsoup.nodes.Document
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL


class CreatePostActivity : AppCompatActivity() {

    private lateinit var etNewPost: EditText
    private lateinit var btnPost: Button
    private lateinit var ivProfile: ImageView
    private lateinit var ivPost: ImageView
    private lateinit var ivClose: ImageView
    private lateinit var ivCancel: ImageView
    private lateinit var tvFullName: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var laLoading: LottieAnimationView
    private lateinit var llPreview: LinearLayout

    private lateinit var linkPost: LinkPost
    private var image: String? = null
    private var description: String? = null
    private var title: String? = null
    private var linkUrl: String? = null

    private var isFindLink: Boolean = false
    private var canceled: Boolean = false
    private var enabledPost: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        initViews()
    }

    private fun initViews() {
        etNewPost = findViewById(R.id.et_post_text)
        btnPost = findViewById(R.id.btn_post)
        ivClose = findViewById(R.id.iv_close)
        ivPost = findViewById(R.id.iv_post)
        ivCancel = findViewById(R.id.iv_cancel)
        ivProfile = findViewById(R.id.iv_profile)
        tvFullName = findViewById(R.id.tv_fullName)
        tvTitle = findViewById(R.id.tv_title)
        tvDescription = findViewById(R.id.tv_description)
        llPreview = findViewById(R.id.ll_preview)
        laLoading = findViewById(R.id.la_loading)

        linkPost = intent.getSerializableExtra("linkPost") as LinkPost
        Picasso.get().load(linkPost.profile).into(ivProfile)
        tvFullName.text = linkPost.fullName

        ivClose.setOnClickListener {
            finish()
        }

        btnPost.setOnClickListener {

            if (enabledPost) {
                linkPost.post = etNewPost.text.toString()
                linkPost.link = linkUrl

                if (!canceled) {
                    linkPost.image = image
                    linkPost.description = description
                    linkPost.title = title
                }
            }

            backToFinish()
        }

        ivCancel.setOnClickListener {
            llPreview.visibility = View.GONE
            canceled = true
        }

        etNewPost.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                getPreview(p0.toString())
                changePostButtonBack(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun backToFinish() {
        val intent = Intent()
        intent.putExtra("newPost", linkPost)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun getElementsJsoup(url: String) {

        llPreview.visibility = View.VISIBLE
        laLoading.visibility = View.VISIBLE
        tvTitle.visibility = View.INVISIBLE
        tvDescription.visibility = View.INVISIBLE
        ivPost.visibility = View.GONE
        canceled = false

        val subscribe = Utils.getJsoupData(url)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: Document ->
                val metaTags = result.getElementsByTag("meta")
                for (element in metaTags) {
                    when {
                        element.attr("property").equals("og:image") -> {
                            image = element.attr("content")
                            Picasso.get().load(image).into(ivPost)
                        }
//                        element.attr("property").equals("og:description") -> {
//                            description = element.attr("content")
//                            tvDescription.text = description
//                        }
                        element.attr("property").equals("og:title") -> {
                            description = element.attr("content")
                            tvDescription.text = description
                        }
                    }
                }

                title = URI(url).host
                tvTitle.text = title

                laLoading.visibility = View.GONE
                tvTitle.visibility = View.VISIBLE
                tvDescription.visibility = View.VISIBLE
                ivPost.visibility = View.VISIBLE

            }, { error ->
                Timber.d(error)
            })
    }

    private fun containsLink(input: String): Boolean {
        val parts = input.split(" ")
        for (item in parts) {
            if (Patterns.WEB_URL.matcher(item).matches()) {
                isFindLink = true
                linkUrl = item
                return true
            }
        }
        return false
    }

    private fun getPreview(text: String) {
        if (!isFindLink) {
            if (containsLink(text)) {
                getElementsJsoup(linkUrl!!)
            }
        }
    }

    private fun changePostButtonBack(text: String) {
        enabledPost = if (text == "") {
            btnPost.setBackgroundResource(R.drawable.shape_border_rounded)
            false
        } else {
            btnPost.setBackgroundResource(R.drawable.shape_border_rounded_enabled)
            true
        }
    }

    private fun checkUrl(host: String): Boolean {
        return try {
            HttpURLConnection.setFollowRedirects(false)
            val con: HttpURLConnection = URL(host).openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.responseCode == HttpURLConnection.HTTP_OK
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}