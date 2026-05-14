package com.example.studynotesapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val registerText = findViewById<TextView>(R.id.registerText)

        loginBtn.setOnClickListener {
            loginUser()
        }

        registerText.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }
    }

    private fun loginUser() {

        val url =
            SupabaseConfig.SUPABASE_URL +
                    "/auth/v1/token?grant_type=password"

        val request = object : StringRequest(
            Request.Method.POST,
            url,

            { response ->

                try {

                    val obj = JSONObject(response)

                    val accessToken =
                        obj.getString("access_token")

                    val user =
                        obj.getJSONObject("user")

                    val userId =
                        user.getString("id")

                    val userEmail =
                        user.getString("email")

                    var userName = "User"

                    if (
                        user.has("user_metadata")
                    ) {

                        val metadata =
                            user.getJSONObject(
                                "user_metadata"
                            )

                        if (
                            metadata.has("name")
                        ) {

                            userName =
                                metadata.getString("name")
                        }
                    }

                    val intent =
                        Intent(
                            this,
                            NotesActivity::class.java
                        )

                    intent.putExtra(
                        "user_id",
                        userId
                    )

                    intent.putExtra(
                        "user_name",
                        userName
                    )

                    intent.putExtra(
                        "user_email",
                        userEmail
                    )

                    intent.putExtra(
                        "access_token",
                        accessToken
                    )

                    startActivity(intent)

                } catch (e: Exception) {

                    Toast.makeText(
                        this,
                        "Login parsing failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },

            {
                Toast.makeText(
                    this,
                    "Invalid email/password",
                    Toast.LENGTH_LONG
                ).show()
            }

        ) {

            override fun getBody(): ByteArray {

                val json = JSONObject()

                json.put(
                    "email",
                    email.text.toString()
                )

                json.put(
                    "password",
                    password.text.toString()
                )

                return json.toString()
                    .toByteArray(Charsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }

            override fun getHeaders(): MutableMap<String, String> {

                val headers =
                    HashMap<String, String>()

                headers["apikey"] =
                    SupabaseConfig.API_KEY

                headers["Content-Type"] =
                    "application/json"

                return headers
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}