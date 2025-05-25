# GameHub

Projeto para exibir um catálogo de Jogos!

## API Utilizada

- **Nome:** RAWG Video Games Database API
- **Link:** [https://rawg.io/apidocs](https://rawg.io/apidocs)

## Endpoints Utilizados

### 1. Listar Jogos
- **Endpoint:** `/games`
- **Descrição:** Retorna uma lista paginada de jogos, podendo ser filtrada e ordenada por diversos parâmetros.
- **Principais parâmetros usados:**
  - `key`: Chave de API (obrigatório)
  - `page`: Página da listagem
  - `page_size`: Quantidade de itens por página
  - `search`: Termo de busca (opcional)
  - `ordering`: Ordenação (ex: `-rating` para maiores ratings primeiro)

#### Exemplo de requisição
```http
GET https://api.rawg.io/api/games?key=SUA_API_KEY&page=1&page_size=20&search=gta&ordering=-rating
```

#### Exemplo de resposta (resumido)
```json
{
  "count": 12345,
  "results": [
    {
      "id": 3498,
      "name": "Grand Theft Auto V",
      "released": "2013-09-17",
      "rating": 4.47,
      "background_image": "https://media.rawg.io/media/games/456/456dea5e1c7e3cd07060c14e96612001.jpg",
      "short_screenshots": [
        { "id": -1, "image": "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe22d035c0ebe64.jpg" },
        { "id": 1827221, "image": "https://media.rawg.io/media/screenshots/a7c/a7c43871a54bed6573a6a429451564ef.jpg" }
      ]
    }
  ]
}
```

### 2. Detalhes de um Jogo
- **Endpoint:** `/games/{id}`
- **Descrição:** Retorna detalhes completos de um jogo, incluindo descrição, imagens, avaliações, etc.
- **Principais parâmetros usados:**
  - `key`: Chave de API (obrigatório)

#### Exemplo de requisição
```http
GET https://api.rawg.io/api/games/3498?key=SUA_API_KEY
```

#### Exemplo de resposta (resumido)
```json
{
  "id": 3498,
  "name": "Grand Theft Auto V",
  "description": "<p>Rockstar Games went bigger...",
  "background_image": "https://media.rawg.io/media/games/456/456dea5e1c7e3cd07060c14e96612001.jpg",
  "rating": 4.47
}
```

## Trechos de Código - Consumo da API

### Interface de Serviço (Retrofit)
```kotlin
interface RAWGService {
    @GET("games")
    suspend fun getGames(
        @Query("key") apikey: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 40,
        @Query("search") search: String? = null,
        @Query("ordering") ordering: String? = null
    ): GameResponse

    @GET("games/{id}")
    suspend fun getGameDetail(
        @Path("id") id: Int,
        @Query("key") apikey: String
    ): Game
}
```

### Instância Retrofit
```kotlin
object RetrofitClient {
    private const val BASE_URL = "https://api.rawg.io/api/"
    val service: RAWGService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RAWGService::class.java)
    }
}
```

### Exemplo de chamada para listar jogos (ViewModel)
```kotlin
fun loadGames(apiKey: String, page: Int = 1, search: String? = null, ordering: String? = null) {
    viewModelScope.launch {
        try {
            val response = RetrofitClient.service.getGames(apiKey, page, 40, search, ordering)
            _games.postValue(response.results)
        } catch (e: Exception) {
            e.printStackTrace()
            _games.postValue(emptyList())
        }
    }
}
```

### Exemplo de chamada para detalhes de um jogo
```kotlin
val game = RetrofitClient.service.getGameDetail(gameId, "SUA_API_KEY")
```

---

Para mais detalhes sobre a API, consulte a [documentação oficial da RAWG](https://rawg.io/apidocs).

