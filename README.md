<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/YouTube_full-color_icon_%282017%29.svg/512px-YouTube_full-color_icon_%282017%29.svg.png" alt="" align="center" width="225" height="150"><h1 align="center">Unwind - Timeshare Exchange Platform</h1>
<p align="center"><a href="#project-description">Project Description</a> - <a href="#key-features">Key Features</a> - <a href="#technology-stack">Tech Stack</a></p>

<img src="https://repolaunch.vercel.app/assets/img/yt.webp" alt="" align="center" width="auto" height="auto">

## Project Description

This project is a full-featured video-sharing platform inspired by YouTube. It allows users to upload, view, and interact with videos. The platform supports high-quality video streaming, personalized recommendations, and user authentication via OAuth2.0. Key functionalities include a video recommendation engine, cloud storage for uploaded content, and responsive playback on any device. The project utilizes modern web technologies and best practices in full-stack development to deliver a seamless user experience.

## Key Features

🚩**Booking & Payment System**

*   **Book Timeshares**: Users can browse and book available timeshare properties based on preferred dates and locations.
*   **Wallet System (Unwind Wallet)**: Users can deposit money into their Unwind Wallet for seamless transactions.
*   **Multiple Payment Methods:** Supports VNPay and Unwind Wallet for payments when booking timeshares or listing properties.

🚩**Interactive Map & Navigation**

*   **OpenStreetMap Integration**: Provides a map view of timeshare properties.
*   **Nearby Locations**: Users can find restaurants, tourist attractions, and essential services near their booked timeshare.
*   **Route Guidance**: Offers directions to booked timeshares using OpenStreetMap’s navigation system.

🚩**User Engagement & Interaction**

*   **Feedback & Reviews**: Users can leave reviews after their stay, helping maintain transparency and trust.
    
*   **Cancel Booking**: If necessary, users can cancel their booking and receive a refund based on cancellation policies.
    
*   **Notifications System**: Real-time push notifications for booking confirmations, payment status, messages, and more.
    

## Tech Stack

**Language**: Kotlin

**Architecture**: MVVM

**Networking**: Retrofit + OkHttp

**Database**: Room Database

**Dependency Injection**: Hilt

**Push Notifications**: Firebase Cloud Messaging (FCM)

**Payment Integration**: VNPay (Sandbox), Unwind Wallet