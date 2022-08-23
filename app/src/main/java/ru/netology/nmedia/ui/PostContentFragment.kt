package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.PostContentFragmentBinding
import ru.netology.nmedia.utils.focusAndShowKeyboard

class PostContentFragment : Fragment() {

    private val args by navArgs<PostContentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostContentFragmentBinding.inflate(
        layoutInflater,
        container,
        false).also { binding ->
        binding.edit.setText(args.initialContent)
        binding.edit.focusAndShowKeyboard()
        binding.videoUrl.setText(args.initialVideoUrl)
        binding.ok.setOnClickListener {
            onOkButtonClicked(binding)
            findNavController().navigateUp()
        }
    }.root

    private fun onOkButtonClicked(binding: PostContentFragmentBinding) {
        val text = binding.edit.text
        val videoUrl = binding.videoUrl.text
        if (!text.isNullOrBlank()) {
            val resultBundle = Bundle(2)
            resultBundle.putString(RESULT_KEY, text.toString())
            resultBundle.putString(RESULT_VIDEO_KEY, videoUrl.toString())
            setFragmentResult(REQUEST_KEY, resultBundle)
        }
        findNavController().popBackStack()
    }

    companion object {
        const val REQUEST_KEY = "requestKey"
        const val RESULT_KEY = "newContent"
        const val RESULT_VIDEO_KEY = "newVideoUrl"
    }
}
