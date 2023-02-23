package io.github.bersoncrios.minutesapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.github.bersoncrios.minutesapp.databinding.ActivityCreateMinuteBinding
import io.github.bersoncrios.minutesapp.models.Minute
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class CreateMinuteActivity : AppCompatActivity() {

     lateinit var binding: ActivityCreateMinuteBinding

    var currentUserId: String = ""
    var currentUserName: String = ""

    //Firebase
    lateinit var auth: FirebaseAuth
    lateinit var user: FirebaseUser

    //Firebase Firestore
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var storageReference: StorageReference

    var collectionReference: CollectionReference = db.collection("Minutes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_minute)

        storageReference = FirebaseStorage.getInstance().reference

        auth = Firebase.auth

        binding.btnFinishMeet.setOnClickListener {
            finishMeet()
        }
    }

    private fun finishMeet() {

        val local: String = binding.etLocal.text.toString().trim()
        val horarioInicio: String = binding.etHoraInicio.text.toString().trim()
        val horarioFim: String = binding.etHoraFim.text.toString().trim()
        val data: String = binding.etData.text.toString().trim()
        val conteudo: String = binding.etContent.text.toString().trim()

        binding.progressBar.visibility = View.VISIBLE

        val minute = Minute(
            local,
            horarioInicio,
            horarioFim,
            data,
            conteudo,
            auth.currentUser?.uid.toString(),
            auth.currentUser?.displayName.toString()
        )
        collectionReference.add(minute)
            .addOnSuccessListener {
                Log.d("MINUTE -- - - -- --", "finishMeet: Minute add")
                binding.progressBar.visibility = View.INVISIBLE
                startActivity(Intent(this, HomeActivity::class.java))
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(this, "Falha ao salvar sua reunião", Toast.LENGTH_LONG ).show()
                Log.d("MINUTE -- - - -- --", "finishMeet: fail to add minute")
            }

    }
}