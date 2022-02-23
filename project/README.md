#Templer
### Video Demo: <https://www.loom.com/share/71a62488c8c147b2999cb9dfe2e4805b>
### Description:

   My CS50 final project is Templer: a Java application utilizing CS50 concepts such as Arrays and Algorithms as well as
new concepts like JavaFx, polymorphism, and inheritance. My program is a game where a player must try to survive as long
as possible: avoiding randomly spawned enemies, obstacles, walls, and super obstacles with the help of LifeGivers that grant
one health point. I programmed the enemies to randomly attack the player. In order to make the game easier on
the player, I made sure the enemies, obstacles, and super obstacles disappear if they go off the screen or collide with
each other. Aside from the gameplay, I added a scoring element to the game. The player gains a certain amount of points
every couple seconds of survival and loses points on collisions with the deterrents. The scoring system also reflects the
player's loss or gain of health points. The game displays all of this information including a timer in a toolbar as it is
played and shows the information on a new scene using JavaFx attributes when the game ends.

   My project includes the files GameGUI.java and GameLogic.java, which act as logic and graphical controllers.
The file Main.java launches the application into a popup window on the client's screen. Finally, the classes Ball.java,
LifeGiver.java, StrongerObstacle.java, and Obstacle.java are classes defining the different characters in my game which
all inherit from Mob: the standard character class. I organized my files as such to avoid repeating code and showcase
my knowledge of polymorphism and inheritance. From character to character, methods are overrided within the character
classes and used to make a new element to the game with a completely different purpose. My GameLogic file handles the
logic and rules of the game. It uses timers to handle scoring and even adds functionality for making the player flash
green if it collides with anything. Finally, my GameGUI allows the player to move using the WASD keys, renders everything
to the screen, and adds a game over screen. My project structure allows my program to have separated logic and graphical
components, more efficiency when adding more deterrents, and allows for easy customization.

   When I was planning out my project, I debated making several changes. For example, I had trouble finding out how
to create a class hierarchy for all the mobs in the game. I thought of having two hierarchies for the ball mobs and two
for the rectangular obstacles, but ended up with my current, simpler design. Furthermore, I debated getting rid of the
timers in my code and just relying on the chance of deterrents spawning to make the code simpler. However, I learned that
this made the game too hard with too many characters on the screen at once. Thus, I went ahead with the characters having
a chance to spawn every so often. Lastly, I thought of letting all characters remain in the canvas on collisions as
the logic for handling collision detection and execution was difficult for the LifeGivers. Because the LifeGivers could
also collide with among one another, they would give each other health as my code tried to check if they had lost a point.
This meant that they would remain on the screen unless they were hit by another character until they had 0 health. Fortunately,
I was able to fix this error and maintain my plan for the project.

    To run my code, download the latest version of a javafx sdk from the internet and be aware of where you download the sdk to.
Then, download my project and open it in IntelliJ. After doing so, click on File, then project structure, and navigate to the section,
"Project". Make sure that the project sdk is the version one you downloaded. If not, select the version you downloaded. Then, go to the
"Libraries" tab and delete everything in the "Classes" and "Native Library Locations" tab. Then click the plus button and find the location of your
javafx sdk. Inside the sdk folder, find the "lib" folder of the correct operating system you are running the program on. Finally, select the lib folder within and press open.
The project should be ready to go! Just go to the Main class and click the green play button next to the class titled, "Main". If issues are arising, quit IntelliJ and reopen the project