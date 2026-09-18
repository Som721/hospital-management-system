# statement.md — Problem Statement & Scope

## Problem Statement
Small clinics and college-project hospitals still manage patients, doctors, appointments,
and bills on paper or scattered spreadsheets. This causes:
- Double-booked doctor slots and long patient wait times
- Lost patient history and repeated data entry
- Billing mistakes (consultation fee + tests calculated manually)
- No quick search (e.g. "find all cardiologists" or "today's appointments")

## Scope
**In scope:**
- Patient registration, search, update, safe delete
- Doctor registration with specialization, fee, and availability slots
- Appointment booking with automatic conflict detection, cancel, reschedule
- Billing with printed receipt and collection report
- CSV file persistence so data survives restarts
- Input validation and file logging

**Out of scope (future enhancements):**
- Online payment gateway
- Multi-user login with passwords / role-based auth
- Web or mobile frontend
- Real database (MySQL/PostgreSQL) migration
- SMS/email appointment reminders

## Target Users
1. **Receptionist / Admin** — registers patients, manages doctors, books appointments, raises bills
2. **Doctor** — views own daily schedule (by doctor ID / date)
3. **Evaluator / Faculty** — runs the CLI, inspects code modularity, runs tests

## High-Level Features
1. Patient Management (CRUD + search)
2. Doctor Management (CRUD + specialization search)
3. Appointment Scheduling (conflict-safe booking workflow)
4. Billing & Reports (receipt + daily collection summary)
