# Unix and Git stuff
Some of you guys requested a small cheat sheet of common linux and git commands. Here's a short list of the essentials. 
## Navigation
`cd`**change directory**

`mkdir`**make directory**
- `mkdir newFolder`
- `cd newFolder`

`ls` **list files/directories**

`pwd` **print working directory** "show me where I am"

`cp` **copy** 
- `cp Drop.java DropCopy.java`

`mv` **move file/folder**
 - `mkdir bird_pictures`
 - `mv FlappyBird.png bird_pictures\flappybird.png`
 - *notice how this can also be used to rename files*
 
USE TAB. Tab will autocomplete filenames for you, so you don't have to type everything. 

Try to wrap your head around the similarity between cp and mv. They're essentially the same command, except with mv, you don't leave a duplicate copy laying around. 

There are tons of other usefull commands, but these are the bare essentails. 

## Git
First of all, you can't really run any git commands until you're in a folder that is tracked by Git. In other words, use the above navigation commands to find your Git project on your computer. You'll know when you get there because git bash will start displaying text like (MASTER), and `git status` will actually have something to tell you. 

`git status` is your friend. It tells you the status of your **local** copy of the Git project, which can be a lot of things. Instead of attempting to explain all the possible scenarios, I'll provide a really simple example of a workflow. 

**let's pretend you changed some files, saved them, and are ready to publish your changes...**

` git status` will show you what has changed. Right now, the changes should be in red, because you haven't *added* them for commit.

`git add <file>` or `git add .` (the . will add everything in this folder)

`git status` will now show the changes in green. Any subsequent changes you create will still be red until you manually add them with `git add`

`git commit -m "Explain what you changed here, so your teammates will know."`
Now your change is *commited*. 

`git log` will show you a history of everyone's commit. you should see yours at the top

`git push` will take your commit and push it online. Git may ask you to sign in. 

other people can now run `git pull` and pull down your previous commit.

## Vim world
If you'r ever in Vim world, and don't know what the hell is going on, hit ESC.
Then type `:q` and you'll get your terminal back :).


# How to clone

## Requirements
- Java Development Kit (JDK)

    The lab machines already have this installed, and you probably do too if you've taken CIS200.
    You can check what version you have by opening command prompt (search 'cmd' in Windows) and issuing 

    ``` java -version ```

    I had some serious problems with JDK 13. Long story short, I had to uninstall JDK 13 and revert to JDK 8. You may have to do this as well if you encounter bizarre gradle issues. 
    I recommend using [AdoptOpenJDK](https://adoptopenjdk.net/) instead of Oracle's website . 

- Android SDK

    You can use Android Studio to install this if you don't already have it. You'll also need to know where the SDK was installed, and Android Studio will show you where it is:

    ( open Android Studio) ``` Tools -> sdk manager  ```

- create ANDROID_SDK_ROOT system variable (Windows only)
    
    Open the Windows search and look for "edit system environment" and open the the first result. 

    ``` Environment variables... -> New.. ->   ```

    ```  Variable name: ANDROID_SDK_ROOT```

    ```  Variable value: <copy SDK path from Android Studio (see above)>```
    
- Accept the Android SDK licenses (Windows):

    This is easy. Just re-open command prompt and issue:

    ``` cd /d "%ANDROID_SDK_ROOT%/tools/bin" ```

    ``` sdkmanager --licenses ```

    Follow the prompts to accept the licenses.

- [Git](https://git-scm.com/):

    Lab machines already have Git and Git Bash. You may have to install these on your personal machine. 
    Mac users can just use Terminal instead of Git Bash. 

## Clone repository 
Open git bash. Use "cd" to navigate to a directory that you want to save the project into, if using a lab machine, make sure it's on the U drive or whatever. Example:

``` cd Desktop/ ```

``` git clone https://github.com/KSU-Mobile-Dev-Club/libgdxgame.git ```

Don't download as a zip! You won't be able to push or pull changes. Use the above commands to clone the repo.
## Importing to Eclipse

Almost done! If something goes wrong in these next steps, the "Console" tab at the bottom will give you more detailed information.


``` File-> Import... -> Gradle -> Existing Gradle Project ```

Browse for the 'libgdxgame' directory that you just cloned.

If importing fails, you will have to delete 
    ``` (File->Delete) ``` the project from the workspace (uncheck "delete from disk" or you will have to re-clone it) before trying again.

I've done this on a Macbook, a KSU lab machine, and a Windows computer. All the errors I've encountered are addressed by everything above. Hopefully you don't encounter any new ones. 

## Run demo
Right click the "desktop" project in the Package Explorer (left side of screen, use Quick Access if the pane got closed) Then ``` (Run as-> Java application) ```


