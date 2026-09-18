# System Architecture

```
              +------------------+
              |  Main (CLI UI)   |  Scanner menus, validation prompts
              +--------+---------+
                       |
        +--------------+--------------+----------------+
        |              |              |                |
+-------+------+ +-----+-------+ +----+---------+ +----+----------+
| PatientService| |DoctorService| |AppointmentSvc| |BillingService |
| HashMap<P>    | |HashMap<D>   | |HashMap<A>    | |List<Bill>     |
+-------+------+ +-----+-------+ +----+---------+ +---------------+
        |              |              |
        +--------------+--------------+
                       |
              +--------+---------+
              |  FileStorage     |  CSV load/save (java.nio.file)
              +--------+---------+
                       |
              +--------+---------+
              | database/*.csv + log | patients, doctors, appointments
              +------------------+
```

**Design decisions:**
- Layered design: UI never touches files directly; all access via services.
- HashMap (`LinkedHashMap`) for O(1) ID lookup while preserving insertion order for listings.
- CSV over SQLite/MySQL: zero setup for viva, human-inspectable, git-friendly.
- Conflict rule enforced in `AppointmentService.bookAppointment()` (single choke point).
- Logging isolated in `LoggerUtil` so it can never crash the app.
