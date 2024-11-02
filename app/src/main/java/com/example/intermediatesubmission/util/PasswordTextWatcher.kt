package com.example.intermediatesubmission.util

import android.content.Context
import android.text.TextWatcher
import com.example.intermediatesubmission.R

class PasswordTextWatcher(val validator: (string: String) -> String, var context: Context) :
    TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Do nothing
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s.toString().length < 8) {
            validator(context.getString(R.string.password_not_valid))
            return
        }
        validator("")
    }

    override fun afterTextChanged(s: android.text.Editable?) {
        // Do nothing
    }

    init {
        validator("")
    }


}