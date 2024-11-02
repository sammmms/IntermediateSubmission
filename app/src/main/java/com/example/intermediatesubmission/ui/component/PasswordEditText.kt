package com.example.intermediatesubmission.ui.component

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.intermediatesubmission.util.PasswordTextWatcher

class PasswordEditText : AppCompatEditText {
    private val _errorText = MutableLiveData<String>()
    val errorText: LiveData<String> = _errorText


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        val passwordTextWatcher = PasswordTextWatcher(::validator, context)
        addTextChangedListener(passwordTextWatcher)

        errorText.observeForever {
            error = it.ifEmpty {
                null
            }
            Log.d("PasswordEditText", "Error text: ${errorText.value}")
        }
    }

    fun validator(string: String): String {
        _errorText.value = string
        return string
    }
}