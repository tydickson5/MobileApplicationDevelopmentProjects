# MobileApplicationDevelopmentProjects for Ty Dickson (tbf8je)

## HW1 Helloworld
I used only the material provided in class and from the Jetpack Compose Components linked on the homework instruction file. I used AI to help setup the project folder and github repo. I accepted its solution for that.

## HW2 UVA Bucket List
For this app, I used the components from the companion notes, in class material, and had a little help from AI. I used AI to help walk me through the date picker as well as show me some more options for text fields, checkboxes, and just help fix overall bugs. I accepted most of its solutions although there were some UI options I rejected because I did not like how they looked.

## HW3 College Football
For this app, I used as much as I could from the slides to set up a room using flow and the api to pull the games. I did use some Ai to piece those together into the DB and then into the view model. I also used some AI just to help set up some of the UI for the pieces I didn't know how to do as well as for the date/time conversions. For the date mapping function I used this one which was recommended to me by Claude. 

```
fun currentWeek(nowMillis: Long = System.currentTimeMillis()): Int {
    val et = TimeZone.getTimeZone("America/New_York")
    val start = Calendar.getInstance(et).apply { clear(); set(2026, Calendar.AUGUST, 31) }.timeInMillis
    val days = Math.floorDiv(nowMillis - start, 86_400_000L)
    return (Math.floorDiv(days, 7L).toInt() + 1).coerceIn(1, 14)
}
```
## HW4 Step Counter
Note: I could not get the accelerometer on my Macbook to connect to the emulator, so I used a test function that produced step data. For this app I used AI to help my build some of the step tracker and the graph UI on the screen. Other than that I just used methods shown in the slides.

For the step tracker, I used SENSOR_DELAY_NORMAL with the accelerometer. This gave a moderate sampling rate, but it was enough to detect step movements. I chose 2.0 m/s^2 for the acceleration to distinguish the step movements from others. There is also a 300-millisecond minimum delay between detecting steps to reduce repeated steps that are actually just one step. As mentioned in the spec, it will stop tracking when the app is closed to help save battery life.

Ethics Refleciton:
1. Collecting data like this over many days could show what someone's regular schedule might look like. This could lead to knowing when they might be home versus away exercizing or at work.
2. Collecting only the necessary data is very important. Nothing more that step count should be stored in the room (for this app) because that is all that is needed. It should also only be retained for the minimum amount of time needed.
3. It is important that the user is informed of what data is being collected. It should be clear when accepting permissions what is being used and how. It should also be clear how and where the data is stored, and there should be a way to delete/clear that data. They should also be able to decline permissions while being able to still access unrelated app functionality
