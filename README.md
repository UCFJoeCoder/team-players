# team-players

My 2nd Kotlin app. (My first being the first pass at this app, but nearly everything in one file and without a database)

This app is being created to learn more about Kotlin, Jetpack Compose, Room databases, NavController, ViewModels and Dependency Injection.

Purpose of the app
I watched a kids' American football game and saw someone on the sidelines recording which players from a team were in each play.  
Their goal was to make sure that each player was in at least 8 plays before the game ended.
It was hectic for them to find the players on the page in time... and with timeouts in the game sometimes they would have to undo a tally of a player.

So this app will store a list of Teams, Players, and football games.  Playes belong to a team. Games will have one team with players.

I decided that when a game is created for a team... a snap shot of the list of players is added to that specific game. This allows for the players on a team to change 
without affecting a past game's data.

Design is stored in PowerPoint file
