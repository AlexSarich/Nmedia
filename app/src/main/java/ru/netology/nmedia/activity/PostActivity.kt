package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.PostContentActivityBinding
import ru.netology.nmedia.dto.EditPostResult
import ru.netology.nmedia.utils.focusAndShowKeyboard

class PostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostContentActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentReceived = intent ?: return
        val text = intentReceived.getStringExtra(RESULT_KEY)
        val url = intentReceived.getStringExtra("newVideoUrl")
        binding.edit.setText(text)
        binding.edit.focusAndShowKeyboard()
        binding.videoUrl.setText(url)

        binding.edit.requestFocus()
        binding.ok.setOnClickListener {
            val intent = Intent()
            if (binding.edit.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.edit.text.toString()
                val videosUrl = binding.videoUrl.text.toString()
                intent.putExtra(RESULT_KEY, content)
                intent.putExtra("newVideosUrl", videosUrl)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    object ResultContract : ActivityResultContract<EditPostResult?, EditPostResult?>() {
        override fun createIntent(context: Context, input: EditPostResult?): Intent {
            val intent = Intent(context, PostActivity::class.java)
            intent.putExtra(RESULT_KEY, input?.newContent)
            intent.putExtra("newVideoUrl", input?.newVideosUrl)
            return intent
        }
        override fun parseResult(resultCode: Int, intent: Intent?): EditPostResult? =
            if (resultCode == Activity.RESULT_OK) {
                EditPostResult(
                newContent = intent?.getStringExtra(RESULT_KEY),
                newVideosUrl = intent?.getStringExtra("newVideoUrl")
                )
            } else null
    }

    private companion object {
        private const val RESULT_KEY = "postNewContent"
    }
}
