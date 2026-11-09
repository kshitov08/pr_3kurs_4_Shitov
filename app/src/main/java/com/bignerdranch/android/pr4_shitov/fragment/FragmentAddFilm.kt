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
import com.bignerdranch.android.pr4_shitov.model.Film
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FragmentAddFilm : Fragment() {

    companion object {
        const val PREFS_NAME = "FilmPrefs"
        const val KEY_FILMS = "films_json"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_film, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTitle = view.findViewById<EditText>(R.id.editTitle)
        val editGenre = view.findViewById<EditText>(R.id.editGenre)
        val editYear = view.findViewById<EditText>(R.id.editYear)
        val editRating = view.findViewById<EditText>(R.id.editRating)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnShowList = view.findViewById<Button>(R.id.btnShowList)

        btnSave.setOnClickListener {
            val title = editTitle.text.toString().trim()
            val genre = editGenre.text.toString().trim()
            val year = editYear.text.toString().toIntOrNull() ?: 0
            val rating = editRating.text.toString().toDoubleOrNull() ?: 0.0

            if (title.isEmpty() || genre.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните название и жанр", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newFilm = Film(title, genre, year, rating)

            val userPrefs = requireContext()
                .getSharedPreferences(FragmentLogin.PREFS_NAME, Context.MODE_PRIVATE)
            val currentUser = userPrefs.getString("current_user", "default") ?: "default"

            val userKey = "${KEY_FILMS}_$currentUser"

            val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val json: String? = prefs.getString(userKey, null)

            val films: ArrayList<Film> = if (json == null || json.length == 0) {
                ArrayList<Film>()
            } else {
                val type = object : TypeToken<ArrayList<Film>>() {}.type
                Gson().fromJson<ArrayList<Film>>(json, type)
            }

            films.add(newFilm)

            val newJson = Gson().toJson(films)
            prefs.edit().putString(userKey, newJson).apply()

            Toast.makeText(requireContext(), "Фильм добавлен!", Toast.LENGTH_SHORT).show()

            editTitle.text.clear()
            editGenre.text.clear()
            editYear.text.clear()
            editRating.text.clear()
        }

        btnShowList.setOnClickListener {
            (requireActivity() as MainActivity).switchFragment(FragmentFilmList())
        }
    }
}