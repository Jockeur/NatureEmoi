package fr.jockeur.naturecollection.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import fr.jockeur.naturecollection.MainActivity
import fr.jockeur.naturecollection.PlantModel
import fr.jockeur.naturecollection.PlantRepository
import fr.jockeur.naturecollection.PlantRepository.Singleton.downloadUri
import fr.jockeur.naturecollection.R
import java.util.*

class AddPlantFragment(
    private val context: MainActivity
) : Fragment() {

    private var file: Uri? = null

    private var uploadedImage: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_plant, container, false)

        // récupérer uploadedImage pour lui associer son composant
        uploadedImage = view.findViewById(R.id.preview_image)

        // récupérer le boutton pour charger l'image
        val pickupImageButton = view.findViewById<Button>(R.id.upload_button)

        // lorsqu'on clic on ouvre les images du téléphone
        pickupImageButton.setOnClickListener{ pickupImage() }

        // récupérer le boutton confirmer
        val confirmButton = view.findViewById<Button>(R.id.confirm_button)

        confirmButton.setOnClickListener {
            sendForm(view)
            context.loadFragment(HomeFragment(context), R.string.home_page_title)
            context.setSelectedTab(R.id.home_page)
        }

        return view
    }

    private fun sendForm(view: View) {

        // héberger sur le bucket
        val repo = PlantRepository()
        repo.uploadImage(file!!) {
            val plantName = view.findViewById<EditText>(R.id.name_input).text.toString()
            val plantDescription = view.findViewById<EditText>(R.id.description_input).text.toString()
            val grow = view.findViewById<Spinner>(R.id.grow_spinner).selectedItem.toString()
            val water = view.findViewById<Spinner>(R.id.water_spinner).selectedItem.toString()
            val downloadImageUrl = downloadUri

            // créer un nouvel obet PlantModel
            val plant = PlantModel(
                UUID.randomUUID().toString(),
                plantName,
                plantDescription,
                downloadImageUrl.toString(),
                grow,
                water
            )

            //envoyer en bdd
            repo.insertPlant(plant)
        }
    }

    private fun pickupImage() {
        val intent = Intent()

        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        this.startActivityForResult(intent, 47)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 47 && resultCode == Activity.RESULT_OK){

            // vérifier si les données ne sont pas null
            if (data == null || data.data == null) return

            // récupérer l'image
            file = data.data

            // mettre à jour l'aperçu
            uploadedImage?.setImageURI(file)
        }
    }

}