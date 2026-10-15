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
