package br.edu.ifpb.petcare.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object PetForm : Screen("pet_form?petId={petId}", "Cadastrar Pet") {
        fun createRoute(petId: Int? = null) =
            if (petId != null) "pet_form?petId=$petId" else "pet_form"
    }
    object PetDetail : Screen("pet_detail/{petId}", "Perfil do Pet") {
        fun createRoute(petId: Int) = "pet_detail/$petId"
    }
    object Medications : Screen("medications?petId={petId}", "Remédios e Vacinas") {
        fun createRoute(petId: Int? = null) =
            if (petId != null) "medications?petId=$petId" else "medications"
    }
    object Products : Screen("products", "Produtos")
    object Agenda : Screen("agenda?petId={petId}", "Agenda e Consultas") {
        fun createRoute(petId: Int? = null) =
            if (petId != null) "agenda?petId=$petId" else "agenda"
    }
    object Documents : Screen("documents?petId={petId}", "Documentos") {
        fun createRoute(petId: Int? = null) =
            if (petId != null) "documents?petId=$petId" else "documents"
    }
}
