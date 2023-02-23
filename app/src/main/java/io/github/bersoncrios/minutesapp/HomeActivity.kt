package io.github.bersoncrios.minutesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import io.github.bersoncrios.minutesapp.databinding.ActivityHomeBinding
import io.github.bersoncrios.minutesapp.models.Minute
import java.time.LocalTime

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding

    //firebase references
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var user: FirebaseUser
    lateinit var storageReference: StorageReference
    var db = FirebaseFirestore.getInstance()

    lateinit var minutesList: MutableList<Minute>
    lateinit var adapter: MinutesAdapter

    var collectionReference: CollectionReference = db.collection("Minutes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.addMinute.setOnClickListener{
            startActivity(Intent(this, CreateMinuteActivity::class.java))
        }

        firebaseAuth = Firebase.auth
        user = firebaseAuth.currentUser!!

        //Recycler View
        binding.minutesList.setHasFixedSize(true)
        binding.minutesList.layoutManager = LinearLayoutManager(this)

        //Posts arrayList
        minutesList = arrayListOf<Minute>()
    }

    //Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_signout -> {
                if (user != null && firebaseAuth != null) {
                    firebaseAuth.signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()

        collectionReference.whereEqualTo("userId", user.uid )
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty){
                    Log.d("TAG", "Elements: ${it}")

                    for (document in it){
                        var minute = Minute(
                            document.data["local"].toString(),
                            document.data["horaInicio"].toString(),
                            document.data["horaFim"].toString(),
                            document.data["data"].toString(),
                            document.data["pauta"].toString(),
                            document.data["userId"].toString(),
                            document.data["username"].toString(),
                        )

                        minutesList.add(minute)
                    }

                    //Recyclerview
                    adapter = MinutesAdapter(
                        this, minutesList,
                        MinutesAdapter.OnClickListener { minute ->
                            Toast.makeText(applicationContext, "${minute}", Toast.LENGTH_SHORT).show()

                        }
                    )

                    binding.minutesList.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Não foi possivel carregar as atas", Toast.LENGTH_LONG).show()
            }
    }
}