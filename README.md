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
  <img align="center" width=180 src="https://user-images.githubusercontent.com/79296181/183625921-537824fb-d75e-4fdb-8ca7-ae1d9fa5cb32.gif" />
  <img align="center" width=180 src="https://user-images.githubusercontent.com/79296181/183625899-c0406d44-e837-4db9-9124-a0e51310eb50.gif" />
  <img align="center" width=180 src="https://user-images.githubusercontent.com/79296181/184468344-e3adc342-6ab3-4c42-8857-827d65766397.jpg" />
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

<img align="center" width=180 src="https://user-images.githubusercontent.com/79296181/183625856-597feb52-c103-4fd7-93fa-36b14c83ef91.gif" />

- There's a version for a shopping item and one for chores item
- Here the user fills in information about the item
  - Option to display a date picker dialog
  - Option to pick a priority
- Button to delete the item
- Button to add the item to the database
