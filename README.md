# 🚀 Smart Job Queue Infrastructure

A high-performance, asynchronous job processing system built with Java (Spring Boot). Designed to handle heavy background tasks with built-in concurrency management, priority queuing, and backpressure mechanisms.

## 🌟 Key Features

* **Thread-Safe Concurrency**: Custom implementation of worker threads using `ThreadPoolExecutor` and semaphores to prevent race conditions and ensure thread safety.
* **Smart Priority Queueing**: VIP tasks dynamically bypass standard tasks and are executed first when a worker thread becomes available.
* **Active Backpressure**: Protects the server from Out-Of-Memory (OOM) errors by rejecting excess tasks with an HTTP 503 status when the bounded queue reaches maximum capacity.
* **AI Integration**: Features an `AiSummarizeTask` that asynchronously communicates with the OpenAI API.
* **Real-Time Dashboard**: A vanilla HTML/JS frontend using polling to visualize queue status, memory limits, and task history in real-time.
* **Dockerized**: Fully packaged using a multi-stage Docker build for seamless, environment-agnostic deployment.

## 🛠️ Tech Stack

* **Backend**: Java 23, Spring Boot, Spring Data JPA
* **Database**: H2 (In-Memory Database)
* **Frontend**: HTML5, CSS3, Vanilla JavaScript (Fetch API)
* **DevOps / Deployment**: Docker, Git

## 🚀 Getting Started

### Prerequisites
* Docker installed on your machine.
* An OpenAI API Key (required for the AI Summarize Task).

### Build and Run via Docker
You can build and run the entire system (Backend + Database + UI) purely through Docker, without needing Java installed locally.

1. Build the image:
   `docker build -t job-queue-app .`

2. Run the container (replace with your actual API key):
   `docker run -p 8080:8080 -e OPENAI_API_KEY="your_openai_api_key_here" job-queue-app`

Once the container is running, open your browser and navigate to:
`http://localhost:8080/`

## 📡 REST API Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/tasks/add` | Submit a new task (Dummy or AI) with a specific priority level. |
| `GET` | `/api/tasks/status` | Get real-time queue metrics (pending tasks, remaining capacity). |
| `GET` | `/api/tasks/all` | Retrieve the history and execution status of all submitted tasks. |