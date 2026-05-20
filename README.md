<div align="center">

# 🌐 ENVECHAT

**A Full-Stack Real-Time Communication Platform**

[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)](https://reactjs.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![WebSockets](https://img.shields.io/badge/WebSockets-010101?style=for-the-badge&logo=socket.io&logoColor=white)](#)
[![Vercel](https://img.shields.io/badge/Vercel-000000?style=for-the-badge&logo=vercel&logoColor=white)](https://vercel.com/)
[![Render](https://img.shields.io/badge/Render-46E3B7?style=for-the-badge&logo=render&logoColor=white)](https://render.com/)

[**Live Application**](https://envechat-frontend.vercel.app) | [**Backend API**](https://envechat.onrender.com) | [**Frontend Repository**](https://github.com/NallapaneniHemanthSai/envechat-frontend)

</div>

---

## 🚀 Project Introduction

**ENVECHAT** is a robust, full-stack real-time communication platform designed for seamless messaging and user engagement. It leverages the power of Spring Boot and WebSockets on the backend for highly scalable, low-latency communication, and React with Tailwind CSS on the frontend for a responsive and intuitive user interface. 

Built with modern software architecture principles, ENVECHAT ensures secure authentication, persistent message storage, and live user status tracking, delivering a production-ready messaging experience.

---

## ✨ Features

- **Real-Time Communication:** Instant messaging with sub-second latency powered by WebSockets.
- **Secure Authentication:** Robust user authentication and authorization mechanisms.
- **Live User Status Tracking:** Instantly see who is online, typing, or away.
- **Scalable Backend Architecture:** Designed to handle concurrent connections efficiently.
- **Persistent Message Storage:** All conversations are securely stored in PostgreSQL.
- **Responsive UI:** A beautiful, accessible, and mobile-friendly interface.

---

## 💻 Tech Stack

### Frontend
- **Framework:** React.js
- **Styling:** Tailwind CSS

### Backend
- **Framework:** Spring Boot (Java)
- **Real-time Engine:** WebSockets
- **API:** RESTful APIs
- **Build Tool:** Maven

### Database
- **Primary Database:** PostgreSQL

### Deployment
- **Frontend:** Vercel
- **Backend:** Render

---

## 🏗️ System Architecture

*(Placeholder for System Architecture Diagram - e.g., a flowchart showing client-server interaction via WebSockets and REST APIs)*

> **Note:** Diagram to be added. 

---

## 📸 Screenshots

*(Placeholder for Screenshots / GIF Demos)*

| Login/Signup | Chat Interface | User Status |
| :---: | :---: | :---: |
| <img src="https://via.placeholder.com/400x250?text=Login+Screen" width="400" /> | <img src="https://via.placeholder.com/400x250?text=Chat+Interface" width="400" /> | <img src="https://via.placeholder.com/400x250?text=User+Status" width="400" /> |

---

## 🔌 API Endpoints

Below is a sample of the REST API endpoints available in the application. WebSockets handle real-time messaging events at `/ws`.

| HTTP Method | Endpoint | Description | Auth Required |
| --- | --- | --- | :---: |
| `POST` | `/api/auth/register` | Register a new user | ❌ |
| `POST` | `/api/auth/login` | Authenticate and retrieve token | ❌ |
| `GET` | `/api/users/me` | Get current logged-in user profile | ✅ |
| `GET` | `/api/rooms` | Retrieve all chat rooms | ✅ |
| `POST` | `/api/rooms` | Create a new chat room | ✅ |
| `GET` | `/api/messages/{roomId}` | Get message history for a room | ✅ |

---

## 🛠️ Getting Started

Follow these instructions to set up the project locally.

### Prerequisites
- Java 17+
- Node.js & npm/yarn
- PostgreSQL Database
- Maven

### 1. Clone the Repositories

First, clone the backend and frontend repositories:

```bash
# Clone Backend
git clone https://github.com/NallapaneniHemanthSai/envechat.git
cd envechat

# Clone Frontend (in a separate terminal/folder)
git clone https://github.com/NallapaneniHemanthSai/envechat-frontend.git
cd envechat-frontend
```

### 2. Configure Environment Variables

**Backend (`src/main/resources/application.properties`):**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/envechat
spring.datasource.username=your_postgres_user
spring.datasource.password=your_postgres_password
# Add your JWT Secret and other required variables here
```

**Frontend (`.env`):**
```env
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_WS_URL=ws://localhost:8080/ws
```

### 3. Run the Backend

Navigate to the backend directory and run:

```bash
./mvnw spring-boot:run
```
*The server will start on `http://localhost:8080`.*

### 4. Run the Frontend

Navigate to the frontend directory, install dependencies, and start the development server:

```bash
npm install
npm start
```
*The React app will open at `http://localhost:3000`.*

---

## 📂 Folder Structure

**Backend Overview:**
```text
backend/
├── src/
│   ├── main/
│   │   ├── java/com/envechat/backend/
│   │   │   ├── config/       # WebSocket, Security, and CORS configurations
│   │   │   ├── controller/   # REST API Controllers
│   │   │   ├── model/        # JPA Entities (User, Message, Room)
│   │   │   ├── repository/   # Spring Data JPA Repositories
│   │   │   └── service/      # Business Logic (Chat Service, Auth Service)
│   │   └── resources/
│   │       └── application.properties
├── pom.xml
└── README.md
```

---

## 🚀 Deployment Instructions

### Deploying the Frontend to Vercel
1. Connect your GitHub account to [Vercel](https://vercel.com/).
2. Import the `envechat-frontend` repository.
3. Configure the **Environment Variables** in the Vercel dashboard (`REACT_APP_API_URL`, etc.).
4. Click **Deploy**. Vercel will automatically build and deploy your React app.

### Deploying the Backend to Render
1. Create a [Render](https://render.com/) account.
2. Setup a new **PostgreSQL** database on Render and save the connection string.
3. Create a new **Web Service** and link your `envechat` (backend) GitHub repository.
4. Set the build command to `./mvnw clean package -DskipTests` and the start command to `java -jar target/backend-0.0.1-SNAPSHOT.jar` (update the jar name as per your `pom.xml`).
5. Add all required **Environment Variables** (e.g., `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`).
6. Click **Deploy Web Service**.

---

## 🔮 Future Improvements

- [ ] Implement end-to-end encryption for private messages.
- [ ] Add file and image sharing capabilities.
- [ ] Introduce voice and video calling features (WebRTC).
- [ ] Implement group chat roles and permissions.
- [ ] Add push notifications for offline users.

---

## 🤝 Contribution Guidelines

Contributions are welcome! If you'd like to improve ENVECHAT, please follow these steps:

1. Fork the repository.
2. Create a new feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---

<div align="center">
  <p>Built with ❤️ by <a href="https://github.com/NallapaneniHemanthSai">Hemanth Sai Nallapaneni</a></p>
</div>
