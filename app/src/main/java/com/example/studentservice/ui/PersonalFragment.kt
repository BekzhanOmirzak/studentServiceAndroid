package com.example.studentservice.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.studentservice.databinding.PersonalFragmentBinding
import com.example.studentservice.entities.ImageFile
import com.example.studentservice.entities.Student
import com.example.studentservice.util.ProgressRequestBody
import com.example.studentservice.util.Resource
import com.example.studentservice.util.TempStorage
import com.example.studentservice.util.Utils
import com.example.studentservice.viewmodel.PersonalViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


@AndroidEntryPoint
class PersonalFragment : Fragment() {

    private var _binding: PersonalFragmentBinding? = null;
    private val binding get() = _binding!!;
    private val viewModel: PersonalViewModel by viewModels();
    private var oldEmail: String = ""
    private var selectedImage: Uri? = null;
    private var x1: Float = 0.0f;
    private var x2: Float = 0.0f;
    private lateinit var listImageFiles: List<ImageFile>;
    private var cur_index: Int = 0;


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PersonalFragmentBinding.inflate(inflater, container, false);
        return _binding!!.root;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        retrivingStudentsDetail()
        updateStudent();
        uploadImage();
        updatingStudentsDetailLiveData();
        showingAllImages();
        detectingSwipeListener();
        closingImageViewByClickingOutside();

    }

    private fun closingImageViewByClickingOutside() {
        binding.showAllImages.visibility = View.GONE
        binding.parent.setOnClickListener {
            binding.showAllImages.visibility = View.GONE;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun detectingSwipeListener() {

        binding.showAllImages.setOnTouchListener { v, event ->

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x1 = event.getX();
                }
                MotionEvent.ACTION_UP -> {
                    x2 = event.getX();
                    val deltaX = x2 - x1;
                    if (deltaX < -10) {

                        if (cur_index < listImageFiles.size - 1)
                            cur_index++;

                        val imageFile = listImageFiles.get(cur_index);
                        Log.e(
                            "Bekzhan left",
                            "detectingSwipeListener: $cur_index out of ${listImageFiles.size}"
                        )
                        updateStudentsPhoto(imageFile.path, imageFile.fileName)

                    } else if (deltaX > 10) {

                        if (cur_index > 0)
                            cur_index--;

                        Log.e(
                            "Bekzhan right",
                            "detectingSwipeListener: $cur_index out of ${listImageFiles.size}"
                        )
                        val imageFile = listImageFiles.get(cur_index);
                        updateStudentsPhoto(imageFile.path, imageFile.fileName);
                    }
                }
            }
            true;
        }

    }

    private fun showingAllImages() {

        binding.btnViewAllImages.setOnClickListener {
            binding.showAllImages.visibility = View.VISIBLE;
            binding.showAllImages.setImageDrawable(binding.circleImage.drawable);
            binding.showAllImages.setOnClickListener {

            }
        }

        viewModel.gettingListImageFiles.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    listImageFiles = it.data!!;
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE;
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE;
                }
            }
        }


    }

    private fun updatingStudentsDetailLiveData() {
        viewModel.updatingStudentsDetail.observe(viewLifecycleOwner) {
            if (it)
                binding.progressBar.visibility = View.VISIBLE
            else
                binding.progressBar.visibility = View.GONE
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && null != data) {
            val uri = data.data!!;
            selectedImage = data.data!!;
            binding.circleImage.setImageURI(uri);
        }

    }


    private fun uploadImage() {
        binding.circleImage.setOnClickListener {

            Utils.permissionToCreateFile(requireActivity());

            val intent = Intent(Intent.ACTION_PICK);
            intent.type = "image/*"
            startActivityForResult(intent, 1);

        }
    }


    private fun updateStudent() {

        binding.btnSave.setOnClickListener {
            it.requestFocus()
            Log.e("Bekzhan save", "saved")
            val student = Student(1);
            student.email = binding.inputEmail.editText!!.text.toString();
            student.phoneNumber = binding.inputPhone.editText!!.text.toString();
            student.major = binding.inputMajor.editText!!.text.toString();
            student.university = binding.inputUniversity.editText!!.text.toString();
            student.lastName = binding.inputLast.editText!!.text.toString();
            student.name = binding.inputFirstName.editText!!.text.toString();
            if (selectedImage != null) {
                Log.e("Bekzhan uri", "updateStudent: ${selectedImage}")
                val file = File(Utils.getPath(selectedImage!!, requireActivity()));
//                val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                val requestBody =
                    ProgressRequestBody(file, "image", object : ProgressRequestBody.UploadCallBack {
                        override fun onProgressUpdate(percentage: Int) {
                            Toast.makeText(
                                requireContext(),
                                "Percentage : $percentage",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                val fileToUpload =
                    MultipartBody.Part.createFormData("image", file.name, requestBody);
                val requestBodyEmail = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    "bekjan1"
                );
                viewModel.uploadPhoto(requestBodyEmail, fileToUpload);
            }
            viewModel.updateStudent(oldEmail, student);

        }
    }

    private fun retrivingStudentsDetail() {
        viewModel.getStudentDetailByEmail("bekjan1");
        viewModel.studentLiveData.observe(viewLifecycleOwner) {
            Log.e("Bekzhan personal", it.message.toString())
            when (it) {
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE;
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val student = it.data!!;
                    oldEmail = student.email;
                    TempStorage.saveEmail(oldEmail);
                    binding.inputEmail.editText!!.setText(student.email);
                    binding.inputPhone.editText!!.setText(student.phoneNumber);
                    binding.inputFirstName.editText!!.setText(student.name);
                    binding.inputLast.editText!!.setText(student.lastName);
                    binding.inputMajor.editText!!.setText(student.major);
                    updateStudentsPhoto(student.imagePath, student.imageLink);
                    viewModel.getListOfImageFiles(oldEmail);
                }
            }
        }
    }

    private fun updateStudentsPhoto(imagePath: String, imageLink: String) {
        viewModel.downloadPhoto(imagePath, imageLink);
        viewModel.gettingImageByteArray.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.e("Bekzhan byteArray", "updateStudentsPhoto: ${it.data}")
                    binding.progressBar.visibility = View.GONE
                    val bmp = BitmapFactory.decodeByteArray(it.data, 0, it.data!!.size);
                    binding.circleImage.setImageBitmap(bmp);
                    binding.showAllImages.setImageBitmap(bmp);
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null;
    }


}