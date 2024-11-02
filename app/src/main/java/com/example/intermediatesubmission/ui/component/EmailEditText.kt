package com.example.intermediatesubmission.ui.component

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.intermediatesubmission.util.EmailTextWatcher

class EmailEditText : AppCompatEditText {
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
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        val emailTextWatcher = EmailTextWatcher(::validator, context)
        addTextChangedListener(emailTextWatcher)

        errorText.observeForever {
            error = it.ifEmpty {
                null
            }
            Log.d("EmailEditText", "Error text: ${errorText.value}")
        }
    }

    fun validator(string: String): String {
        _errorText.value = string
        return string
    }
}