# Product Catalog & Offline Cart

A Kotlin Android app for browsing products from DummyJSON and managing a persistent cart.

## Features

- Product grid with images, price, rating, loading, empty, error, and retry states.
- Debounced product search backed by DummyJSON's search endpoint.
- Product details with image gallery and stock information.
- Add, increase, decrease, and remove cart items, with totals calculated from saved items.
- Cart storage in Room, available offline and after app restarts.

## Technology

Kotlin, Android SDK, Jetpack Compose, Material 3, MVVM, Coroutines/Flow, Retrofit, OkHttp, Room, Hilt, Navigation Compose, and Coil.

## Architecture

`presentation` depends on domain use cases; domain contains models, use cases, and repository interfaces; `data` implements repositories and contains Retrofit DTOs, Room entities, and explicit mappers. Hilt supplies API, database, repositories, and use cases. Navigation passes product IDs, and ViewModels expose immutable `StateFlow` UI state.

### Package structure

- `domain/model`, `domain/repository`, `domain/usecase`
- `data/remote`, `data/local`, `data/mapper`, `data/repository`
- `di`
- `presentation/product`, `presentation/details`, `presentation/cart`, `presentation/navigation`, `presentation/components`

### Offline cart

The cart path is Compose → `CartViewModel` → cart use cases → `CartRepository` → Room DAO/database. Cart writes use Room transactions for read/modify/write operations. Quantity is constrained to 1–999; decrementing a single item removes it. The database stores item fields and quantity only. Item count and total price are derived from observed rows using integer cents. Cart operations do not call the network.

### Search and errors

Search waits 350 ms after edits, removes duplicate trimmed queries, and uses `flatMapLatest` so a newer query cancels the previous request and its pending debounce. Clearing the query loads the full product list. Repository boundaries map transport, HTTP, malformed-data, and storage failures to app errors; screens show translated messages and retry actions.

### API

The app uses `https://dummyjson.com/` for product listing, search, and details. Prices are rounded to USD cents at the API boundary.

## Build and run

Open the project in Android Studio with JDK 21 and Android SDK 36 installed, or run:

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:test
```

Run the app on Android 7.0 (API 24) or later. Product browsing requires internet access; cart viewing and edits work offline.

## Testing

Unit tests cover domain money/cart calculations and product repository mapping for successful, empty, malformed, and I/O-failure responses. The project currently has no Room persistence or ViewModel tests.

## Decisions and trade-offs

- The cart snapshots the product name, price, and image when added, so it remains readable offline and its total does not shift when the catalog changes.
- Money uses integer cents to avoid floating-point total errors.
- Product data is fetched on demand and is not cached; offline support is scoped to the cart.
- USD is used because it matches the source API's price values.
