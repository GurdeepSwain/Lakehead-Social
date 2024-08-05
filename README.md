# Lakehead Social

Lakehead Social is a social media application designed to connect users by allowing them to follow each other, post updates (with or without images), and view updates from followed users. This project demonstrates the integration of Firebase for authentication, Firestore for data storage, and Firebase Storage for image storage.

## Features

- User Authentication: Users can register and log in using their email and password.
- Profile Management: Users can upload a profile picture, and update their name, email, and bio.
- Posting Updates: Users can create new posts with text and optional images.
- Following Users: Users can follow and unfollow other users.
- Notifications: Users receive notifications when they gain a new follower or when someone they follow posts a new update.
- Responsive Design: The app is designed to be visually appealing and easy to use on various screen sizes.

## Screenshots

  <img width="300" alt="image" src="https://github.com/user-attachments/assets/20386e30-fb84-4df1-914e-d0d2035151d6">
  <img width="300" alt="image" src="https://github.com/user-attachments/assets/e21a2813-234f-405d-8e12-9ec297727686">
  <img width="300" alt="image" src="https://github.com/user-attachments/assets/043223d5-4e4f-4c60-9f02-1e8a52888db0">
<!-- ![Profile Screen](https://github.com/user-attachments/assets/20386e30-fb84-4df1-914e-d0d2035151d6)
![Home Screen](screenshots/home_screen.png)
![Friends Screen](screenshots/friends_screen.png)
![Following Screen](screenshots/following_screen.png)
 -->

 
## Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/GurdeepSwain/lakehead-social.git
    cd lakehead-social
    ```

2. Open the project in Android Studio.

3. Add your Firebase configuration file (google-services.json) to the `app` directory.

4. Sync the project with Gradle files.

5. Build and run the project on an Android device or emulator.

## Usage

### Register
1. Open the app.
2. Click on the "Register" button on the intro screen.
3. Fill in the required fields (name, email, password, bio) and optionally select a profile image.
4. Click on "Register" to create a new account.

### Login
1. Open the app.
2. Click on the "Login" button on the intro screen.
3. Enter your email and password.
4. Click on "Login" to access your account.

### Profile
1. After logging in, you can view your profile by clicking on the "Profile" icon in the bottom navigation bar.
2. Update your profile details and profile image if needed.

### Posting
1. On the home screen, click on the floating action button (+) to create a new post.
2. Enter your post text and optionally select an image.
3. Click on "Post" to share your update.

### Following Users
1. Navigate to the "Friends" screen by clicking on the "Friends" icon in the bottom navigation bar.
2. Click on the "Follow" button next to the user you want to follow.
3. To unfollow, click on the "Unfollow" button next to the user.

## Firebase Setup

To use Firebase services (authentication, Firestore, and storage), follow these steps:

1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Create a new project or use an existing project.
3. Add an Android app to your Firebase project.
4. Download the `google-services.json` file and place it in the `app` directory of your project.
5. Enable Email/Password authentication in the Firebase Authentication section.
6. Create Firestore and Storage databases.

## Contributing

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your changes (`git commit -m 'Add some feature'`).
5. Push to the branch (`git push origin feature-branch`).
6. Open a pull request.

<!--## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

For any questions or suggestions, please contact:

- [Gurdeep Swain](mailto:gurdeep.swain1@gmail.com)
- GitHub: [GurdeepSwain](https://github.com/GurdeepSwain)
--->
---

