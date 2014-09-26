TrailGuide - Project Name Suggestions
=====================================
trailCompanion
trailBuddy
Explore
trailExplorer
BeActive
GetUGo


TrailGuide - Stories
====================

Required User Stories (Brainstorming)

    1. Recommended / Popular Hikes
    2. Trips
    3. Trip Detail (Picture Story and Map views)
    4. Plan My Hike
        a. Story View
        b. Map View
        c. Add a friend (facebook)
    5. Profile page 
        a. list of trips


User Stories
============

Required:

    1.  In the main (opening) activity of the app, user should be able to see a list of hiking stories shared by other app users.
        a.  Each hiking story view in the list should comprise of a cover picture, title and number of likes, and plan reference count.
    2.  User should be able to enter a search query in the action bar.
    3.  User should be able to set preferences for time, level and location (defaults in bold).
        a.  time (5-6 hours/weekend/4-5 days)
        b.  location (near me/reachable/don’t mind another country)
        c.  level (beginner/intermediate/adventurous/extreme/any)
    4.  User can open a hiking story to see a detail timeline of events including pictures, user notes in the timeline view.
    5.  User can open a hiking story to see the map of waypoints (rough path taken) with markers for information (for notes and pictures) in the map view.
    6.  User should be able to click the “Add to my hike” at the bottom of hike story detail view, which takes them to the login dialog.
    7.  If logged in, user should be able to click on the “profile action bar item” to go to their profile.
    8.  User profile comprises of name and profile thumbnail (a random pic like github) of user followed by list of his hikes (list similar to search).
        a.  list example: [New] Day at Vernel falls Yosemite, Brunch at Muir woods.
        b.  On the detail view of user’s personal hike, he should be able to share the hike with an existing app user or an email address.
        c.  On the detail timeline view of the user’s new personal hike
              i.  User should be able to edit hike title
             ii.  User should be able to add another app user (the hike will get added to my hikes of the other user, as a new hike).
            iii.  User should be able to click start hike and end hike.
             iv.  Should be able to add a pic from the gallery.
              v.  Should be able to add a comment.
             vi.  Should be able to click “Create story”
        d.  On the map view, user can see the rough paths and information points of the hikes he added to his plan (max 2).
    9.  After clicking “Create Story”, user should get a notification when the story is created on the server and that should take the user directly to the hike detail story page. Now the details page map view comprises of just his map and pics and not the ones used in planning.

Nice to have (optional):
========================

    1.  The main activity is divided into two sections:
        a.  My hikes (a carousel view of cover photos of user’s personal hikes)
        b.  Recommended (list of recommended hikes as in required stories)
    2.  The hiking story view in list should be a carousel of random pictures, other updates such as comments, etc. from the hike.
    3.  User profile has a status and user medal
        a.  User should be able to upload a profile picture.
        b.  Status - hiking “title of hike”/planning (if he has plans)/inactive
        c.  Medal/Level - beginner/intermediate hiker/climber/moutaineer/pro/beach boy/ready for everest, etc. (other fun levels, based on the hikes they undertake).
    4.  User should be able to get notifications during the hike about nearby scenic spots.
    5.  User profile page should have a big “Create new hike story” button at the bottom which takes him to the gallery and lets him select start and end pictures.
    6.  User should be able to edit a hike story with comments, tags, hashtags, etc.

Oleg's stories:
===============

User story 1 (explore)
Required:

    1. Search trip DB based on location and duration
    2. Browse results and annotate favorite trips (with user pictures and notes) on common map (similar to evertrail format)
    3. Delete trips from the final map
    4. Save offline map with trips annotated on the phone

Optional:

    1. Edit the map and delete selected end points (pics/notes)
    2. share the trip using facebook
    3. Invite others to provide feedback/participate in the trip
    4. msgboard (I think not a required feature).

User story 2 (on the go)
Required:

    1. Start app at the start point (GPS will run and sample location every 3 minutes)
    2. take pictures and store on the phone (location sampled)
    3. take notes and store on the phone (location sampled)
    4. Display marker showing current location

Optional:

    1. Start spontaneous trip - create a new trip with a map of surrounding area based on projected duration of the trip
    2. Notification when getting close (0.5miles)  to the “landmark” (previously annotated note or a pic)
    3. Control GPS sampling time
    4. Update location button
    5. Feeling lucky (get one trip and go based on location, weather, difficulty, time, etc)


User story 3 (after)
Required:

    1. Update the trip (upload photos and notes to the existing trip
    2. Annotate the track, pictures and notes onto the existing map
    3. Invite others to check the trip (email, sms, whatsup, facebook?)

Optional:

    1. Upload the spontaneous trip


