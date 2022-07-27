package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.utils.hideKeyboard
import ru.netology.nmedia.utils.showKeyboard
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostAdapter(viewModel)
        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        binding.saveButton.setOnClickListener {
            with(binding.contentEdit) {
                val content = text.toString()
                viewModel.onSaveButtonClicked(content)
                clearFocus()
                hideKeyboard()
            }
        }
        viewModel.currentPost.observe(this) { currentPost ->
            with (binding.contentEdit) {
                val content = currentPost?.content
                setText(content)
                if (content != null) {
                    setSelection(text.length)
                    requestFocus()
                    showKeyboard()
                    binding.editGroup.visibility = View.VISIBLE
                } else {
                    clearFocus()
                    hideKeyboard()
                    binding.editGroup.visibility = View.GONE
                }
            }
        }
        binding.editCancel.setOnClickListener {
            viewModel.onCancelEditClicked()
            with(binding) {
                editGroup.visibility = View.GONE
                contentEdit.clearFocus()
                contentEdit.hideKeyboard()
            }
        }
    }
}


