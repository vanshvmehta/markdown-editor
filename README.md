## Releases
// 4/10/2023
// Version : 4.0.0
- Added functionality to login for unique users
- Added ability to save, delete, and browse files for unique users
- Added functionality for settings to persist across users
- Added more themes (Quiet light and Night Blue)
- Added guest login
- Added functionality for multiple tabs

// 3/24/2023
// Version : 3.0.0
- Added keyboard shortcuts
- Added functionality for: Undo, redo, cut, copy, paste
- Implemented/separated: 'Save' and 'Save as'

// 3/10/2023
// Version : 2.0.0
- Added themes (dark mode and light mode)
- Added functionality to compile and display valid markdown text
- Added file directory pane for files
- Added data persistence for user settings


// 2/16/2023
// Version : 1.0.0
Baseline functionality
- A resizable application
- Toolbars
- Buttons for basic Markdown syntax bold, italics etc.
- Text pane to type
- Display pane and compile button
- Ability to open .txt files
- Ability to save .txt files
- File directory pane

## Running different releases
If you want to run different releases, it is highly recommended that you navigate to the user you are logged in to on
your computer, and delete the '.Markdown' folder. For example, on a Windows an example path would be 'C:\Users\simon'.
The '.Markdown' folder will be there. It may be hidden because of the prefix, so you may have to show hidden files.
The application may crash otherwise, due to updates to the internal structure. 

- Delete the .Markdown/config.txt before you test a new release, since config.txt from a different release would crash the application.

## How to Run the Application
You don't need to do anything to connect to our webservice, it should connect automatically.

When you first open the program, you will get a 'User Login' window. You can input a username and password that we
verify to login. If you want to make a new account, then just put in a username (that hasn't already been taken) and
your choice of a password. There is a 'Sign-out' button in 'File' that takes you back to the login manager.
Some already created users are:

Username: bob
Password: cs346
Username: jane
Password: jane

Logging in will set your settings to your previously saved settings (or default if you're a new user).
It will also download a directory of the user's files (more on this later).

Most of the basic functionality (typing, shortcuts, undo/redo, cut/copy/paste) is relatively self-explanatory. You can
also hover the common markdown buttons (Bold, Italics, Heading, etc.) to get the shortcuts. 

Anytime you edit in the middle text area, it automatically compiles onto the right pane!

After you select a file from the directory, the Parent Folder Tree of the selected file would be shown on the left of the text area.
You can open different files by simply selecting them, use 'Ctrl + S' to save the content before changing files.

We implemented the ability to have multiple files open using tabs. Simply click on the '+' button, or 'Ctrl + N' to
open new tabs. Close them with the 'X' button. Unsaved files in tabs will give you a warning when you try to close them.

Our toolbar also allows you to change the font and font size for the editing/compiled window. Just a heads up, certain
fonts don't have an 'italics' or 'bold' mode. The font 'system' is one of these. If you would like to test these
functions, a good font we can recommend is 'Arial'.

Themes can be accessed in View -> Themes -> (Theme of your choice).
 - You don't need to set your preferences, changes to the window size/themes are automatically saved across sessions
 for each unique user.

You can save, save-as, open files as normal using the buttons/shortcuts. When you attempt to select a file after you
click these buttons, they will default to a special directory on your computer that we downloaded earlier. This
directory is a copy of your remote one. If you save, save-as, or delete (using our buttons in the application) any of the
files inside this directory, the changes will be reflected in your remote directory! You can also use the save,
save-as, open files outside of this directory. These changes won't affect your remote directory, and are just local to
your computer.

We also display the file directory of files you opened/save-as in the left window. You can click on '.txt' and '.md'
files inside this tree to open them.

Enjoy! :)
