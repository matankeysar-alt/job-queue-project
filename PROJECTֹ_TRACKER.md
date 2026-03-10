Job Queue Project - Skeleton Checklist (100 Hours)
Phase 1: Infrastructure & Setup (Environment Configuration)

[x] Install IDE (IntelliJ IDEA Ultimate) and Git.

[ ] Create a GitHub repository.

[ ] Generate initial Spring Boot project (Spring Web) and perform initial Push.

Phase 2: Core Engine (No Network or Database)

[ ] Define Task interface (with execute method).

[ ] Create DummyTask class simulating work (Sleep).

[ ] Create PriorityBlockingQueue (Bounded queue for memory management).

[ ] Set up ThreadPoolExecutor (Worker pool) listening to the queue.

Phase 3: API & Network Shell (Client-Server)

[ ] Write Controller for POST requests (Injecting tasks to the queue).

[ ] Write Controller for GET requests (Fetching queue state and statuses).

[ ] Manual testing via Postman or browser to ensure the server receives tasks.

Phase 4: Database (Persistence)

[ ] Connect project to a DB (Initially in-memory H2, later PostgreSQL/MySQL).

[ ] Create an appropriate table and save every incoming task to the DB.

[ ] Real-time task status updates in DB (Pending -> Running -> Completed/Failed).

Phase 5: Advanced Tasks (AI Integration)

[ ] Add AiSummarizeTask implementing the Task interface.

[ ] Connect to OpenAI API for text summarization as an asynchronous task.

Phase 6: Automation & Load Testing (QA Automation)

[ ] Write a Python script or use JMeter/Postman.

[ ] Send 10,000 concurrent requests to the server.

[ ] Verify queue backpressure triggers in time, urgent tasks are prioritized, and server doesn't crash.

Phase 7: User Interface (Frontend Dashboard)

[ ] Use AI (Cursor/Claude) to write a clean HTML/JS page.

[ ] Connect the page to the API to display task table, real-time statuses, and a test injection button.

Phase 8: Packaging & Release (Deployment & Portfolio)

[ ] Package project into a Docker Container.

[ ] Write a professional GitHub README.md (incl. screenshots, architecture explanation, and QA summary).

[ ] Add project to Resume and LinkedIn profile.