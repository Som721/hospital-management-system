# Workflow Diagram (text version — redraw in report tool)

```
START
  |
  v
Register Patient? --no--> Search Patient
  | yes                      |
  v                          v
Add Patient (validate) --> Select Patient
                             |
                             v
                     Search Doctor by Specialization
                             |
                             v
                     Pick Doctor + Date + Slot
                             |
                             v
                     Conflict? --yes--> Show error, pick another slot
                       | no
                       v
                   Confirm Booking --> Save CSV --> Generate Bill
                       |
                       v
                 Print Receipt --> Reports --> END
```

# ER Diagram (text version)

```
PATIENT (id PK, name, age, gender, phone, address, bloodGroup)
   |
   | 1 --- * APPOINTMENT (id PK, patientId FK, doctorId FK, date, slot, status, reason)
   |
DOCTOR (id PK, name, specialization, phone, fee, slots)
```

Render the .puml files at https://www.plantuml.com/plantuml/ and paste PNGs into the report.
