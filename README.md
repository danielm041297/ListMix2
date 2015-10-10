
#ListMix2

This application is a playlist application which is portable, and can be used when I dont have internet. It is intended to be used along with the website http://www.youtube-mp3.org/ because I dont like paying for my music. Right now I am reading in the information about each song from the file name itself. The format it reads looks like this

Artist - Song_Name.mp3

Incidentally all songs on youtube have that format if it is an official music channel.. The beauty of this application is if i have a directory on a usb with all of my youtube music in it, then i can just select the directory, and the application will load all the songs into your music library using recursion.

Ideas:

 1.) Add an option to download the song directly from the app. I will send a GET request to www.youtube-mp3.org using the url the user provides as the request parameter. Then I will download the song, and add it to the library.. 

2.) Some music fingerprinting library, or creative method to recognize information of each song. This would get rid of the format mentioned above, and will make the app more extensible.
