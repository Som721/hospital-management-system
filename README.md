# Hospital Management System (Java + CSV)

Console-based Hospital Management System built in **pure Java** with **CSV file persistence** — no database server, no external dependencies.


## Features

### Module 1 — Patient Management
- Register / view / search (name or phone) / update / delete patients
- Validation: 10-digit phone, age 0–120, mandatory name

### Module 2 — Doctor Management
- Add / view / search doctors by specialization
- Stores consultation fee + daily availability slots

### Module 3 — Appointment Scheduling
- Book / cancel / reschedule / complete appointments
- **Conflict detection:** same doctor + same date + same slot cannot be double-booked
- Referential integrity: patient and doctor must exist
- Views: all, by date, by doctor, by patient

### Module 4 — Billing & Reports
- Bill = doctor consultation fee + extra charges (tests/medicine)
- Printable receipt + collection summary report

## Tech Stack
- Java 17+ (tested on OpenJDK 17/25)
- CSV persistence via `java.nio.file` (`database/*.csv` — see `database/SCHEMA.md`)
- Layered architecture: `model → service → storage → Main (CLI)`
- `HashMap` for O(1) lookups, `ArrayList` for listings/search
- `java.time` for date/slot validation, file logger

## Project Structure
```
hospital-management-system/
├── README.md
├── statement.md
├── .env.example
├── .gitignore
├── Dockerfile / docker-compose.yml
├── run.sh / run.bat
├── backend/src/main/java/com/hospital/
│   ├── model/Patient.java, Doctor.java, Appointment.java, Bill.java
│   ├── service/PatientService.java, DoctorService.java, AppointmentService.java, BillingService.java
│   ├── storage/FileStorage.java
│   └── util/Validator.java, LoggerUtil.java
├── backend/src/test/java/com/hospital/HospitalSystemTest.java
├── frontend/src/main/java/com/hospital/Main.java  (console UI)
├── frontend/README.md
├── database/patients.csv, doctors.csv, appointments.csv
├── database/SCHEMA.md
└── docs/  (architecture, diagrams, report outline)
```

## Installation & Run

### Prerequisites
- JDK 17 or higher with `javac`:
```bash
java -version
javac -version
# Fedora: sudo dnf install java-17-openjdk-devel -y
# Ubuntu: sudo apt install openjdk-17-jdk -y
```

### Compile & Run (Linux/macOS)
```bash
chmod +x run.sh
./run.sh
```

### Manual Compile & Run
```bash
mkdir -p out
javac -d out $(find backend/src/main/java frontend/src/main/java -name "*.java")
java -cp out com.hospital.Main
```

### Docker (optional, mirrors friend's CampusFix layout)
```bash
cp .env.example .env
docker compose up --build
```

### Windows
```bat
run.bat
```

## Testing
No JUnit needed — self-contained test runner:
```bash
mkdir -p out
javac -d out $(find backend/src/main/java backend/src/test/java frontend/src/main/java -name "*.java")
java -cp out com.hospital.HospitalSystemTest
```
Expected: `6+ PASS` lines, `0 failed`.

| Test | What it checks |
|------|---------------|
| validator | phone/date/slot formats |
| patient CRUD | add/get/search/duplicate-ID |
| double booking | same doctor+date+slot rejected |
| cancel frees slot | rebooking after cancel works |
| billing total | fee + extra = total |
| CSV round-trip | save + load preserves data |

## Sample Workflow
1. Add patient `P001` → Add doctor `D001` (Cardiology, slots 10:00,11:00)
2. Book `P001 + D001` on `2026-10-01 10:00` → success
3. Book same slot again → rejected (conflict detection)
4. Cancel → rebook same slot → success
5. Billing → enter extra charges → receipt printed
6. Restart app → data persists from `database/*.csv`

## Non-Functional Highlights
- **Performance:** HashMap O(1) lookup by ID
- **Reliability:** auto-save CSV after every menu action + on exit
- **Usability:** menu-driven CLI with validation messages
- **Maintainability:** layered packages, single-responsibility classes
- **Error handling:** illegal input blocked with clear messages
- **Logging:** every mutation appended to `database/hospital.log`

## Screenshots
Run the app and capture:
1. Main menu
2. Patient list
3. Doctor list
4. Double-booking error message
5. Bill receipt
6. Test run output (`HospitalSystemTest`)

