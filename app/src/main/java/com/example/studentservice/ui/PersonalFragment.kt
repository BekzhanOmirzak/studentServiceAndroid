package com.example.studentservice.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.studentservice.databinding.PersonalFragmentBinding
import com.example.studentservice.entities.Student
import com.example.studentservice.util.Resource
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

    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

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

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && null != data) {
            val uri = data.data!!;



            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor: Cursor? = requireActivity().contentResolver.query(
                uri,
                filePathColumn, null, null, null
            )
            cursor!!.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()

            val file = File(picturePath)

            val filePart = MultipartBody.Part.createFormData(
                "file", file.name, RequestBody.create(
                    MediaType.parse("image/*"), file
                )
            );

            viewModel.uploadPhoto("bekjan.omirzak98@gmail.com", filePart);

        }

    }


    private fun uploadImage() {
        binding.circleImage.setOnClickListener {


            val permission = ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    PERMISSIONS_STORAGE,
                    1
                );
            }

            val intent = Intent(Intent.ACTION_PICK);
            intent.type = "image/*"
            startActivityForResult(intent, 1);
        }
    }


    private fun updateStudent() {

        binding.btnSave.setOnClickListener {
            Log.e("Bekzhan save", "saved")
            val student = Student();
            student.email = binding.inputEmail.editText!!.text.toString();
            student.phoneNumber = binding.inputPhone.editText!!.text.toString();
            student.major = binding.inputMajor.editText!!.text.toString();
            student.university = binding.inputUniversity.editText!!.text.toString();
            student.lastName = binding.inputLast.editText!!.text.toString();
            student.name = binding.inputFirstName.editText!!.text.toString();
            viewModel.updateStudent(oldEmail, student);
        }

    }

    private fun retrivingStudentsDetail() {
        viewModel.getStudentDetailByEmail("bekjan.omirzak98@gmail.com");
        viewModel.studentLiveData.observe(viewLifecycleOwner) {
            Log.e("Bekzhan personal", it.message.toString())
            when (it) {
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val student = it.data!!;
                    oldEmail = student.email;
                    binding.inputEmail.editText!!.setText(student.email);
                    binding.inputPhone.editText!!.setText(student.phoneNumber);
                    binding.inputFirstName.editText!!.setText(student.name);
                    binding.inputLast.editText!!.setText(student.lastName);
                    binding.inputMajor.editText!!.setText(student.major);
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null;
    }


}