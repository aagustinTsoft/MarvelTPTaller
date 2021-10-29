package datos

import Modelo.Character
import Modelo.CharactersRepository
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.features.json.JsonFeature

class KtorCharactersRepository2(private val apiClient: MarvelCharactersClient) : CharactersRepository {

    override suspend fun getCharacters(timestamp: Long, md5: String): List<Character> {
        return apiClient.getAllCharacters(timestamp, md5).toModel()
    }

    private fun CharactersResponse.toModel(): List<Character> {
        return this.characters.list.map {
            Character(it.name, it.description, it.thumbnail.toUrl())
        }
    }
}

class KtorCharactersRepository() : CharactersRepository {
    private val httpClient = HttpClient(){
       install(Logging){
           level = LogLevel.ALL
           logger = object : Logger{
               override fun log(message: String) {
                   Napier.v(tag = "Log de prueba", message = message)
               }
           }
       }
        install(JsonFeature){
            val json = kotlinx.serialization.json.Json {ignoreUnknownKeys = true}
            serializer = KotlinxSerializer(json)
        }
    }

    override suspend fun getCharacters(timestamp: Long, md5: String): List<Character> {
        return httpClient.get("https://gateway.marvel.com/v1/public/characters")
    }
}
