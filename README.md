# house-mate

App meant for house mates to share a list of items to buy and chores to do in the house.

- Users first create a group ID and a user ID is created automatically
- Once the group is created in a firestore database, other users can join the group using the group id
- Users can add shopping and chore items with more data (like priority and who added it)
- All this data is shared in realtime among members of the group

(Only the Android version is completed)

---

### Tools and skills used:

- MVVM architecture
  - Shared ViewModel
- Material Design
- Jetpack Navigation Component
- Modal Bottom Sheet
- ViewPager 2 Tabs
- Firebase Firestore Database
  - Complex queries
- LiveData
  - Livedata Observers
  - Kotlin Flow
- Kotlin coroutines (for synchronous excecutions)
- RecyclerView
- Date Picker

---

### Item Lists Screen

<p align="left" style="display:flex">
  <img align="center" width=180 src="https://user-images.githubusercontent.com/79296181/189297482-6a518c9f-1dbd-4ee7-bbf0-0159ad3a1f1d.gif" />
  <img align="center" width=180 src="https://user-images.githubusercontent.com/79296181/189297487-f454fa5a-63b0-46c1-b7fa-4deadfa180ac.gif" />
  <img align="center" width=180 src="https://user-images.githubusercontent.com/79296181/189297379-eb1da789-8d7c-4d07-b2dd-2989f46c2b37.jpg" />
</p>

- One tab for shopping items, one tab for chore items
- Items have the name of the item and a checkbox to mark it completed
  - Each item has the color of it's priority (red = 1, yellow = 2, green = 3)
- Click on an item and bottom sheet pops up with more data about the item
  - Option to write in a volunteer for the item
  - Option to edit the item
- When Edit mode is on, the user can edit or delete items by pressing a button
- Press the floating action button to add a new item
- The items are updated in realtime when the list is edited from another client device

---

### Add Item Screen

<img align="center" width=180 src="https://user-images.githubusercontent.com/79296181/189297433-98994e06-5426-4410-aec1-5b13803b1b68.gif" />

- There's a version for a shopping item and one for chores item
- Here the user fills in information about the item
  - Option to display a date picker dialog
  - Option to pick a priority
- Button to delete the item
- Button to add the item to the database
