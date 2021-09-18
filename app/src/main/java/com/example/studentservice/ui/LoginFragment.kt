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

            if (it == "notEnabled") {
                Toast.makeText(
                    requireContext(),
                    "Please, click the link to confirm your email",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (it == "success") {
                startActivity(Intent(requireContext(), MainActivity::class.java))
            } else if (it == "error") {
                Toast.makeText(requireContext(), "Student can't be found", Toast.LENGTH_LONG)
                    .show();
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