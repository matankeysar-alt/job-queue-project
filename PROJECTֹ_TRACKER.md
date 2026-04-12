Job Queue Project - Skeleton Checklist (100 Hours)
Phase 1: Infrastructure & Setup (Environment Configuration)

[x] Install IDE (IntelliJ IDEA Ultimate) and Git.

[x] Create a GitHub repository.

[x] Generate initial Spring Boot project (Spring Web) and perform initial Push.

Phase 2: Core Engine (No Network or Database)

[x] Define Task interface (with execute method).

[x] Create DummyTask class simulating work (Sleep).

[x] Create PriorityBlockingQueue (Bounded queue for memory management).

[x] Set up ThreadPoolExecutor (Worker pool) listening to the queue.

Phase 3: API & Network Shell (Client-Server)

[x] Write Controller for POST requests (Injecting tasks to the queue).

[x] Write Controller for GET requests (Fetching queue state and statuses).

[x] Manual testing via Postman or browser to ensure the server receives tasks.

Phase 4: Database (Persistence)

[x] Connect project to a DB (Initially in-memory H2, later PostgreSQL/MySQL).

[x] Create an appropriate table and save every incoming task to the DB.

[x] Real-time task status updates in DB (Pending -> Running -> Completed/Failed).

Phase 5: Advanced Tasks (AI Integration)

[x] Add AiSummarizeTask implementing the Task interface.

[x] Connect to OpenAI API for text summarization as an asynchronous task.

Phase 6: Automation & Load Testing (QA Automation)

[x] Set up an Apache JMeter Test Plan for the task creation endpoint.

[x] Send 1,000 concurrent requests to the server.

[x] Verify queue backpressure triggers in time, urgent tasks are prioritized, and server doesn't crash.

Phase 7: User Interface (Frontend Dashboard)

[x] Use AI to write a clean HTML/JS page.

[x] Connect the page to the API to display task table, real-time statuses, and a test injection button.

Phase 8: Packaging & Release (Deployment & Portfolio)

[x] Package project into a Docker Container.

[ ] Write a professional GitHub README.md (incl. screenshots, architecture explanation, and QA summary).

[ ] Add project to Resume and LinkedIn profile.