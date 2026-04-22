Team BinarySearchers: Arnavee Lande, Divya Pardeshi, Nayan Wanjari

Project IntelGuard – Simulation of AI Agents as Insider Threats
Overview
IntelGuard is a security-focused web application that simulates AI-driven insider threats and detects malicious behavior using Data Structures & Algorithms (DSA).
The system models how internal AI agents (HR, Finance, IT) interact with sensitive data and identifies suspicious or malicious activities using:
Pattern matching (Trie)
Behavioral analysis (Sliding Window)
Role-based access control

Problem Statement
Insider threats—especially those driven by AI agents—are difficult to detect because they often mimic normal user behavior.
IntelGuard addresses this by:
Simulating agent behavior
Monitoring request patterns
Detecting anomalies in real time

Key Features
1. Keyword Threat Detection (Trie - DSA)
Uses a Trie data structure for efficient keyword matching
Detects suspicious words like:
hack, exploit, bypass, attack
2. Critical Threat Override
Certain high-risk actions trigger instant blocking
Example:
hack employee database → BLOCKED immediately
3. Sliding Window Algorithm (DSA)
Tracks request frequency using a Queue
Detects burst attacks (rapid repeated actions)
Logic:
Time window: 60 seconds
Threshold: 5 requests
If exceeded →  Burst Attack Detected
4. Threat Logging Dashboard
Displays all analyzed requests
Shows:
Agent
Prompt
Score
Status
Reason

Tech Stack
Backend: Java, Spring Boot
Frontend: HTML, CSS, Thymeleaf
DSA Used:
Trie (pattern matching)
Queue (sliding window)
HashMap (caching & tracking)


Test Cases HR
✅ Safe Request
view employee attendance
🚨 Unauthorized Access
access salary details
🔥 Critical Attack
hack employee database
⚡ Burst Attack

Write & Scan multiple times quickly → triggers:
Burst Activity Detected (60s Window)
