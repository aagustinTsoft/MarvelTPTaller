package datos

import Modelo.Character
import Modelo.CharactersRepository

class RetrofitCharactersRepository(private val apiClient: MarvelCharactersClient) :
    CharactersRepository {

    override suspend fun getCharacters(timestamp: Long, md5: String): List<Character> {
        return apiClient.getAllCharacters(timestamp, md5).toModel()
    }

    private fun CharactersResponse.toModel(): List<Character> {
        return this.characters.list.map {
            Character(it.name, it.description, it.thumbnail.toUrl())
        }
    }
}

