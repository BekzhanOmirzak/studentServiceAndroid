package com.example.studentservice.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentservice.R
import com.example.studentservice.databinding.RegisterFragmentBinding
import com.example.studentservice.entities.Student
import com.example.studentservice.util.Resource
import com.example.studentservice.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlin.concurrent.thread
import kotlin.math.log


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: RegisterFragmentBinding? = null;
    private val binding get() = _binding!!;
    private val viewModel: LoginViewModel by viewModels();


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegisterFragmentBinding.inflate(inflater, container, false);
        val view = _binding!!.root;
        return view;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnRegister.setOnClickListener {
            studentRegister();
        }
        observingRegisterLiveData()
        binding.viewSignIn.setOnClickListener {
            activity?.supportFragmentManager!!.beginTransaction()
                .replace(R.id.container, LoginFragment()).addToBackStack(null).commit();
        }
    }

    private fun observingRegisterLiveData() {
        viewModel.registerLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.visibility=View.GONE;
                    if (it.data == "success") {
                        Toast.makeText(
                            requireContext(),
                            "Confirmation Link sent to ${binding.textInputEmail.editText?.text.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Thread {
                            Thread.sleep(3000L)
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.container, LoginFragment()).commit();
                        }
                    } else if (it.data == "exist") {
                        Toast.makeText(requireContext(), "Email is not available", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility=View.GONE;
                    Toast.makeText(
                        requireContext(),
                        "Error status : ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility=View.VISIBLE;
                }
            }
        }
    }


    private fun studentRegister() {
        if (!validateUserEmail() xor !validateUserPassword())
            return

        val student = Student();
        student.email = binding.textInputEmail.editText?.text.toString()
        student.password = binding.textInputPassword.editText?.text.toString();
        viewModel.register(student);
    }


    fun validateUserEmail(): Boolean {
        val email = binding.textInputEmail.editText?.text.toString();
        if (email.isEmpty()) {
            binding.textInputEmail.error = "Email can't be empty";
            return false;
        } else if (email.matches(Regex(" \"^(.+)@(.+)\$\""))) {
            binding.textInputEmail.error = "Provide valid email"
            return false;
        }
        return true;
    }

    fun validateUserPassword(): Boolean {
        val password1 = binding.textInputPassword.editText?.text.toString()
        val password2 = binding.textInputRePassword.editText?.text.toString();
        if (password1 != password2) {
            binding.textInputPassword.error = "Passwords must match";
            return false;
        }
        return true;
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null;
    }


}