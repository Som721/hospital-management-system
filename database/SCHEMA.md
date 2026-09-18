# database/ — CSV file storage (no DB server required)

Files (headers only — add records via the app):
- `patients.csv`: `id,name,age,gender,phone,address,bloodGroup`
- `doctors.csv`: `id,name,specialization,phone,fee,slots` (slots separated by `;`, e.g. `10:00;11:00`)
- `appointments.csv`: `id,patientId,doctorId,date,slot,status,reason` (date `YYYY-MM-DD`, slot `HH:MM` 24h, status `BOOKED|CANCELLED|COMPLETED`)
- `hospital.log`: created at runtime, appends every mutation (git-ignored)

## Schema / ER
```
PATIENT (id PK, name, age, gender, phone, address, bloodGroup)
   | 1 --- * APPOINTMENT (id PK, patientId FK, doctorId FK, date, slot, status, reason) * --- 1
DOCTOR (id PK, name, specialization, phone, fee, slots)
```
Referential integrity is enforced in `AppointmentService`: patient and doctor must exist;
same doctor + date + slot cannot be double-booked while `BOOKED`.
