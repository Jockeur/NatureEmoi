package fr.jockeur.naturecollection

class PlantModel(
    val id: String = "plant0",
    val name: String = "Tulipe",
    val description: String = "Petite Description",
    val imageUrl: String = "http://graven.yt/plant.jpg",
    val grow: String = "Lente",
    val water: String = "Faible",
    var liked: Boolean = false
)