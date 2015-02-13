# Android_My_Video_Games_V2
This is a more concise version of the Android My Video Games app written in my repository. It eliminates redundant code and works to simplify things

Improvements from Version 1.0:

-Replaced raw database entry with a Content provider, allows for easier access to the data and allows for exporting out of the app.

-Changed the way data is passed between activities. Before, all data was passed via a list and had to stay in specific order (IE first was the game ID, second was the alias, etc), but now, the data is accessed directly via the content provider and allows for any developers who use it to not have to specify a specific order.

-In the ListFragment class, it runs quicker/ smoother now due to less cycles through for-loops. It also now has custom classes of the following types: Buttons, longPress buttons, Checkboxes, and Rating Bars. These all implement the listener and allow for less cluttered code, more Object-oriented-style programming, and an overall cleaner look.

-Added new check for first time run to fill the database as opposed to the old "check if there are no items", which prevents users from deleting all the records and having 3 magically appear

-Added public static classes for direct access to the Content provider and allow for different style returns

-In the AddNewGame Fragment, setup the class to remove much of the dependencies and redundancies. The class is now setup where actions are not all nested together (IE A calls B, which calls C, which Calls D, which finishes), the actions now work more independently. Removed the redundant Async classes and made 2 async classes which handle the server calls and database entries. Moved the deserialize method from GSON into this class and made it static/ public. Added new error checking for Strings returned from the server in case a null or badly formatted URL is returned. 

-Added a check for network connectivity (WiFi, 3g, 4g) before running web searches to prevent timeouts

-Deleted unused /erronous classes and packages and combined ones that were small enough to not be alone. 
