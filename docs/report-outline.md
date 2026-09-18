# Project Report Outline (maps to PDF §6 — all 15 sections)

1. **Cover Page** — Title, your name, reg. no., course, faculty, date
2. **Introduction** — why hospital digitization matters, Java + file-system approach
3. **Problem Statement** — copy from statement.md
4. **Functional Requirements** — 4 modules + input/output tables + CLI workflow
5. **Non-Functional Requirements** — performance, reliability, usability, maintainability, error handling, logging
6. **System Architecture** — paste docs/architecture.md diagram + layer table
7. **Design Diagrams** — render docs/*.puml on plantuml.com:
   Use Case, Class, Sequence (booking), Workflow, ER
8. **Design Decisions & Rationale** — why HashMap, why CSV not MySQL, why conflict in service layer
9. **Implementation Details** — package table (model/service/storage/util), key algorithms (conflict O(n)), validation regexes
10. **Screenshots / Results** — main menu, patient list, double-booking error, bill receipt, test output
11. **Testing Approach** — HospitalSystemTest table (6 test groups) + manual test script
12. **Challenges Faced** — CSV comma escaping, slot-format validation, counter collision after reload
13. **Learnings & Key Takeaways** — OOP layering, HashMap vs List, persistence, validation-first design
14. **Future Enhancements** — login/roles, MySQL, Swing/JavaFX GUI, reminders, payments
15. **References** — Oracle Java docs, PlantUML, VIT syllabus
