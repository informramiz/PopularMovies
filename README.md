# PopularMovies
A simple app that helps users discover popular, top rated and recent movies.

![animation](docs-data/animation.gif)

## Goal
Goal was to try JSON parsing manually (without any library like GSON), pass Parcelable objects between activities, RecyclerView etc. 

## Instructions for Running Code
The app uses **[TheMovieDB API](https://www.themoviedb.org/documentation/api)** to fetch movies. To fetch movies an **API Key** is needed. If you don’t already have an account, you will need to [create one](https://www.google.com/url?q=https://www.themoviedb.org/account/signup&sa=D&ust=1531283698522000) in order to request an API Key.

After you have created the API key, paste it in your **local.properties** file with name **apiKey** (e.g. `apiKey=YOUR_KEY`). Once you paste the API key in **local.properties** you can run the app code as Gradle will automatically pull your API key.