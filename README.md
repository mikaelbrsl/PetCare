# PetCare - Aplicativo Android (Kotlin + Jetpack Compose + Room)

Projeto desenvolvido seguindo todos os requisitos funcionais (RF01 a RF22), não funcionais (RNF01 a RNF10) e o protótipo visual (Penpot) da apresentação **PetCare (Time: Mikael e Rogério)**.

## Estrutura de Pastas do Projeto
```text
PetCare_Android/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── app/
    ├── build.gradle.kts
    └── src/main/
        ├── AndroidManifest.xml
        ├── res/values/strings.xml
        └── java/br/edu/ifpb/petcare/
            ├── MainActivity.kt                  # Activity principal + Drawer Modal + NavHost
            ├── data/
            │   ├── model/Entities.kt            # Entidades Room (Pet, Appointment, HealthRecord, PetDocument, PetProduct)
            │   ├── dao/PetCareDao.kt            # Consultas SQL reativas (Flow) e operações CRUD
            │   └── database/PetCareDatabase.kt  # Instância Singleton do banco SQLite/Room
            ├── viewmodel/
            │   └── PetCareViewModel.kt          # Estado reativo das telas e seed dos dados do slide
            └── ui/
                ├── theme/Theme.kt               # Paleta de cores oficial do protótipo (#1F6341, #E57C32, #FBF9F3, #D6E2DC)
                ├── navigation/Screen.kt         # Rotas tipadas da árvore de navegação
                ├── components/CommonComponents.kt # TopBar, botões laranjas, cards de sugestão e filtro de Pet
                └── screens/
                    ├── HomeScreen.kt            # Estado vazio + Lista "Seus pets" (Mingau, Kabeção, Mel, Juju)
                    ├── PetFormScreen.kt         # Cadastro, foto da galeria, edição e exclusão de Pet
                    ├── PetDetailScreen.kt       # Hub centralizado de cuidados de um Pet específico
                    ├── AgendaScreen.kt          # Agendamento de data/hora, consultas veterinárias e histórico concluído
                    ├── MedicationsScreen.kt     # Controle de Vacinas e Remédios por animal
                    ├── DocumentsScreen.kt       # Cadastro e visualização de documentos vinculados ao Pet
                    └── ProductsScreen.kt        # Controle de produtos, preços e lista de reposição
```

## Como abrir e rodar no Android Studio
1. Extraia o arquivo `PetCare_Android.zip` em uma pasta no seu computador.
2. Abra o **Android Studio** (recomendado *Iguana*, *Jellyfish*, *Koala* ou *Ladybug*).
3. Clique em **Open** e selecione a pasta `PetCare_Android`.
4. Aguarde o **Gradle Sync** baixar automaticamente o wrapper e as bibliotecas (Room, Navigation Compose, Coil e Material 3).
5. Selecione um emulador Android (API 26 ou superior) ou conecte seu celular via USB/Wi-Fi Debugging e clique em **Run ('app')**.
