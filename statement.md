# statement.md — Problem Statement & Scope

## Problem Statement
Small clinics and college-project hospitals still manage patients, doctors, appointments,
and bills on paper or scattered spreadsheets. This causes:
- Double-booked doctor slots and long patient wait times
- Lost patient history and repeated data entry
- Billing mistakes (consultation fee + tests calculated manually)
- No quick search (e.g. "find all cardiologists" or "today's appointments")

## Objectives (per PDF §1 & §4)
1. Apply subject concepts (OOP layering, HashMap/ArrayList data structures, file handling, validation) to a real-world hospital workflow.
2. Eliminate double-booking through automatic conflict detection in the service layer.
3. Persist all records in human-readable CSV files so data survives restarts with zero DB setup.
4. Demonstrate understanding through modular code, self-contained tests, UML/storage design artefacts, and a 15-section project report.

## Input / Output Structure (per PDF §2.1)
| Module | Inputs | Outputs | Stored in |
|---|---|---|---|
| Patient Management | id, name, age (0–120), gender, 10-digit phone, address, blood group | patient record / search list / validation error | database/patients.csv |
| Doctor Management | id, name, specialization, 10-digit phone, fee ≥ 0, slots (HH:MM) | doctor record / specialization list / validation error | database/doctors.csv |
| Appointment Scheduling | patientId, doctorId (must exist), date (YYYY-MM-DD), slot (HH:MM), reason | booking confirmation OR conflict error; cancel/reschedule status | database/appointments.csv |
| Billing & Reports | appointmentId (must exist), extra charges ≥ 0 | printed receipt (consult + extra = total); collection summary | in-memory (see Future Enhancements) |

## Scope
*In scope:*
- Patient registration, search, update, safe delete
- Doctor registration with specialization, fee, and availability slots
- Appointment booking with automatic conflict detection, cancel, reschedule
- Billing with printed receipt and collection report
- CSV file persistence so data survives restarts
- Input validation and file logging

*Out of scope (future enhancements):*
- Online payment gateway
- Multi-user login with passwords / role-based auth
- Web or mobile frontend
- Real database (MySQL/PostgreSQL) migration
- SMS/email appointment reminders

## Target Users
1. *Receptionist / Admin* — registers patients, manages doctors, books appointments, raises bills
2. *Doctor* — views own daily schedule (by doctor ID / date)
3. *Evaluator / Faculty* — runs the CLI, inspects code modularity, runs tests

## High-Level Features
1. Patient Management (CRUD + search)
2. Doctor Management (CRUD + specialization search)
3. Appointment Scheduling (conflict-safe booking workflow)
4. Billing & Reports (receipt + daily collection summary)
