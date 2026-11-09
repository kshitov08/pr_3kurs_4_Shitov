package com.bignerdranch.android.pr4_shitov.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bignerdranch.android.pr4_shitov.MainActivity
import com.bignerdranch.android.pr4_shitov.R
import com.bignerdranch.android.pr4_shitov.model.Film
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FragmentFilmList : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_film_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvFilmList = view.findViewById<TextView>(R.id.tvFilmList)
        val btnBack = view.findViewById<Button>(R.id.btnBack)

        val userPrefs = requireContext()
            .getSharedPreferences(FragmentLogin.PREFS_NAME, Context.MODE_PRIVATE)
        val currentUser = userPrefs.getString("current_user", "default") ?: "default"
        val userKey = "${FragmentAddFilm.KEY_FILMS}_$currentUser"

        val prefs = requireContext()
            .getSharedPreferences(FragmentAddFilm.PREFS_NAME, Context.MODE_PRIVATE)
        val json: String? = prefs.getString(userKey, null)

        val films: List<Film> = if (json.isNullOrEmpty()) {
            emptyList()
        } else {
            val type = object : TypeToken<List<Film>>() {}.type
            Gson().fromJson<List<Film>>(json, type)
        }

        if (films.isEmpty()) {
            tvFilmList.text = "Список пуст. Добавьте фильмы!"
            Toast.makeText(requireContext(), "Список пуст", Toast.LENGTH_SHORT).show()
        } else {
            val sb = StringBuilder()
            for ((index, film) in films.withIndex()) {
                sb.append("${index + 1}. ${film.title}\n")
                sb.append("Жанр: ${film.genre}\n")
                sb.append("Год: ${film.year}\n")
                sb.append("Рейтинг: ${film.rating}\n")
                sb.append("──────────────\n")
            }
            tvFilmList.text = sb.toString()
        }

        btnBack.setOnClickListener {
            (requireActivity() as MainActivity).switchFragment(FragmentAddFilm())
        }
    }
}