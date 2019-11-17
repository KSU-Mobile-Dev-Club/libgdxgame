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

I've done this on a Macbook, a KSU lab machine, and a Windows computer. All the error's I've encountered are addressed by everything above. Hopefully you don't encounter any new ones. 

## Run demo
Right click the "desktop" project in the Package Explorer (left side of screen, use Quick Access if the pane got closed) Then ``` (Run as-> Java application) ```


