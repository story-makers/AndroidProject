TrailGuide - Description
========================

We are avid hikers and travelers. We like to log and note interesting moments of our experience by means of pictures. Currently every user takes lots of pictures, shares a few with others and may be note location with social networks. This setup seems very sparse and does not capture the true experience of entire hike. 

Our app tries to provide a simple way to annotate users experience with help of notes, pictures and location. Once created the entire hike can be shared with friends on social network and also by means of web site. We envision that an immersive experience with photos, location and user notes in a fluid UI helps tell the story the way user experienced. 

The app is primarily focused on making it very easy to create, discover and enjoy the experiences with other friends and for personal consumption as well. Additionally we aim to make the shared stories available for fellow hikers to discover new places and follow along to enjoy the same. 

  

TrailGuide - Project Name Suggestions
=====================================

  * trailCompanion
  * trailBuddy
  * Explore
  * trailExplorer
  * BeActive
  * GetUGo



TrailGuide User Stories
=======================

Required:

    1.  [Exploring shared trips]: In the main (opening) activity of the app, user should be able to see a list of hiking stories shared by other app users.
        a.  Each hiking story view in the list should comprise of a cover picture, title and number of likes, and plan reference count.
    2.  [Search trips] User should be able to enter a search query in the action bar.
    
    4.  [Hike in detail] User can open a hiking story to see a detail timeline of events including pictures, user notes in the timeline view.
    5.  [Hike in detail] User can open a hiking story to see the map of waypoints (rough path taken) with markers for information (for notes and pictures) in the map view.
    6.  [Hike in detail] User can save hike offline for use on device for referring while going on a hike.
    7.  [Create my hike] User should be able to click the “Add to my hike” at the bottom of hike story detail view, which takes them to the login dialog(if required).
    8.  [Create my hike] After clicking “Create Story”, user should get a view when the story is created on the server and that should take the user directly to the create hike detail story page. 
        a. The details page view comprises of timeline of notes, photos, locations
        b. User should be able to edit hike title
        c. [Scoped out] User should be able to add another app user (the hike will get added to my hikes of the other user, as a new hike).
        d. User should be able to click start hike and end hike.
        e. User can select photos from gallery or take one right from the interface.
        f. User can add notes with a location in the timeline 
        g. Data generated for this trip will be locally saved and have option to edit/review items until its published
        h. Upon publishing the data is uploaded to server and available for sharing or in search lists of other users.
    9.  [Profile view] If logged in, user should be able to click on the “profile action bar item” to go to their profile.
    10. [Profile view] User profile comprises of name and profile thumbnail (a random pic like github) of user followed by list of his hikes (list similar to search) including saved hikes.
        a.  list example: [New] Day at Vernel falls Yosemite, Brunch at Muir woods.
        b.  On the detail view of user’s personal hike, he should be able to share the hike with an existing app user or an email address or facebook.
        c.  On the map view, user can see the rough paths and information points of the hikes he added to his plan (max 2).
    
    

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
    7.  (Ref: Required:2) User should be able to set preferences for time, level and location (defaults in bold).
        a.  time (5-6 hours/weekend/4-5 days)
        b.  location (near me/reachable/don’t mind another country)
        c.  level (beginner/intermediate/adventurous/extreme/any)
    8. Add/Invite friends from facebook and twitter to participate in group hike creation
    9. Add option in create my hike view add detail gps locations, which will sample location from GPS at a sparse interval (ex. 3 mins). This will help create detail trail map along with the timeline.


Mockups (work in progress):
===========================

    1. Home screen showing list of hikes with likes and title and cover photo
    
![Home screen](./mockups/guide_home.png)

![Hike Detail screen](./mockups/hike_detail.png)

![Profile screen](./mockups/profile.png)




