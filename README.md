# house-mate

App meant for house mates to share a list of items to buy and chores to do in the house.

- Users first create a group ID and a user ID is created automatically
- Once the group is created in a firestore database, other users can join the group using the group id
- Users can add shopping and chore items with more data (like priority and who added it)
- All this data is shared in realtime among members of the group

---

### Tools and skills used:

- MVVM architecture
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

- There's a version for a shopping item and one for chores item
- Here the user fills in information about the item
  - Option to display a date picker dialog
  - Option to pick a priority
- Button to delete the item
- Button to add the item to the database
