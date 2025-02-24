## Weatherlords

Welcome to weatherlords: a weather based resource development game. As the weatherlord, it is your
task to employ capitalism to reach currency past the integer limit.

## Try it out!

You can try out this project by cloning it and building it locally, or head over to the [releases]()
to download the `.jar` file.

```bash
$ git clone https://github.com/FireStreaker2/Weatherlords.git
$ cd Weatherlords
$ ./gradlew lwjgl3:run
# alternatively:
$ ./gradlew lwjgl3:dist
```

## Inspiration

We wanted to make a game that aligned with the given prompt, which was weather/climate related. Out
of all the possible game styles that we brainstormed, we felt like a civilization game was not only
the fastest to complete and make high quality, but also the most representative of our ideas.

## What it does

Weatherlords is a resource development game, using buildings (farms, mines, etc.) paired with the
power of weather in order to get currency and beat the game.

## How we built it

We initially decided on using Java as our primary language, as it was the most commonly known among
us. Moving forward, we discovered the library [libgdx](https://libgdx.com/), which was a popular "
cross-platform Java game development framework based on OpenGL". Moreover, we also decided to use
the well known IDE [Android Studio](https://developer.android.com/) for our main development, as it
paired well with the library we were using.

## Challenges we ran into

* Time constraints - since we had many plans for our game (ex. backend integration, session IDs,
  multiplayer, fancy graphics, etc.), it took a lot of time to properly choose the ideas we wanted,
  and then further implementing the ones we chose.
* Lack of experience - while we all had experience with Java itself (5 in APCSA), we were limited by
  not knowing the library at all; we had to completely learn (most of) the API within a couple of
  days, and then actually use it. Moreover, some of our team was not used to other technologies,
  such as Android Studio and Git.
* Sick member - part of our team was sick during this long break, leading to less productivity!
* Ambiguous error messages while working with libgdx within Android Studio

## Accomplishments that we're proud of

* The fact that we actually made a game!
* Being able to learn a completely new library very fast
* Efficiently working together
* Good enough graphics to be appealing to the eye

## What we learned

* How to use the new technologies in our tech stack (libgdx, Android Studio, git)
* Sort through feature as a team to meet the deadline
* How to work together more efficiently than everybody force pushing to `master`

## What's next for Weatherlords

We plan on further developing Weatherlords; first with a backend implementation to support
multiplayer; secondly polishing everything else. Below is a sample idea of what we were initially
planning to add, but had to cut due to time constraints:

```
 - Add feature where people can invest any point in the game, but only continuous investment will allow for greater development of weather facilities which grants more information
 - Add other resources like power and minerals
 - Add workers which are people who work on the facilities to generate resources
      - Provide benefit where the player doesn't have to be on the farm
      - Also some food from farms are given to workers
 - Add research facilities which create technologies from resources like power and minerals to improve farming and weather conditions
 - Add make some specific weather conditions to islands

Backend implementation:
1) It is a multiplayer game?
2) player registers with the game (provide a way to register)
  - Can we do password-based?
3) After player completes, we show Top 'X' players and current position of the player. Players position should be highlighted.
4) Backend storage is database.
  - Store player details 
  - past game stats? at least top 'X' positions
  - current game stats
5) Backend Service:
   - A java program (can be another programming language) with database connectivity supports.
   - Acts like a service and has core logic. 
   - Java clients from users laptops connects to backend service and interacts.
   - Client send user names/password to get information from backend service to display current user details.
   - Client sends new game start info to backend to create java session for new game and returns back client for communication.
   - Client send game decision as activityinfo to backend with session id and backend service will map session id and takes appropriate action and send result back to client.
   - Clients refresh to interface based on session changes.
   - Finally clients send all activity completed status and backend service compares current result with previous best result of user and updates it if new result is better.
   - backend send top 10 results and include extra entry for users if the user is not in top 10 for displaying current standing.
   - Current standings can be displayed at any time by user.
   - Bonus: Retain in game java session id and resume game if user client interface on client laptop is closed, reopened the browser, gone through authentication and try retrieve previous current in-game to CONTINUE.
   - No backtrack of activities which were already done
```

## Contributors 
* [FireStreaker2](https://github.com/FireStreaker2) - Lead Developer
* [OPWinner](https://github.com/OPwinner) - Lead Designer, Developer
* [YeOldBeasts](https://github.com/DragonMaster12345678) - Designer

## License
[MIT](https://github.com/FireStreaker2/Weatherlords/blob/main/LICENSE)
