package com.example.studentservice.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.studentservice.R
import com.example.studentservice.databinding.LoginFragmentBinding
import com.example.studentservice.util.Resource
import com.example.studentservice.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null;
    private val binding get() = _binding!!;
    private val viewModel: LoginViewModel by viewModels();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false);
        val view = binding.root;
        return view;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewSignUp.setOnClickListener() {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, RegisterFragment()).addToBackStack("Register").commit();
        }

        viewModel.loginLiveData.observe(viewLifecycleOwner) {

            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE;
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE;
                    Log.e("Bekzhan login status", it.data!!);
                    if (it.data == "notEnabled") {
                        Toast.makeText(
                            requireContext(),
                            "Please, click the link to confirm your email",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (it.data == "success") {
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE;
                    Toast.makeText(requireContext(), "Failed ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }


        }

        binding.btnLogin.setOnClickListener() {
            studentSignIn();
        }

    }

    private fun studentSignIn() {
        if (!validateUserEmail() xor !validateUserPassword())
            return

        val email = binding.textInputEmail.editText?.text.toString();
        val password = binding.textInputPassword.editText?.text.toString();
        viewModel.loginByEmailPassword(email, password);
    }


    private fun validateUserPassword(): Boolean {
        if (binding.textInputPassword.editText?.text.toString().isEmpty()) {
            binding.textInputPassword.error = "Password can't be empty";
            return false;
        }
        return true;
    }

    private fun validateUserEmail(): Boolean {
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null;
    }




}