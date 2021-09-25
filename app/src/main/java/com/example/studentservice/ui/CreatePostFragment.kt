package com.example.studentservice.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import com.example.studentservice.databinding.CreateFragmentBinding
import com.example.studentservice.util.ProgressRequestBody
import com.example.studentservice.util.TempStorage
import com.example.studentservice.util.Utils
import com.example.studentservice.viewmodel.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


@AndroidEntryPoint
class CreatePostFragment : Fragment() {

    private var _binding: CreateFragmentBinding? = null;
    private val binding get() = _binding!!;
    private lateinit var selectedImageURI: Uri;
    private val viewModel: PostsViewModel by viewModels();

    companion object {
        const val IMAGE_PICK_CODE = 1;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreateFragmentBinding.inflate(inflater, container, false);
        return _binding!!.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        creatingAndSendingPost()
        choosingImageForPost();
    }


    private fun choosingImageForPost() {
        binding.chooseImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICK_CODE);
        }
    }

    private fun creatingAndSendingPost() {

        binding.btnPost.setOnClickListener {
            createPostAndSending();
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
            inputMethodManager.hideSoftInputFromWindow(
                requireActivity().currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageURI = data.data!!;
            binding.chooseImage.setImageURI(selectedImageURI);
        }
    }

    private fun createPostAndSending() {

        Utils.permissionToCreateFile(requireActivity());

        var email = TempStorage.getEmail();
        email = "Bekjan1";
        val subject = binding.inputSubject.editText?.text.toString();
        val content = binding.inputSubject.editText?.text.toString();
        var file: File?;
        if (this::selectedImageURI.isInitialized) {
            file = File(Utils.getPath(selectedImageURI!!, requireActivity()));
        } else {
            file = File.createTempFile("temp", "comp");
            file.createNewFile();
            Log.e("Bekzhan file", "createPostAndSending: ${file.length()} ")
        }
        val request_email = RequestBody.create(MediaType.parse("multipart/form-data"), email);
        val request_subject = RequestBody.create(MediaType.parse("multipart/form-data"), subject);
        val request_content = RequestBody.create(MediaType.parse("multipart/form-data"), content);
        val request_image =
            ProgressRequestBody(file!!, "image", object : ProgressRequestBody.UploadCallBack {

                override fun onProgressUpdate(percentage: Int) {

                }
            });
        val image_multi_part =
            MultipartBody.Part.createFormData("image", file?.name, request_image);
        viewModel.sendingPost(request_email, request_subject, request_content, image_multi_part);
        viewModel.sending_post.observe(viewLifecycleOwner) {
            if (it)
                binding.progressBar.visibility = View.VISIBLE
            else
                binding.progressBar.visibility = View.GONE
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null;
    }


}