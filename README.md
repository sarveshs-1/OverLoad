# Overload 

A native Android fitness and progression tracking application built in Java. Overload is designed to optimize gym performance by tracking individual workout sets, organizing training schedules, and utilizing local data persistence to monitor physical progression over time.

##  Key Features

* **Progressive Overload Tracking:** Structured log system to record workout sets, reps, and weight progression.
* **Component-Driven UI:** Uses a modular Fragment architecture (`TodayFragment`, `AddWorkoutFragment`) for smooth, fluid screen transitions.
* **Dynamic Content Rendering:** Implements a custom `WorkoutAdapter` to dynamically bind and display exercise data efficiently within the user interface.
* **Secure User Onboarding:** Includes a dedicated `LoginActivity` flow to manage user access sessions.

##  Architecture & Tech Stack

* **Language:** Java
* **Platform:** Android SDK / Android Studio
* **UI Layouts:** XML (Responsive ConstraintLayouts & Material Components)
* **Local Data Persistence:** SQLite database layer managed via an abstracted Data Access Object (`WorkoutDao.java` & `AppDatabase.java`) to ensure zero data loss between app sessions.
