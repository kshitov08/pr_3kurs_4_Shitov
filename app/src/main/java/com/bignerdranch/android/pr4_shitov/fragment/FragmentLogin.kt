package com.bignerdranch.android.pr4_shitov.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bignerdranch.android.pr4_shitov.MainActivity
import com.bignerdranch.android.pr4_shitov.R

class FragmentLogin : Fragment() {

    companion object {
        const val PREFS_NAME = "UserPrefs"
        const val KEY_LOGIN = "login"
        const val KEY_PASSWORD = "password"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editLogin = view.findViewById<EditText>(R.id.editLogin)
        val editPassword = view.findViewById<EditText>(R.id.editPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)


        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedLogin = prefs.getString(KEY_LOGIN, "")
        if (!savedLogin.isNullOrEmpty()) {
            editLogin.setText(savedLogin)
        }

        btnLogin.setOnClickListener {
            val login = editLogin.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Введите логин и пароль", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val savedPassword = prefs.getString(KEY_PASSWORD, "")

            if (login == savedLogin && password == savedPassword) {
                prefs.edit().putString("current_user", login).apply()
                Toast.makeText(requireContext(), "Вход выполнен!", Toast.LENGTH_SHORT).show()
                (requireActivity() as MainActivity).switchFragment(FragmentAddFilm())
            } else {
                Toast.makeText(requireContext(), "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegister.setOnClickListener {
            val login = editLogin.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Введите логин и пароль", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            prefs.edit().apply {
                putString(KEY_LOGIN, login)
                putString(KEY_PASSWORD, password)
                apply()
            }

            Toast.makeText(requireContext(), "Регистрация успешна!", Toast.LENGTH_SHORT).show()
        }
    }
}